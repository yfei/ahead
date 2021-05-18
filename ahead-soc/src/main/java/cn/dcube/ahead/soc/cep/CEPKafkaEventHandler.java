package cn.dcube.ahead.soc.cep;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import cn.dcube.ahead.kafka.event.KafkaEvent;
import cn.dcube.ahead.soc.kafka.IKafkaEventHandler;

@Service
@ConditionalOnExpression("${soc.dp.enable:false}==true") // 当配置为true时
public class CEPKafkaEventHandler implements IKafkaEventHandler{

	@Override
	public void handle(KafkaEvent event) {
		// 处理kafka消息
		
	}

	@Override
	public String getTopic() {
		// TODO Auto-generated method stub
		return null;
	}

}
