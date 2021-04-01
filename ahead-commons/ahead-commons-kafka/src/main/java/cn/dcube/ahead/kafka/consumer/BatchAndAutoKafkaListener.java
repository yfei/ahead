package cn.dcube.ahead.kafka.consumer;

import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
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
@ConditionalOnExpression("${spring.kafka.consumer.batch:true}==true && ${spring.kafka.consumer.enable-auto-commit:true}==true")
public class BatchAndAutoKafkaListener {

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@KafkaListener(id = "batchAndAutoListener", containerFactory = "kafkaListenerFactory", topics = {
			"#{'${spring.kafka.consumer.topics}'.split(',')}" })
	public void listenBatchAndAuto(List<ConsumerRecord<?, ?>> records) {
		try {
			if (records.size() > 0) {
				log.debug("batch receive {} records.", records.size());
			}
			// 将事件publish发布出来
			for (ConsumerRecord<?, ?> record : records) {
				eventPublisher.publishEvent(new KafkaEvent(record));
			}
		} catch (Exception e) {
			log.error("Kafka监听异常" + e.getMessage(), e);
		}
	}

}
