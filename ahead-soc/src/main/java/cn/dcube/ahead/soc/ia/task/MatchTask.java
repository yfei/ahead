package cn.dcube.ahead.soc.ia.task;

import java.util.Map;
import java.util.Map.Entry;

import cn.dcube.ahead.commons.proto.transport.EventTransportEntity;
import cn.dcube.ahead.soc.ia.config.IAEventModuleConfig;
import cn.dcube.ahead.soc.ia.context.IAContext;
import cn.dcube.goku.lucy.data.packaging.MonkeyPackageSrvImpl;
import cn.dcube.goku.net.protobuf.PB_Query.Event;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MatchTask implements Runnable {

	private EventTransportEntity event;

	private IAContext context;

	public MatchTask(EventTransportEntity event, IAContext context) {
		this.event = event;
		this.context = context;
	}

	@Override
	public void run() {
		// 处理数据
		Map<String, Object> eventData = (Map<String, Object>) event.getEventData();
		// 对于每个模块进行处理
		for (Entry<String, IAEventModuleConfig> moduleEntry : context.getConfig().getModules().entrySet()) {
			context.getMatchService().handle(moduleEntry.getKey(), moduleEntry.getValue(), event);
		}
		// 发送protobuf
		try {
			byte[] messages = this.event(event);
			context.getProducer().sendByteMessage("test", messages, null, null);
		} catch (Exception e) {
			log.error("发送消息{}到topic{}失败!", event.getEventId(), event.getEventName());
		}
	}

	private byte[] event(EventTransportEntity event) throws Exception {
		Map<String, Object> eventData = (Map<String, Object>) event.getEventData();
		// FIXME 这里转换为protobuf
		Event.Builder pbEvent = Event.newBuilder();
		pbEvent.setId(Long.valueOf(event.getEventId()));
		pbEvent.setA(Integer.valueOf(eventData.get("a").toString()));
		pbEvent.setAccount(null);
		pbEvent.setAccountBytes(null);
		MonkeyPackageSrvImpl packageService = new MonkeyPackageSrvImpl();
		byte[] bytes = packageService.doPackage(false, false, pbEvent.build());
		return bytes;
	}
}
