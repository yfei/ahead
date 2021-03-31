package cn.dcube.ahead.module.demo;

import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.dcube.ahead.elastic.service.ElasticService;
import cn.dcube.ahead.kafka.producer.KafkaProducer;
import cn.dcube.ahead.redis.service.RedisService;

@Component
public class Demo {

	@Autowired
	private KafkaProducer kafkaProducer;

	@Autowired
	private ElasticService elasticService;

	@Autowired
	private RedisService redisService;

	@PostConstruct
	public void test() throws Exception {
		// testKafka();
		// testES();
		testRedis();
	}

	public void testKafka() {
		// 发送kafka消息,自定义的kafka listener里会对数据做处理
		kafkaProducer.sendStringMessage("ahead-test", "this is a test");
	}

	public void testES() {
		Set<String> indexes = elasticService.getIndices();
		indexes.forEach(item -> {
			System.out.println("es>>>>>>>>>>" + item);
		});
	}

	public void testRedis() {
		redisService.getKeys("*").forEach(item -> {
			System.out.println("redis>>>>>>>>>>" + item);
		});
	}

}
