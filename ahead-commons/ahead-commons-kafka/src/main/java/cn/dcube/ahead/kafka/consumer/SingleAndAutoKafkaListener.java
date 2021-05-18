package cn.dcube.ahead.kafka.consumer;

import java.util.Optional;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import cn.dcube.ahead.kafka.config.KafkaConfig;
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
//当使用kafka时并且配置非批量、自动提交时生效
@ConditionalOnBean(KafkaConfig.class)
@ConditionalOnExpression("${spring.kafka.consumer.batch:true}==false && ${spring.kafka.consumer.enable-auto-commit:true}==true")
public class SingleAndAutoKafkaListener {

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@KafkaListener(id = "batchAndAutoListener", containerFactory = "kafkaListenerFactory", topics = {
			"#{'${spring.kafka.consumer.topics}'.split(',')}" })
	public void listenBatchAndAuto(ConsumerRecord<?, ?> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		try {
			Optional<?> message = Optional.ofNullable(record.value());
			if (message.isPresent()) {
				log.debug("single receive record of topic {}.", topic);
				// 将事件publish发布出来
				eventPublisher.publishEvent(new KafkaEvent(record));
			}
		} catch (Exception e) {
			log.error("Kafka监听异常" + e.getMessage(), e);
		}
	}

}
