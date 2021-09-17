package cn.dcube.ahead.soc.cep;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import cn.dcube.ahead.commons.proto.transport.EventTransportEntity.MessageType;
import cn.dcube.ahead.kafka.event.KafkaEvent;
import cn.dcube.ahead.soc.kafka.handler.IKafkaEventHandler;
import cn.dcube.ahead.soc.kafka.model.KafkaTopic;

@Service
@ConditionalOnExpression("${soc.dp.enable:false}==true") // 当配置为true时
public class CEPKafkaEventHandler implements IKafkaEventHandler {

	@Override
	public void handle(MessageType type,KafkaEvent event) {
		// 处理CEP分析

	}

	@Override
	public List<KafkaTopic> getTopic() {
		return null;
	}

}
