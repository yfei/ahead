package cn.dcube.ahead.soc.ia.config;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

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
@ConfigurationProperties(prefix = "soc.ia")
@ConditionalOnExpression("${soc.ia.enable:false}")
public class IAConfig {

	public static String KEY_SPLIT = "_";

	public static String KEY_FIELD_JOIN_SPLIT = "\\|";

	private long id;

	private boolean enable;

	// 消费主题
	private List<KafkaTopic> consumerTopics;

	// 回填线程数
	private int matchThread;

	private Map<String, IAEventModuleConfig> modules;


}
