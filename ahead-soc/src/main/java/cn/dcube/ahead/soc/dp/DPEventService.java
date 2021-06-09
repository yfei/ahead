package cn.dcube.ahead.soc.dp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import cn.dcube.ahead.commons.proto.transport.EventTransportEntity;
import cn.dcube.ahead.kafka.event.KafkaEvent;
import cn.dcube.ahead.soc.dp.config.DPConfig;
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
@ConditionalOnExpression("${soc.dp.enable:false}==true")
@Slf4j
public class DPEventService implements IKafkaEventHandler {

	@Autowired
	private DPConfig config;

	@Autowired
	private DPContext dpContext;

	public DPEventService() {
		log.info("DP的Event数据处理服务已开启!");
	}

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
		return config.getEvent().getConsumer();
	}

}
