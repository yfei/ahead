package cn.dcube.ahead.soc.ia.handler;

import cn.dcube.ahead.commons.proto.transport.EventTransportEntity;

public interface IAHandler {

	public void handle(EventTransportEntity event);

	public IAHandler getNext();

}
