package cn.dcube.ahead.soc.ia.service.handler;

import cn.dcube.ahead.commons.proto.transport.EventTransportEntity;
import cn.dcube.ahead.soc.ia.config.IAEventModuleConfig;

public interface IMatchHandler {

	public String getCategory();

	public void handle(String moduleName, IAEventModuleConfig module, EventTransportEntity event);

}
