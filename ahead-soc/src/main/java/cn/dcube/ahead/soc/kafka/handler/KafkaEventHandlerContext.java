package cn.dcube.ahead.soc.kafka.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.dcube.ahead.commons.proto.transport.EventTransportEntity.MessageType;
import cn.dcube.ahead.kafka.event.KafkaEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * kafka消息处理上下文
 * 
 * @author yangfei
 *
 */
@Service
@Slf4j
public class KafkaEventHandlerContext {

	// 每个topic可以配置多个handler,顺序处理
	private Map<String, List<IKafkaEventHandler>> handlerCategory = new HashMap<String, List<IKafkaEventHandler>>();

	private Map<String, MessageType> topicMessageTypes = new HashMap<String, MessageType>();
	@Autowired
	private List<IKafkaEventHandler> handlers;

	@PostConstruct
	public void init() {
		if (handlers != null) {
			handlers.forEach(handler -> {
				if (handler.getTopic() != null) {
					handler.getTopic().forEach(topic -> {
						log.info("注册主题{}的KafkaEventHandler->{}", topic, handler.getClass().getName());
						List<IKafkaEventHandler> handlers = handlerCategory.get(topic);
						if (handlers == null) {
							handlers = new ArrayList<IKafkaEventHandler>();
						}
						handlers.add(handler);
						handlerCategory.put(topic.getTopic(), handlers);
						topicMessageTypes.put(topic.getTopic(), topic.getType());
					});
				}
			});
		}
	}

	public void handle(KafkaEvent event) throws Exception {
		if (event == null || event.getTopic() == null) {
			return;
		}
		String topic = event.getTopic();
		if (handlerCategory.containsKey(event.getTopic())) {
			List<IKafkaEventHandler> handlers = handlerCategory.get(topic);
			final MessageType type = topicMessageTypes.containsKey(topic) ? topicMessageTypes.get(topic)
					: MessageType.TRANSPORT;
			if (handlers != null && handlers.size() > 0) {
				handlers.forEach(handler -> handler.handle(type, event));
			} else {
				log.error("不存在{}对应的KafkaEventHandler处理器!", topic);
				throw new Exception("不存在对应的KafkaEventHandler处理器!");
			}
		} else {
			log.error("不存在{}对应的KafkaEventHandler处理器!", topic);
			throw new Exception("不存在对应的KafkaEventHandler处理器!");
		}

	}
}
