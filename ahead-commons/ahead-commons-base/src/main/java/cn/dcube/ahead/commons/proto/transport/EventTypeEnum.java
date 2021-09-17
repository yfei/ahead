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
	EVENT("EVENT", "原始事件"), 
	CEPEVENT("CEPEVENT", "CEP事件"), 
	SNPEVENT("SNPEVENT", "CEP事件"), 
	WEPEVENT("WEPEVENT", "CEP事件"),
	CONFIG("CONFIG", "配置信息"),
	RULE("RULE", "规则"),
	HEART_BEAT("HEAR_BEAT", "心跳数据"),
	OTHER("OTHER", "其他");

	private String code;

	private String name;
}
