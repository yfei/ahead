package cn.dcube.ahead.soc.dp.receiver;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import cn.dcube.ahead.commons.proto.transport.EventTransportEntity;
import cn.dcube.ahead.commons.proto.transport.EventTransportEntity.MessageType;
import cn.dcube.ahead.commons.proto.transport.EventTypeEnum;
import cn.dcube.ahead.kafka.coder.ByteMessageParser;
import cn.dcube.ahead.kafka.event.KafkaEvent;
import cn.dcube.ahead.soc.dp.config.DPConfig;
import cn.dcube.ahead.soc.dp.context.DPContext;
import cn.dcube.ahead.soc.kafka.handler.IKafkaEventHandler;
import cn.dcube.ahead.soc.kafka.model.KafkaTopic;
import lombok.extern.slf4j.Slf4j;

/**
 * DP接收Event的Kafka handler.
 * 
 * @author yangfei
 *
 */
@Service
@Slf4j
@ConditionalOnBean(DPConfig.class)
public class DPEventHandler implements IKafkaEventHandler {

	private DPConfig config;

	@Autowired
	private DPContext dpContext;

	public DPEventHandler() {
		log.info("DP的Event数据处理服务已开启!");
	}

	@Override
	public void handle(MessageType messageType, KafkaEvent event) {
		EventTransportEntity transportEvent = ByteMessageParser.deserializer(event, messageType, this.getEventType());
		dpContext.handle(transportEvent);
	}

	@Override
	public List<KafkaTopic> getTopic() {
		List<KafkaTopic> topics = new ArrayList<KafkaTopic>();
		config.getConsumerTopics().forEach(topic -> {
			if (this.getEventType().equals(topic.getEventType())) {
				topics.add(topic);
			}
		});
		return topics;
	}

	@Override
	public String getEventType() {
		return EventTypeEnum.EVENT.getCode();
	}

}
