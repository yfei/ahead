package cn.dcube.ahead.soc.dp.config;

import java.util.List;
import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import cn.dcube.ahead.soc.kafka.model.KafkaTopic;
import lombok.Data;

/**
 * DP配置
 * 
 * @author yangfei
 *
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "soc.dp")
@ConditionalOnExpression("${soc.dp.enable:false}")
public class DPConfig {

	public static String KEY_SPLIT = "_";

	public static String FIELD_JOIN_SPLIT = "\\|";

	// 节点id
	private long id;

	// 是否启用
	private boolean enable;

	// 消费主题
	private List<KafkaTopic> consumerTopics;

	// 回填线程数
	private int refillThread;

	// DP事件回填模块
	private Map<String, DPEventModuleConfig> modules;

}
