package cn.dcube.ahead.soc.ia.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * DP的配置
 * @author yangfei
 *
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "soc.dp")
public class IAConfig {

	private boolean enable;

	private IAEventConfig event;

}
