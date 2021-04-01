package cn.dcube.ahead.kafka.listener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import cn.dcube.ahead.commons.proto.transport.EventTransportEntity;
import cn.dcube.ahead.kafka.event.KafkaEvent;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class KafkaEventListener implements ApplicationListener<KafkaEvent> {
	
	Map<String,AtomicLong> counterInfos = new HashMap<String,AtomicLong>();
	
	@Override
	public void onApplicationEvent(KafkaEvent event) {
		if(event.getValue() instanceof EventTransportEntity) {
			// 如果是protobuf
			
		} else {
			
		}
		// 处理kafkaEvent
		log.debug(event.getTopic() + ">>>>>>>" + event.getValue());
	}

}
