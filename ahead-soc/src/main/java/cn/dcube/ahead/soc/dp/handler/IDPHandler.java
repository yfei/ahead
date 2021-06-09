package cn.dcube.ahead.soc.dp.handler;

import cn.dcube.ahead.commons.proto.transport.EventTransportEntity;

public interface IDPHandler {

	public void handle(EventTransportEntity event);

	public IDPHandler getNext();

}
