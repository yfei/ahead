package cn.dcube.ahead.soc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * SOC的全局的配置
 * 
 * @author yangfei
 *
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "soc")
public class SOCConfig {

	// 收到kafka消息后的
	private int handleThreads = Runtime.getRuntime().availableProcessors();

	private String handleThreadName = "soc-handle";

}
