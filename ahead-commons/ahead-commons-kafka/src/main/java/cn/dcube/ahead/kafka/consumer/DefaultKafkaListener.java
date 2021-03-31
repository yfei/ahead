package cn.dcube.ahead.kafka.consumer;

import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.lang.Nullable;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import cn.dcube.ahead.kafka.event.KafkaEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * 监听Kafka主题消息,收到的Kafka消息,通过ApplicationEventPublisher发布出来
 * 
 * @author：yangfei<br>
 * @date：2021年3月24日上午10:45:31
 * @since 1.0
 */
@Slf4j
@Component
public class DefaultKafkaListener {

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@Autowired
	private KafkaProperties kafkaProperties;

	@KafkaListener(id = "default", containerFactory = "kafkaListenerFactory", topics = {
	        "#{'${spring.kafka.consumer.topics}'.split(',')}" })
	public void listen(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
		try {
			if (records.size() > 0) {
				log.debug("receive {} records from topic {} ", records.size(), records.get(0).topic());
			}
			// 将事件publish发布出来
			for (ConsumerRecord<?, ?> record : records) {
				eventPublisher.publishEvent(new KafkaEvent(record));
			}
		} catch (Exception e) {
			log.error("Kafka监听异常" + e.getMessage(), e);
		} finally {
			// 手动提交偏移量
			if (!kafkaProperties.getConsumer().getEnableAutoCommit()) {
				ack.acknowledge();
			}
		}
	}

}
