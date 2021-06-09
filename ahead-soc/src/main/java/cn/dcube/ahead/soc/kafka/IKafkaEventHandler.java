package cn.dcube.ahead.soc.kafka;

import java.util.List;

import cn.dcube.ahead.kafka.event.KafkaEvent;

/**
 * kafka消息处理接口
 * 
 * @author yangfei
 *
 */
public interface IKafkaEventHandler {
	/**
	 * 数据处理
	 */
	public void handle(KafkaEvent event);

	/**
	 * 同步的数据类型
	 * 
	 * @return
	 */
	public List<String> getTopic();

}
