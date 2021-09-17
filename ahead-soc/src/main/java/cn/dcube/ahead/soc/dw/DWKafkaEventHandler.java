package cn.dcube.ahead.soc.dw;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import cn.dcube.ahead.commons.proto.transport.EventTransportEntity.MessageType;
import cn.dcube.ahead.kafka.event.KafkaEvent;
import cn.dcube.ahead.soc.kafka.handler.IKafkaEventHandler;
import cn.dcube.ahead.soc.kafka.model.KafkaTopic;

@Service
@ConditionalOnExpression("${soc.dw.enable:false}==true") // 当配置为true时
public class DWKafkaEventHandler implements IKafkaEventHandler{

	@Override
	public void handle(MessageType type,KafkaEvent event) {
		// 写入ES
		
	}

	@Override
	public List<KafkaTopic> getTopic() {
		return null;
	}

}
