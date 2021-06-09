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
 * DP状态监控处理器
 * 
 * @author yangfei
 *
 */
@Service
// 当DP生效并且monitor生效时
@ConditionalOnExpression("${soc.dp.enable:false}==true && ${soc.dp.monitor.enable:false}==true")
@Slf4j
public class DPMonitorService implements IKafkaEventHandler {

	@Autowired
	private DPConfig config;

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
		// TODO 这里处理monitor
		return config.getEvent().getConsumer();
	}

}
