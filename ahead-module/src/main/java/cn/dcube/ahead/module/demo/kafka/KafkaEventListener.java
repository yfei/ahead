package cn.dcube.ahead.module.demo.kafka;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import cn.dcube.ahead.kafka.event.KafkaEvent;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class KafkaEventListener implements ApplicationListener<KafkaEvent> {
	
	@Override
	public void onApplicationEvent(KafkaEvent event) {
		// 处理kafkaEvent
		log.info(event.getTopic() + "<<<<<<<<<<<<<" + event.getValue());
	}

}
