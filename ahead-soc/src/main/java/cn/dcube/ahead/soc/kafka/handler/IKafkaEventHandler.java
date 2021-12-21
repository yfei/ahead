package cn.dcube.ahead.soc.kafka.handler;

import java.util.List;

import cn.dcube.ahead.commons.proto.transport.EventTransportEntity.MessageType;
import cn.dcube.ahead.kafka.event.KafkaEvent;
import cn.dcube.ahead.soc.kafka.model.KafkaTopic;

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
	public void handle(MessageType messageType,KafkaEvent event);

	/**
	 * 同步的数据类型
	 * 
	 * @return
	 */
	public List<KafkaTopic> getTopic();
	
	/**
	 * 事件的业务类型
	 * @return
	 */
	public String getEventType();

}
