package cn.dcube.ahead.soc.redis.sync;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * redis数据同步.采用策略模式
 * 
 * @author yangfei
 *
 */
@Service
@ConditionalOnExpression("${soc.redis-sync.enable:true}==true") // 当配置为true时
@Slf4j
public class RedisSyncContext {

	// ORG信息
	public static final String REDIS_ORG = "ORG";
	// 资产信息
	public static final String REDIS_ASSET = "ASSET";
	// 业务系统信息
	public static final String REDIS_SYSTEM = "SYSTEM";
	// 恶意IP
	public static final String REDIS_BAD_IP = "BADIP";
	// 恶意邮箱
	public static final String REDIS_BAD_EMAIL = "BADEMAIL";
	// 恶意代码
	public static final String REDIS_BAD_CODE = "BADCODE";
	// 恶意网址
	public static final String REDIS_BAD_URL = "BADURL";

	private Map<String, IRedisSyncService> syncCategory = new HashMap<String, IRedisSyncService>();

	@Autowired
	private List<IRedisSyncService> syncServices;

	@PostConstruct
	public void init() {
		if (syncServices != null) {
			syncServices.forEach(item -> {
				if (syncCategory.containsKey(item.getType())) {
					log.warn("{}类型的RedisSyncService已存在!覆盖!", item.getType());
				}
				log.info("注册{}类型的RedisSyncService", item.getType());
				syncCategory.put(item.getType(), item);
			});
		}
	}

	public void sync(String type) throws Exception {
		if (syncCategory.containsKey(type)) {
			log.info("{}类型的数据执行Redis数据同步", type);
			syncCategory.get(type).sync();
		} else {
			log.error("不存在{}对应的RedisSync处理器!", type);
			throw new Exception("不存在对应的RedisSync处理器!");
		}

	}

}
