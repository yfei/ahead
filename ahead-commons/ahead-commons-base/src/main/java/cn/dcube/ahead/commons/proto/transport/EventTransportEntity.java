package cn.dcube.ahead.commons.proto.transport;

import io.protostuff.Tag;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: wenlong
 * @CreateTime: 2020-07-20 17:24
 * @Description: 基础 kafka消息通讯实体 定义
 */
/**
 * @author yangfei
 *
 */
@Getter
@Setter
public class EventTransportEntity {

	public enum MessageType {
		PROTOBUF, TRANSPORT, STRING, STRING_JSON
	}

	/**
	 * 事件唯一标识
	 */
	@Tag(1)
	private String eventId;
	/**
	 * 事件名称
	 */
	@Tag(2)
	private String eventName;
	/**
	 * 事件值(简单值)
	 */
	@Tag(3)
	private String eventVal;
	/**
	 * 事件类型
	 */
	@Tag(4)
	private EventTypeEnum eventType;
	/**
	 * 事件数据(按需传参)
	 */
	@Tag(5)
	private Object eventData;

	/**
	 * @param eventId   事件ID
	 * @param eventName 事件名称
	 * @param eventType 事件类型
	 */
	public EventTransportEntity(String eventId, String eventName, EventTypeEnum eventType) {
		this(eventId, eventName, eventType, null);
	}

	/**
	 * @param eventId   事件ID
	 * @param eventName 事件名称
	 * @param eventVal  事件值(简单值)
	 * @param eventType 事件类型
	 */
	public EventTransportEntity(String eventId, String eventName, String eventVal, EventTypeEnum eventType) {
		this(eventId, eventName, eventVal, eventType, null);
	}

	/**
	 * @param eventId   事件ID
	 * @param eventName 事件名称
	 * @param eventType 事件类型
	 * @param eventData 事件数据(按需传参)
	 */
	public EventTransportEntity(String eventId, String eventName, EventTypeEnum eventType, Object eventData) {
		this(eventId, eventName, null, eventType, eventData);
	}

	/**
	 * @param eventId   事件ID
	 * @param eventName 事件名称
	 * @param eventVal  事件值(简单值)
	 * @param eventType 事件类型
	 * @param eventData 事件数据(按需传参)
	 */
	public EventTransportEntity(String eventId, String eventName, String eventVal, EventTypeEnum eventType,
			Object eventData) {
		this.eventId = eventId;
		this.eventName = eventName;
		this.eventVal = eventVal;
		this.eventType = eventType;
		this.eventData = eventData;
	}

}
