package cn.dcube.ahead.soc.dp;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import cn.dcube.ahead.kafka.event.KafkaEvent;
import cn.dcube.ahead.soc.kafka.IKafkaEventHandler;

/**
 * DP处理器
 * 
 * @author yangfei
 *
 */
@Service
@ConditionalOnExpression("${soc.dp.enable:false}==true") // 当配置为true时
public class DPKafkaEventHandler implements IKafkaEventHandler {

	@Override
	public void handle(KafkaEvent event) {
		// 处理kafka消息,这里采用责任链模式,
		// 1. 回填资产
		// 2. 回填业务系统
		// 3. 

	}

	@Override
	public String getTopic() {
		return null;
	}

}
