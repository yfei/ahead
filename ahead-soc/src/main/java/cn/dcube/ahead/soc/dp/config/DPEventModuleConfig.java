package cn.dcube.ahead.soc.dp.config;

import java.util.Map;

import lombok.Data;

@Data
public class DPEventModuleConfig {

	private boolean enable;

	private String type = "redis";

	private String redisKey;

	private String redisKeyJoin = DPConfig.KEY_SPLIT;

	private String redisIndex;

	private boolean fillIfNotNull = true;

	private Map<String, String> fields;
	
	// GEO回填使用,key包括src/dst/ip
	private Map<String, String> ipFields;
	

}
