package cn.dcube.ahead.soc.dp;

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
 * DP回填处理器
 * 
 * @author yangfei
 *
 */
@Service
@Slf4j
@ConditionalOnBean(DPConfig.class)
public class DPKafkaEventHandler implements IKafkaEventHandler {

	@Autowired
	private DPConfig config;

	@Autowired
	private DPContext dpContext;

	public DPKafkaEventHandler() {
		log.info("DP的Event数据处理服务已开启!");
	}

	@Override
	public void handle(MessageType type, KafkaEvent event) {
		EventTransportEntity transportEvent = ByteMessageParser.deserializer(event, type, EventTypeEnum.EVENT);
		dpContext.handle(transportEvent);
	}

	@Override
	public List<KafkaTopic> getTopic() {
		List<KafkaTopic> topics = new ArrayList<KafkaTopic>();
		config.getConsumerTopics().forEach(topic -> {
			if ("Event".equals(topic.getKey())) {
				topics.add(topic);
			}
		});
		return topics;
	}

}
