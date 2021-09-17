package cn.dcube.ahead.soc.dp.service.handler;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.dcube.ahead.commons.proto.transport.EventTransportEntity;
import cn.dcube.ahead.redis.service.RedisService;
import cn.dcube.ahead.soc.dp.config.DPConfig;
import cn.dcube.ahead.soc.dp.config.DPEventModuleConfig;
import lombok.extern.slf4j.Slf4j;

/**
 * GEO地理位置回填
 * 
 * @author yangfei
 *
 */
@Service
@Slf4j
@ConditionalOnBean(DPConfig.class)
public class GeoRefillHandler implements IRefillHandler {

	public static final String SRC = "src";

	public static final String DST = "dst";

	public static final String IP = "ip";

	@Autowired
	private RedisService redisService;

	@Override
	public String getCategory() {
		return "GEO";
	}

	@Override
	public void handle(String moduleName, DPEventModuleConfig module, EventTransportEntity event) {
		try {
			if (module != null && module.isEnable()) {
				log.info("处理Event:{}的GEO信息丰富化", event.getEventId(), moduleName);
				// 处理数据
				Map<String, Object> eventData = (Map<String, Object>) event.getEventData();

				// 04. 根据IP范围回填网域
				Map<String, String> ipFields = module.getIpFields();
				for (Entry<String, String> ipFiledEntry : ipFields.entrySet()) {
					if (SRC.equals(ipFiledEntry.getKey()) || DST.equals(ipFiledEntry.getKey())) {
						String prexif = ipFiledEntry.getKey() + "_";
						Object value = eventData.get(ipFiledEntry.getValue());
						if (value != null) {
							// 回填网域
							Set<String> domainSets = redisService.zrangeByScore("soc_domain_ip", 0D,
									Double.valueOf(value.toString()), 0, 1);
							if (domainSets != null && domainSets.size() > 0) {
								String domainId = domainSets.iterator().next();
								if (!domainId.isEmpty()) {
									String domainInfo = redisService.getCacheMapValue("domain_info", domainId);
									if (!domainInfo.isEmpty()) {
										JSONObject json = JSON.parseObject(domainInfo);
										eventData.put(prexif + "DomainId", domainId); // 网域id
										eventData.put(prexif + "DomainType", json.get("ipType")); // 网域类型
										if (eventData.get(prexif + "OrgId") == null) {
											eventData.put(prexif + "OrgId", json.get("deptId")); // 组织
										}
									}
								}
							}
							// 回填地理位置
							Set<String> geoSets = redisService.zrangeByScore("geolite_city_ipv4", 0D,
									Double.valueOf(value.toString()), 0, 1);
							if (domainSets != null && domainSets.size() > 0) {
								String geos = geoSets.iterator().next().replace("[", "").replace("]", "");
								if (geos.isEmpty()) {
									String[] geoArray = geos.split("_");
									String geoId = geoArray[0]; // 0是geoid,3、4是经纬度
									eventData.put(prexif + "Latitude", geoArray[3]);
									eventData.put(prexif + "Longitude", geoArray[4]);
									// 查找geoId
									if (!geoId.isEmpty()) {
										String geoInfo = redisService.getCacheMapValue("geolite2_city_locations_zh_cn",
												geoId);
										if (!geoInfo.isEmpty() && geoInfo.indexOf(":") > 0) {
											String[] geoInfoArray = geoInfo.split(":", -1);
											eventData.put(prexif + "Geo1Name",
													geoInfoArray[4].isEmpty() ? "" : geoInfoArray[4]);
											eventData.put(prexif + "Geo1Code",
													geoInfoArray[3].isEmpty() ? "" : geoInfoArray[3]);
											eventData.put(prexif + "Geo2Name",
													geoInfoArray[6].isEmpty() ? "" : geoInfoArray[6]);
											eventData.put(prexif + "Geo2Code",
													geoInfoArray[5].isEmpty() ? "" : geoInfoArray[5]);
											eventData.put(prexif + "Geo3Name",
													geoInfoArray[9].isEmpty() ? "" : geoInfoArray[9]);
											eventData.put(prexif + "Geo3Code",
													geoInfoArray[10].isEmpty() ? "" : geoInfoArray[10]);
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			log.info("处理Event:{}的{}信息丰富化失败!", event.getEventId(), moduleName);
			log.error("", e);
		}
	}

}
