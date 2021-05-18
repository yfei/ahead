package cn.dcube.ahead.soc.kafka;

import cn.dcube.ahead.kafka.event.KafkaEvent;

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
	public String getTopic();

}
