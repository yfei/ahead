package cn.dcube.ahead.soc.dp.handler.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.dcube.ahead.commons.proto.transport.EventTransportEntity;
import cn.dcube.ahead.commons.util.StringUtils;
import cn.dcube.ahead.soc.dp.handler.IDPHandler;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EventIdHandler implements IDPHandler {

	@Autowired
	private AssetHandler assetHandler;

	@Override
	public void handle(EventTransportEntity event) {
		// 回填eventId
		log.debug("处理event,回填id");
		event.setEventId(StringUtils.getUUID());
		if (this.getNext() != null) {
			this.getNext().handle(event);
		}
	}

	@Override
	public IDPHandler getNext() {
		return assetHandler;
	}
}
