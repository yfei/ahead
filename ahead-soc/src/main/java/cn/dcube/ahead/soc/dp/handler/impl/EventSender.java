package cn.dcube.ahead.soc.dp.handler.impl;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.dcube.ahead.commons.proto.transport.EventTransportEntity;
import cn.dcube.ahead.kafka.producer.KafkaProducer;
import cn.dcube.ahead.soc.dp.config.DPConfig;
import cn.dcube.ahead.soc.dp.handler.IRefillHandler;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EventSender implements IRefillHandler {

	@Autowired
	private DPConfig config;

	@Autowired
	private KafkaProducer producer;

	@PostConstruct
	public void init() {
	}

	@Override
	public void handle(EventTransportEntity event) {
		config.getEvent().getProducer().forEach(topic -> {
			log.debug("发送消息{}到主题{}", event.getEventId(), topic);
			producer.sendProtobufMessage(topic, event);
		});
		if (this.getNext() != null) {
			this.getNext().handle(event);
		}
	}

	@Override
	public IRefillHandler getNext() {
		return null;
	}

}
