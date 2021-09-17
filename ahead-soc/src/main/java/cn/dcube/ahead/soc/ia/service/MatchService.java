package cn.dcube.ahead.soc.ia.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import cn.dcube.ahead.commons.proto.transport.EventTransportEntity;
import cn.dcube.ahead.soc.ia.config.IAConfig;
import cn.dcube.ahead.soc.ia.config.IAEventModuleConfig;
import cn.dcube.ahead.soc.ia.service.handler.IAMatchHandler;

@Service
@ConditionalOnBean(IAConfig.class)
public class MatchService {

	private Map<String, IAMatchHandler> handlerCategory = new HashMap<String, IAMatchHandler>();

	private List<IAMatchHandler> handlers;

	@PostConstruct
	public void init() {
		for (IAMatchHandler handler : handlers) {
			handlerCategory.put(handler.getCategory(), handler);
		}
	}

	public void handle(String moduleName, IAEventModuleConfig module, EventTransportEntity event) {
		if (handlerCategory.containsKey(moduleName)) {
			handlerCategory.get(moduleName).handle(moduleName, module, event);
		} else {
			// 默认使用Common
			handlerCategory.get("COMMON").handle(moduleName, module, event);
		}

	}

}
