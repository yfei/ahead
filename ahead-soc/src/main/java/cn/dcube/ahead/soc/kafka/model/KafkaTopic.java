package cn.dcube.ahead.soc.kafka.model;

import cn.dcube.ahead.commons.proto.transport.EventTransportEntity.MessageType;
import lombok.Data;

@Data
public class KafkaTopic {

	// 主题名称
	private String topic;
	
	// 主题数据类型
	private MessageType messageType;
	// 主题key
	private String EventType;
}
