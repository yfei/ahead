package cn.dcube.ahead.soc.ia.config;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class IAEventConfig {

	// 消费主题
	private List<String> consumer;

	// 生产主题
	private List<String> producer;

	private Map<String, IAEventModuleConfig> modules;

}
