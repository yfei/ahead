package cn.dcube.ahead.soc.dp.handler.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.dcube.ahead.commons.proto.transport.EventTransportEntity;
import cn.dcube.ahead.commons.util.StringUtils;
import cn.dcube.ahead.soc.dp.handler.IRefillHandler;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EventIdRefillHandler implements IRefillHandler {

	@Autowired
	private AssetRefillHandler assetHandler;

	@Override
	public void handle(EventTransportEntity event) {
		// 回填eventId
		event.setEventId(StringUtils.getUUID());
		log.debug("处理event,回填id:{}", event.getEventId());
		if (this.getNext() != null) {
			this.getNext().handle(event);
		}
	}

	@Override
	public IRefillHandler getNext() {
		return assetHandler;
	}
}
