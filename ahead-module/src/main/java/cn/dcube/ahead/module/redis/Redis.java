package cn.dcube.ahead.module.redis;

import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.dcube.ahead.redis.service.RedisService;

@Component
public class Redis {
	
	@Autowired
	private RedisService redisService;
	
	@PostConstruct
	public void test() {
		Set<String> sets = redisService.zrangeByScore("geolite_city_ipv4", 0d, 16777471d, 0,1);
		System.out.println(sets.size());
	}

}
