package cn.dcube.ahead.soc.kafka;

import org.springframework.stereotype.Component;

import cn.dcube.ahead.kafka.event.KafkaEvent;
import cn.dcube.ahead.kafka.listener.DefaultKafkaEventListener;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class KafkaEventListener extends DefaultKafkaEventListener {
	
	// 不同主题的消息对应不同的处理器
	// 比如
	@Override
	protected void handleProtobuf(KafkaEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handleString(KafkaEvent event) {
		if ("soc.eventbase.collector".equals(event.getTopic())) {
			
		} else {
			log.warn("{}主题的数据不应为字符串格式!", event.getTopic());
		}
	}

	@Override
	protected void handleSpecial(KafkaEvent event) {

	}

}
