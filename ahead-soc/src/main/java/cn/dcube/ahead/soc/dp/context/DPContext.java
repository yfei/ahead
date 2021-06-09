package cn.dcube.ahead.soc.dp.context;

import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import cn.dcube.ahead.commons.proto.transport.EventTransportEntity;
import cn.dcube.ahead.commons.thread.ThreadPoolUtil;
import cn.dcube.ahead.soc.dp.handler.impl.AssetHandler;
import lombok.extern.slf4j.Slf4j;

@Service
@ConditionalOnExpression("${soc.dp.enable:false}==true") // 当配置为true时
@Slf4j
public class DPContext {

	private ExecutorService executorService;

	@Autowired
	private AssetHandler assetHandler;

	public DPContext() {
		executorService = ThreadPoolUtil.createFixedThreadPool(4, "dp-handler");
		log.info("初始化DPContext,线程池数量为4");
	}

	public void handle(EventTransportEntity event) {
		executorService.submit(new Runnable() {
			@Override
			public void run() {
				log.debug("处理event事件,eventid为{}", event.getEventId());
				assetHandler.handle(event);
			}
		});
	}

}
