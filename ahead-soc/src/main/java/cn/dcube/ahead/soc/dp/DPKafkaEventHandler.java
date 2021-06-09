package cn.dcube.ahead.soc.dp;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import cn.dcube.ahead.commons.proto.transport.EventTransportEntity;
import cn.dcube.ahead.kafka.event.KafkaEvent;
import cn.dcube.ahead.soc.dp.context.DPContext;
import cn.dcube.ahead.soc.kafka.IKafkaEventHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * DP处理器
 * 
 * @author yangfei
 *
 */
@Service
@ConditionalOnExpression("${soc.dp.enable:false}==true") // 当配置为true时
@Slf4j
public class DPKafkaEventHandler implements IKafkaEventHandler {

	@Autowired
	private Environment env;

	@Autowired
	private DPContext dpContext;

	@Override
	public void handle(KafkaEvent event) {
		if (event.getValue() instanceof EventTransportEntity) {
			dpContext.handle((EventTransportEntity) event.getValue());
		} else {
			log.warn("{}主题的数据格式异常!", event.getTopic());
		}
	}

	@Override
	public List<String> getTopic() {
		return Arrays.asList(env.getProperty("soc.dp.consumer.topic").split(","));
	}

}
