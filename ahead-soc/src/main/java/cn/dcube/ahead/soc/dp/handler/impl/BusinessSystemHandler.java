package cn.dcube.ahead.soc.dp.handler.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import cn.dcube.ahead.commons.proto.transport.EventTransportEntity;
import cn.dcube.ahead.commons.util.StringUtils;
import cn.dcube.ahead.redis.service.RedisService;
import cn.dcube.ahead.soc.dp.handler.IDPHandler;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BusinessSystemHandler implements IDPHandler {

	@Autowired
	private Environment env;

	@Autowired
	private RedisService redisService;

	// 资产在redis中的key
	private String asset_key = "soc_businessSystem";

	// 该handler是否生效
	private boolean enable = true;

	public BusinessSystemHandler() {
		asset_key = env.getProperty("soc.dp.event.business.redis-key");
		enable = env.getProperty("soc.dp.event.business.enable", Boolean.class);
		if (!enable) {
			log.warn("AssetHandler未启用!");
		}
	}

	@Override
	public void handle(EventTransportEntity event) {
		if (enable) {
			log.debug("处理event,回填业务系统!");
			// 回填源IP资产
			String srcAsset = redisService.getCacheMapValue(asset_key, event.getSrcIp());
			if (StringUtils.isNotEmpty(srcAsset)) {
			}
			String dstAsset = redisService.getCacheMapValue(asset_key, event.getDstIp());
			if (StringUtils.isNotEmpty(dstAsset)) {

			}
		}

		if (this.getNext() != null) {
			this.getNext().handle(event);
		}
	}

	@Override
	public IDPHandler getNext() {
		return null;
	}

}
