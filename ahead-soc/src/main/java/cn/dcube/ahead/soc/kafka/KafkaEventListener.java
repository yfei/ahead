package cn.dcube.ahead.soc.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.dcube.ahead.kafka.event.KafkaEvent;
import cn.dcube.ahead.kafka.listener.DefaultKafkaEventListener;
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
	protected void handleProtobuf(KafkaEvent event) {
		try {
			context.handle(event);
		} catch (Exception e) {
			log.error("", e);
		}

	}

	@Override
	protected void handleString(KafkaEvent event) {
		log.warn("暂未实现");
	}

	@Override
	protected void handleSpecial(KafkaEvent event) {
		log.warn("暂未实现");
	}

}
