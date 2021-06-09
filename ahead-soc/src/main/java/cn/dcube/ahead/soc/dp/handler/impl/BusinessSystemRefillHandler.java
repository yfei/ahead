package cn.dcube.ahead.soc.dp.handler.impl;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.dcube.ahead.commons.proto.transport.EventTransportEntity;
import cn.dcube.ahead.commons.util.StringUtils;
import cn.dcube.ahead.redis.service.RedisService;
import cn.dcube.ahead.soc.dp.config.DPConfig;
import cn.dcube.ahead.soc.dp.config.DPEventModuleConfig;
import cn.dcube.ahead.soc.dp.handler.IRefillHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 业务系统回填
 * @author yangfei
 *
 */
@Service
@Slf4j
public class BusinessSystemRefillHandler implements IRefillHandler {

	@Autowired
	private DPConfig config;

	@Autowired
	private RedisService redisService;

	private DPEventModuleConfig moduleConfig;

	@PostConstruct
	public void init() {
		moduleConfig = config.getEvent().getModules().get("business");
		log.debug("业务系统回填处理器[{}]!", moduleConfig.isEnable() ? "已开启" : "未开启");
	}

	@Override
	public void handle(EventTransportEntity event) {
		if (moduleConfig.isEnable()) {
			log.debug("event:{}进行资产回填!", event.getEventId());
			String srcBusiness = redisService.getCacheMapValue(moduleConfig.getRedisKey(), event.getSrcIp());
			if (StringUtils.isNotEmpty(srcBusiness)) {
				moduleConfig.getFields().forEach((k, v) -> {
					JSONObject businessInfo = JSON.parseObject(srcBusiness);
					if (businessInfo.get(v) != null) {
						Map<String, Object> eventData = (Map<String, Object>) event.getEventData();
						eventData.put(k, businessInfo.get(v));
					}
				});
			}
			String dstBusiness = redisService.getCacheMapValue(moduleConfig.getRedisKey(), event.getDstIp());
			if (StringUtils.isNotEmpty(dstBusiness)) {
				moduleConfig.getFields().forEach((k, v) -> {
					JSONObject businessInfo = JSON.parseObject(dstBusiness);
					if (businessInfo.get(v) != null) {
						Map<String, Object> eventData = (Map<String, Object>) event.getEventData();
						eventData.put(k, businessInfo.get(v));
					}
				});
			}
		}

		if (this.getNext() != null) {
			this.getNext().handle(event);
		}
	}

	@Override
	public IRefillHandler getNext() {
		return null;
	}

}
