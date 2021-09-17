package cn.dcube.ahead.module.kafka;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import cn.dcube.ahead.kafka.config.KafkaConfig;
import cn.dcube.ahead.kafka.config.KafkaTopicConfig;

@Component
@ConditionalOnProperty(prefix = "spring.kafka", name = { "bootstrap-servers" })
public class Kafka {

	@Autowired
	private KafkaConfig config;
	
	@Autowired
	private KafkaTopicConfig config2;


	// @PostConstruct
	public void test() {
		System.out.println(config2);
		System.out.println(config);
	}
}
