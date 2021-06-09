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
	/**
	 * 事件唯一标识
	 */
	@Tag(1)
	private String eventId;

	/**
	 * 事件分类--大类
	 */
	@Tag(2)
	private Integer clazz;

	/**
	 * 事件分类--子类
	 */
	@Tag(3)
	private Integer subClazz;

	/**
	 * 事件分类--小类
	 */
	@Tag(4)
	private Integer family;

	/**
	 * 客户id
	 */
	@Tag(5)
	private Integer customerId;

	/**
	 * 设备厂商
	 */
	@Tag(6)
	private Long sensorMask;

	/**
	 * 设备型号
	 */
	@Tag(7)
	private Long sensorModel;

	/**
	 * 设备IP
	 */
	@Tag(8)
	private String sensorIp;

	/**
	 * 五元组 srcip+srcport+dstip+dstport+protocol
	 */
	@Tag(9)
	private String srcIp;

	@Tag(10)
	private String dstIp;

	@Tag(11)
	private Integer srcPort;

	@Tag(12)
	private Integer dstPort;

	@Tag(13)
	private String protocol;

	/**
	 * 事件类型
	 */
	@Tag(14)
	private EventTypeEnum eventType;
	/**
	 * 事件数据(按需传参)
	 */
	@Tag(15)
	private Object eventData;

}
