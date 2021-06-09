package cn.dcube.ahead.soc.dp.handler.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.dcube.ahead.commons.proto.transport.EventTransportEntity;
import cn.dcube.ahead.soc.dp.handler.IRefillHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 试件分类回填
 * 
 * @author yangfei
 *
 */
@Service
@Slf4j
public class EventTypeRefillHandler implements IRefillHandler {

	@Autowired
	private AssetRefillHandler assetHandler;

	@Override
	public void handle(EventTransportEntity event) {
		// TODO 处理业务
		if (this.getNext() != null) {
			this.getNext().handle(event);
		}
	}

	@Override
	public IRefillHandler getNext() {
		return null;
	}
}
