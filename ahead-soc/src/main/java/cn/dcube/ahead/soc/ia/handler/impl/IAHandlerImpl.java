package cn.dcube.ahead.soc.ia.handler.impl;

import org.springframework.stereotype.Service;

import cn.dcube.ahead.commons.proto.transport.EventTransportEntity;
import cn.dcube.ahead.soc.dp.handler.IDPHandler;

@Service
public class IAHandlerImpl implements IDPHandler {

	@Override
	public void handle(EventTransportEntity event) {
	}

	@Override
	public IDPHandler getNext() {
		// TODO Auto-generated method stub
		return null;
	}

}
