package cn.dcube.ahead.commons.proto.transport;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wenlong
 * @description 事件类型
 * @date 2020/8/13 15:16
 */
@Getter
@AllArgsConstructor
public enum EventTypeEnum {
	/**
	 * 原始数据采集类型
	 */
	EXTRACT_DATA("EVENT", "数据采集"),

	/**
	 * 配置信息
	 */
	SCHEDULED_TASK("CONFIG", "配置信息"),
	/**
	 * 心跳数据类型
	 */
	HEART_BEAT("HEAR_BEAT", "心跳数据"),
	/**
	 * 其他类型
	 */
	OTHER("OTHER", "其他");

	private String code;

	private String name;
}
