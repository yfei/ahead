package cn.dcube.ahead.soc.dp.handler;

import cn.dcube.ahead.commons.proto.transport.EventTransportEntity;

public interface IRefillHandler {

	public void handle(EventTransportEntity event);

	public IRefillHandler getNext();

}
