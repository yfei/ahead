package cn.dcube.ahead.soc.ia.handler.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.dcube.ahead.commons.proto.transport.EventTransportEntity;
import cn.dcube.ahead.redis.service.RedisService;
import cn.dcube.ahead.soc.dp.handler.impl.BusinessSystemRefillHandler;
import cn.dcube.ahead.soc.ia.config.IAConfig;
import cn.dcube.ahead.soc.ia.config.IAEventModuleConfig;
import cn.dcube.ahead.soc.ia.handler.IAHandler;
import cn.dcube.ahead.utils.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BadUrlHandler implements IAHandler {

	@Autowired
	private IAConfig config;

	@Autowired
	private RedisService redisService;

	@Autowired
	private BusinessSystemRefillHandler businessSystemHandler;

	private IAEventModuleConfig moduleConfig;

	public void init() {
		moduleConfig = config.getEvent().getModules().get("badIp");
	}

	@Override
	public void handle(EventTransportEntity event) {
		if (moduleConfig.isEnable()) {
			log.debug("处理event,恶意IP比对!");
			// 查找恶意IP是否存在
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
	public IAHandler getNext() {
		return null;
	}

}
