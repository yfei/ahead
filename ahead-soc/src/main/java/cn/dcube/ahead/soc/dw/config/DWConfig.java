package cn.dcube.ahead.soc.dw.config;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import cn.dcube.ahead.soc.kafka.model.KafkaTopic;
import lombok.Data;

/**
 * DP的配置
 * 
 * @author yangfei
 *
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "soc.dw")
@ConditionalOnExpression("${soc.dw.enable:false}")
public class DWConfig {

	private long id;

	private boolean enable;

	// 消费主题
	private List<KafkaTopic> consumerTopics;

}
