package cn.dcube.ahead.soc.kafka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	private Map<String, IKafkaEventHandler> handlerCategory = new HashMap<String, IKafkaEventHandler>();

	@Autowired
	private List<IKafkaEventHandler> handlers;

	@PostConstruct
	public void init() {
		if (handlers != null) {
			handlers.forEach(item -> {
				if (item.getTopic() != null) {
					item.getTopic().forEach(topic -> {
						if (handlerCategory.containsKey(topic)) {
							log.warn("{}类型的KafkaEventHandler已存在!覆盖!", topic);
						}
						log.info("注册{}类型的KafkaEventHandler", topic);
						handlerCategory.put(topic, item);

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
			handlerCategory.get(topic).handle(event);
		} else {
			log.error("不存在{}对应的RedisSync处理器!", topic);
			throw new Exception("不存在对应的KafkaEventHandler!");
		}

	}
}
