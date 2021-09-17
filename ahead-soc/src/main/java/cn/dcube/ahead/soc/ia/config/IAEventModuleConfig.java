package cn.dcube.ahead.soc.ia.config;

import java.util.Map;

import lombok.Data;

@Data
public class IAEventModuleConfig {

	private boolean enable;

	private String type = "redis";

	private String redisKey;

	private String redisKeyJoin = IAConfig.KEY_SPLIT;

	private String redisIndex;

	private boolean fillIfNotNull = true;

	private Map<String, String> fields;
	
	// GEO回填使用,key包括src/dst/ip
	private Map<String, String> ipFields;
	

}
