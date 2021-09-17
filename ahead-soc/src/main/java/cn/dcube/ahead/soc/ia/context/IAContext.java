package cn.dcube.ahead.soc.ia.context;

import java.util.concurrent.ExecutorService;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import cn.dcube.ahead.commons.proto.transport.EventTransportEntity;
import cn.dcube.ahead.kafka.producer.KafkaProducer;
import cn.dcube.ahead.redis.service.RedisService;
import cn.dcube.ahead.soc.ia.config.IAConfig;
import cn.dcube.ahead.soc.ia.service.MatchService;
import cn.dcube.ahead.soc.ia.task.MatchTask;
import cn.dcube.ahead.utils.thread.ThreadPoolUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@ConditionalOnBean(IAConfig.class)
public class IAContext {

	private ExecutorService executorService;

	@Autowired
	private IAConfig config;

	@Autowired
	private KafkaProducer producer;

	@Autowired
	private RedisService redisService;

	@Autowired
	private MatchService matchService;

	@PostConstruct
	public void init() {
		executorService = ThreadPoolUtil.createFixedThreadPool(config.getMatchThread(), "dp-handler");
		log.info("初始化IA匹配线程池数量为" + config.getMatchThread());
	}

	public void handle(EventTransportEntity event) {
		executorService.submit(new MatchTask(event, this));
	}

	public ExecutorService getExecutorService() {
		return executorService;
	}

	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}

	public IAConfig getConfig() {
		return config;
	}

	public void setConfig(IAConfig config) {
		this.config = config;
	}

	public KafkaProducer getProducer() {
		return producer;
	}

	public void setProducer(KafkaProducer producer) {
		this.producer = producer;
	}

	public RedisService getRedisService() {
		return redisService;
	}

	public void setRedisService(RedisService redisService) {
		this.redisService = redisService;
	}

	public MatchService getMatchService() {
		return matchService;
	}

	public void setMatchService(MatchService matchService) {
		this.matchService = matchService;
	}
}
