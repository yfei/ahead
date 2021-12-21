package cn.dcube.ahead.soc.dw;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import cn.dcube.ahead.commons.proto.transport.EventTransportEntity;
import cn.dcube.ahead.commons.proto.transport.EventTransportEntity.MessageType;
import cn.dcube.ahead.commons.proto.transport.EventTypeEnum;
import cn.dcube.ahead.elastic.service.ElasticService;
import cn.dcube.ahead.kafka.coder.ByteMessageParser;
import cn.dcube.ahead.kafka.event.KafkaEvent;
import cn.dcube.ahead.soc.dw.config.DWConfig;
import cn.dcube.ahead.soc.dw.model.EventBase4ES;
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
@ConditionalOnBean(DWConfig.class)
public class DWKafkaEventHandler implements IKafkaEventHandler {

	@Autowired
	private DWConfig config;

	@Autowired
	private ElasticService esService;

	public DWKafkaEventHandler() {
		log.info("DW的Event数据处理服务已开启!");
	}

	@Override
	public void handle(MessageType type, KafkaEvent event) {
		EventTransportEntity transportEvent = ByteMessageParser.deserializer(event, type, this.getEventType());
		// 将对象保存到ES
		EventBase4ES eb = EventBase4ES.trans(transportEvent);
		esService.getElasticTemplate().save(eb);
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
