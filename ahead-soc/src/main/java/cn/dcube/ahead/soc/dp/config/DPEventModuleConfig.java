package cn.dcube.ahead.soc.dp.config;

import java.util.Map;

import lombok.Data;

/**
 * DP的事件处理模块
 * @author yangfei
 *
 */
@Data
public class DPEventModuleConfig {

	// 是否启用
	private boolean enable;

	// 回填类型
	private String type = "redis";

	// redis中的key
	private String fieldKey;

	// 事件中key的拼接方式
	private String fieldKeyJoin = DPConfig.KEY_SPLIT;

	// redis索引
	private String redisIndex;

	// 如果为空时,是否回填
	private boolean fillIfNotNull = true;

	// 字段映射
	private Map<String, String> fieldMapping;
	
	// GEO回填使用,key包括src/dst/ip
	private Map<String, String> ipFields;
	

}
