package cn.dcube.ahead.kafka.config;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

/**
 * Kafka配置项
 * 
 * @author：yangfei<br>
 * @date：2021年3月30日下午5:44:55
 * @since 1.0
 */
@EnableKafka
@Configuration
@ConfigurationProperties(prefix = "spring.kafka.topics")
// 存在spring.kafka.bootstrap-servers时才生效
@ConditionalOnProperty(prefix = "spring.kafka", name = { "bootstrap-servers" })
public class KafkaTopicConfig {


	// 消费者topic
	private String consumer;

	// 生产者topic
	private Map<String, String> producer;

	public String getConsumer() {
		return consumer;
	}

	public void setConsumer(String consumer) {
		this.consumer = consumer;
	}

	public Map<String, String> getProducer() {
		return producer;
	}

	public void setProducer(Map<String, String> producer) {
		this.producer = producer;
	}
}
