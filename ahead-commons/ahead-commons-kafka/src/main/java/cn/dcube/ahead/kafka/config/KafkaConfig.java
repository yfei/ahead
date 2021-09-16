package cn.dcube.ahead.kafka.config;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
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
@ConfigurationProperties(prefix = "spring.kafka")
// 存在spring.kafka.bootstrap-servers时才生效
@ConditionalOnBean(KafkaProperties.class)
@ConditionalOnProperty(prefix = "spring.kafka", name = { "bootstrap-servers" })
public class KafkaConfig {

	@Autowired
	private KafkaProperties kafkaProperties;

	@Value("${spring.kafka.topics.consumer}")
	private List<KafkaTopic> consumerTopics;

	/**
	 * 普通主题监听器配置
	 *
	 * @return
	 */
	@Bean(name = "kafkaListenerFactory")
	@ConditionalOnMissingBean
	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, byte[]>> kafkaListenerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, byte[]> factory = kafkaListenerContainerFactory();
		factory.setConcurrency(kafkaProperties.getListener().getConcurrency());
		return factory;
	}

	@Bean
	public KafkaTemplate<String, byte[]> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}

	/**
	 * 并发KafkaListener
	 * 
	 * @return
	 */
	private ConcurrentKafkaListenerContainerFactory<String, byte[]> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, byte[]> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		return factory;
	}

	private ConsumerFactory<String, byte[]> consumerFactory() {
		return new DefaultKafkaConsumerFactory<>(consumerConfigs());
	}

	private Map<String, Object> consumerConfigs() {
		return kafkaProperties.buildConsumerProperties();
	}

	/**
	 * producerFactory
	 * 
	 * @return
	 */
	private ProducerFactory<String, byte[]> producerFactory() {
		DefaultKafkaProducerFactory<String, byte[]> producerFactory = new DefaultKafkaProducerFactory<>(
				producerConfigs());
		return producerFactory;
	}

	private Map<String, Object> producerConfigs() {
		return kafkaProperties.buildProducerProperties();
	}

}
