package cn.dcube.ahead.soc.dp.task;

import java.util.Map;
import java.util.Map.Entry;

import cn.dcube.ahead.commons.proto.transport.EventTransportEntity;
import cn.dcube.ahead.soc.dp.config.DPEventModuleConfig;
import cn.dcube.ahead.soc.dp.context.DPContext;
import cn.dcube.goku.lucy.data.packaging.MonkeyPackageSrvImpl;
import cn.dcube.goku.net.protobuf.PB_Query.Event;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RefillTask implements Runnable {

	private EventTransportEntity event;

	private DPContext context;

	public RefillTask(EventTransportEntity event, DPContext context) {
		this.event = event;
		this.context = context;
	}

	@Override
	public void run() {
		// 处理数据
		Map<String, Object> eventData = (Map<String, Object>) event.getEventData();
		// 设值id
		event.setEventId(context.getIdGenerator().GetEvtBaseId(context.getConfig().getId()) + "");
		// FIXME 处理通用信息
		// 对于每个模块进行处理
		for (Entry<String, DPEventModuleConfig> moduleEntry : context.getConfig().getModules().entrySet()) {
			context.getRefillHandlerContext().handle(moduleEntry.getKey(), moduleEntry.getValue(), event);
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
