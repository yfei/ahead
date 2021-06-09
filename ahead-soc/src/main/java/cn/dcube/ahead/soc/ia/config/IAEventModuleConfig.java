package cn.dcube.ahead.soc.ia.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

import lombok.Data;

@Data
public class IAEventModuleConfig {
	
	private boolean enable;
	
	private String type;
	
	private String redisKey;
	
	private Map<String,String> fields;

}
