package cn.dcube.ahead.soc.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.dcube.ahead.soc.redis.sync.RedisSyncContext;

/**
 * redis数据同步controller
 * 
 * @author yangfei
 *
 */
// TODO 添加swagger
@Controller
@ConditionalOnBean(RedisSyncContext.class)
public class RedisSyncController {

	@Autowired
	private RedisSyncContext syncContext;

	@RequestMapping("/redis/sync/{type}")
	public String index(@PathVariable String type) throws Exception {
		syncContext.sync(type);
		return "SUCCESS";
	}

}
