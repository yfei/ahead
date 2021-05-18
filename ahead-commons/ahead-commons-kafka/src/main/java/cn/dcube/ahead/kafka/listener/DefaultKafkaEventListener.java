package cn.dcube.ahead.kafka.listener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.context.ApplicationListener;

import cn.dcube.ahead.commons.proto.transport.EventTransportEntity;
import cn.dcube.ahead.kafka.event.KafkaEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class DefaultKafkaEventListener implements ApplicationListener<KafkaEvent> {

	Map<String, AtomicLong> counterInfos = new HashMap<String, AtomicLong>();

	@Override
	public void onApplicationEvent(KafkaEvent event) {
		if (event.getValue() instanceof EventTransportEntity) {
			// 如果是protobuf
			this.handleProtobuf(event);
		} else if (event.getValue() instanceof String) {
			// 如果是字符串
			this.handleString(event);
		} else {
			log.warn("暂不支持的Kafka数据类型!");
		}
		// TODO 这里进行topic的统计
		this.handleSpecial(event);
	}

	/**
	 * 处理protobuf
	 * 
	 * @param event
	 */
	protected abstract void handleProtobuf(KafkaEvent event);

	/**
	 * 处理字符串
	 * 
	 * @param event
	 */
	protected abstract void handleString(KafkaEvent event);

	/**
	 * 特殊的自定义业务处理
	 * 
	 * @param event
	 */
	protected abstract void handleSpecial(KafkaEvent event);
}
