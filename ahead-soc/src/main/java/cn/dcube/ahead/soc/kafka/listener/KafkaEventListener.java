package cn.dcube.ahead.soc.kafka.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.dcube.ahead.kafka.event.KafkaEvent;
import cn.dcube.ahead.kafka.listener.DefaultKafkaEventListener;
import cn.dcube.ahead.soc.kafka.handler.KafkaEventHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * kafka消息监听器
 * 
 * @author yangfei
 *
 */
@Service
@Slf4j
public class KafkaEventListener extends DefaultKafkaEventListener {

	@Autowired
	private KafkaEventHandlerContext context;

	@Override
	protected void handle(KafkaEvent event) {
		try {
			context.handle(event);
		} catch (Exception e) {
			log.error("", e);
		}
	}

	protected void statistics(KafkaEvent event) {
		// TODO Auto-generated method stub
		
	}

}
