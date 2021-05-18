package cn.dcube.ahead.soc.redis.sync.impl;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import cn.dcube.ahead.soc.redis.sync.IRedisSyncService;
import cn.dcube.ahead.soc.redis.sync.RedisSyncContext;

/**
 * 同步恶意IP到Redis
 * 
 * @author yangfei
 *
 */
@Service
@ConditionalOnBean(RedisSyncContext.class)
public class BadIpSyncServiceImpl implements IRedisSyncService {

	@Override
	public <T> void sync() {

	}

	@Override
	public String getType() {
		return RedisSyncContext.REDIS_BAD_IP;
	}

}
