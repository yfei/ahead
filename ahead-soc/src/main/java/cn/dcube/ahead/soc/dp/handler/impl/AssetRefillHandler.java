package cn.dcube.ahead.soc.dp.handler.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.dcube.ahead.commons.proto.transport.EventTransportEntity;
import cn.dcube.ahead.redis.service.RedisService;
import cn.dcube.ahead.soc.dp.config.DPConfig;
import cn.dcube.ahead.soc.dp.config.DPEventModuleConfig;
import cn.dcube.ahead.soc.dp.handler.IRefillHandler;
import cn.dcube.ahead.utils.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 资产回填
 * 
 * @author yangfei
 *
 */
@Service
@Slf4j
public class AssetRefillHandler implements IRefillHandler {

	@Autowired
	private DPConfig config;

	@Autowired
	private RedisService redisService;

	@Autowired
	private BusinessSystemRefillHandler businessSystemHandler;

	private DPEventModuleConfig moduleConfig;

	public void init() {
		moduleConfig = config.getEvent().getModules().get("asset");
		log.debug("资产回填处理器[{}]!", moduleConfig.isEnable() ? "已开启" : "未开启");
	}

	@Override
	public void handle(EventTransportEntity event) {
		if (moduleConfig.isEnable()) {
			log.debug("event:{}进行资产回填!", event.getEventId());
			String srcAsset = redisService.getCacheMapValue(moduleConfig.getRedisKey(), event.getSrcIp());
			if (StringUtils.isNotEmpty(srcAsset)) {
				moduleConfig.getFields().forEach((k, v) -> {
					JSONObject assetInfo = JSON.parseObject(srcAsset);
					if (assetInfo.get(v) != null) {
						Map<String, Object> eventData = (Map<String, Object>) event.getEventData();
						eventData.put(k, assetInfo.get(v));
					}
				});
			}
			String dstAsset = redisService.getCacheMapValue(moduleConfig.getRedisKey(), event.getDstIp());
			if (StringUtils.isNotEmpty(dstAsset)) {
				moduleConfig.getFields().forEach((k, v) -> {
					JSONObject assetInfo = JSON.parseObject(dstAsset);
					if (assetInfo.get(v) != null) {
						Map<String, Object> eventData = (Map<String, Object>) event.getEventData();
						eventData.put(k, assetInfo.get(v));
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
		return businessSystemHandler;
	}

}
