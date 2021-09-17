package cn.dcube.ahead.soc.dp.service.handler;

import cn.dcube.ahead.commons.proto.transport.EventTransportEntity;
import cn.dcube.ahead.soc.dp.config.DPEventModuleConfig;

public interface IRefillHandler {

	public String getCategory();

	public void handle(String moduleName, DPEventModuleConfig module, EventTransportEntity event);

}
