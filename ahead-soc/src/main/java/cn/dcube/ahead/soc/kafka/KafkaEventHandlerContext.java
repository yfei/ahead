package cn.dcube.ahead.soc.kafka;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.dcube.ahead.commons.proto.transport.EventTransportEntity.MessageType;
import cn.dcube.ahead.kafka.event.KafkaEvent;
import cn.dcube.ahead.soc.config.SOCConfig;
import cn.dcube.ahead.soc.kafka.handler.IKafkaEventHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
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

	// 每个topic可以配置多个handler,顺序处理.key为topic,value为handler处理器列表
	private Map<String, List<IKafkaEventHandler>> handlerCategory = new HashMap<String, List<IKafkaEventHandler>>();

	// 主题消息类型,key为topic
	private Map<String, MessageType> topicMessageTypes = new HashMap<String, MessageType>();

	@Autowired
	private List<IKafkaEventHandler> handlers;

	@Autowired
	private SOCConfig socConfig;

	private EventLoopGroup eventLoopGroup;

	@PostConstruct
	public void init() {
		eventLoopGroup = new NioEventLoopGroup(socConfig.getHandleThreads(),
				new DefaultThreadFactory(socConfig.getHandleThreadName()));
		if (handlers != null) {
			handlers.forEach(handler -> {
				if (handler.getTopic() != null) {
					handler.getTopic().forEach(topic -> {
						log.info("注册主题{}的KafkaEventHandler->{}", topic, handler.getClass().getName());
						List<IKafkaEventHandler> handlers = handlerCategory.get(topic.getTopic());
						if (handlers == null) {
							handlers = new ArrayList<IKafkaEventHandler>();
						}
						handlers.add(handler);
						handlerCategory.put(topic.getTopic(), handlers);
						topicMessageTypes.put(topic.getTopic(), topic.getMessageType());
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
				// 这里考虑使用线程池.避免多个节点订阅一个topic时,顺序处理
				handlers.forEach(handler -> eventLoopGroup.submit(new Runnable() {
					@Override
					public void run() {
						handler.handle(type, event);
					}
				}));
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
