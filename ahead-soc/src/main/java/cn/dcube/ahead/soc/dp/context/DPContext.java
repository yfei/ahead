package cn.dcube.ahead.soc.dp.context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import cn.dcube.ahead.commons.proto.transport.EventTransportEntity;
import cn.dcube.ahead.kafka.producer.KafkaProducer;
import cn.dcube.ahead.redis.service.RedisService;
import cn.dcube.ahead.soc.dp.config.DPConfig;
import cn.dcube.ahead.soc.dp.service.RefillService;
import cn.dcube.ahead.soc.dp.task.RefillTask;
import cn.dcube.ahead.soc.util.IDGenerator;
import cn.dcube.ahead.utils.thread.ThreadPoolUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@ConditionalOnBean(DPConfig.class)
public class DPContext {

	private ExecutorService executorService;

	@Autowired
	private DPConfig config;

	@Autowired
	private KafkaProducer producer;

	@Autowired
	private RedisService redisService;

	@Autowired
	private RefillService refillHandlerContext;
	
	@Autowired
	private IDGenerator idGenerator;


	@PostConstruct
	public void init() {
		executorService = ThreadPoolUtil.createFixedThreadPool(config.getRefillThread(), "dp-handler");
		log.info("初始化DPContext,线程池数量为" + config.getRefillThread());
	}

	public void handle(EventTransportEntity event) {
		executorService.submit(new RefillTask(event, this));
	}

	public DPConfig getConfig() {
		return config;
	}

	public void setConfig(DPConfig config) {
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

	public RefillService getRefillHandlerContext() {
		return refillHandlerContext;
	}

	public void setRefillHandlerContext(RefillService refillHandlerContext) {
		this.refillHandlerContext = refillHandlerContext;
	}

	public IDGenerator getIdGenerator() {
		return idGenerator;
	}

	public void setIdGenerator(IDGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}
}
