package cn.dcube.ahead.soc.cep;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import cn.dcube.ahead.kafka.event.KafkaEvent;
import cn.dcube.ahead.soc.kafka.IKafkaEventHandler;

@Service
@ConditionalOnExpression("${soc.dp.enable:false}==true") // 当配置为true时
public class CEPKafkaEventHandler implements IKafkaEventHandler{

	@Override
	public void handle(KafkaEvent event) {
		// 处理CEP分析
		
	}

	@Override
	public List<String> getTopic() {
		// TODO Auto-generated method stub
		return null;
	}

}
