package cn.dcube.ahead.kafka.coder;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.JSON;
import com.google.protobuf.Descriptors;
import com.google.protobuf.MessageLite;

import cn.dcube.ahead.commons.proto.transport.EventTransportEntity;
import cn.dcube.ahead.commons.proto.transport.EventTransportEntity.MessageType;
import cn.dcube.ahead.commons.proto.transport.EventTypeEnum;
import cn.dcube.ahead.commons.proto.util.ProtoBufUtils;
import cn.dcube.ahead.kafka.event.KafkaEvent;
import cn.dcube.ahead.utils.util.StringUtils;
import cn.dcube.ahead.utils.util.ZipUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 字符串数组转换为EventTransportEntity
 * 
 * @author yangfei
 *
 */
@Slf4j
public class ByteMessageParser {

	/**
	 * byte数组反序列化为EventTransportEntity
	 * 
	 * @param type
	 * @param record
	 * @return
	 */
	public static EventTransportEntity deserializer(KafkaEvent record, MessageType type, EventTypeEnum eventType) {
		EventTransportEntity eventTransportEntity = null;
		if (type.compareTo(MessageType.TRANSPORT) == 0) {
			try {
				eventTransportEntity = ProtoBufUtils.deserializer(ZipUtils.unGZip((byte[]) record.getValue()),
						EventTransportEntity.class);
			} catch (Exception e) {
				log.error("kafka消息反序列化异常 topicName={},key={}", record.getTopic(), record.getKey());
			}
		} else if (type.compareTo(MessageType.PROTOBUF) == 0) {
			eventTransportEntity = new EventTransportEntity(StringUtils.getUUID(), record.getTopic(), eventType);
			Map<String, Object> objectMap = compatibleProcessingDeserializer(record.getTopic(),
					(byte[]) record.getValue());
			eventTransportEntity.setEventData(objectMap);
		} else if (type.compareTo(MessageType.STRING_JSON) == 0) {
			eventTransportEntity = new EventTransportEntity(StringUtils.getUUID(), record.getTopic(), eventType);
			try {
				Map<String, Object> objectMap = JSON.parseObject(new String((byte[]) record.getValue(), "UTF-8"));
				eventTransportEntity.setEventData(objectMap);
			} catch (UnsupportedEncodingException e) {
				log.error("kafka消息反序列化异常 topicName={},key={}", record.getTopic(), record.getKey());
			}
		} else if (type.compareTo(MessageType.STRING) == 0) {
			eventTransportEntity = new EventTransportEntity(StringUtils.getUUID(), record.getTopic(), eventType);
			try {
				eventTransportEntity.setEventData(new String((byte[]) record.getValue(), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				log.error("kafka消息反序列化异常 topicName={},key={}", record.getTopic(), record.getKey());
			}
		}
		return eventTransportEntity;
	}

	/**
	 * 兼容处理
	 * 
	 * @param topic
	 * @param protobufData
	 * @return
	 */
	private static Map<String, Object> compatibleProcessingDeserializer(String topic, byte[] protobufData) {
		Map<String, Object> resultMap = new ConcurrentHashMap<>();
		boolean UnPackageUtilIsPresent = isPresent("cn.dcube.goku.lucy.data.packaging.UnPackageUtil");
		boolean CB_PigsyMetaDataIsPresent = isPresent("cn.dcube.goku.lucy.data.entity.CB_PigsyMetaData");
		boolean PB_QUERYIsPresent = isPresent("cn.dcube.goku.net.protobuf.PB_Query");
		boolean CB_BodyIsPresent = isPresent("cn.dcube.goku.lucy.data.entity.CB_Body");
		if (UnPackageUtilIsPresent && CB_PigsyMetaDataIsPresent && PB_QUERYIsPresent && CB_BodyIsPresent) {
			try {
				Class<?> UnPackageUtilCls = Class.forName("cn.dcube.goku.lucy.data.packaging.UnPackageUtil");
				Class<?> CB_PigsyMetaDataCls = Class.forName("cn.dcube.goku.lucy.data.entity.CB_PigsyMetaData");
				Class<?> CB_BodyCls = Class.forName("cn.dcube.goku.lucy.data.entity.CB_Body");

				Method unPackage = UnPackageUtilCls.getDeclaredMethod("unPackage", boolean.class, boolean.class,
						byte[].class);
				Object CB_PigsyMetaDataObj = unPackage.invoke(UnPackageUtilCls.newInstance(), false, false,
						protobufData);
				Object CB_BodyObj = CB_PigsyMetaDataCls.getMethod("getBody").invoke(CB_PigsyMetaDataObj);
				Object PB_QUERY_EventObj = CB_BodyCls.getMethod("getMessage").invoke(CB_BodyObj);
				if ("cn.dcube.goku.net.protobuf.PB_Query$Event".equals(PB_QUERY_EventObj.getClass().getName())) {
					Map<Descriptors.FieldDescriptor, Object> allFields = (Map<Descriptors.FieldDescriptor, Object>) PB_QUERY_EventObj
							.getClass().getMethod("getAllFields").invoke(PB_QUERY_EventObj);
					for (Map.Entry<Descriptors.FieldDescriptor, Object> entry : allFields.entrySet()) {
						if (entry.getValue() instanceof List) {
							for (com.google.protobuf.MapEntry<String, Object> en : (List<com.google.protobuf.MapEntry>) entry
									.getValue()) {
								resultMap.put(en.getKey(), en.getValue());
							}
						} else {
							resultMap.put(entry.getKey().getName(), entry.getValue());
						}
					}
				}
			} catch (Exception e) {
				log.error("消费主题为 {} 的兼容消息时发生异常：", topic, e);
			}
		}
		return JSON.parseObject(JSON.toJSONString(resultMap), Map.class);
	}

	/**
	 * 序列化消息
	 * 
	 * @param topic
	 * @param type
	 * @param message
	 */
	public static byte[] serializer(String topic, MessageType type, Object message) {
		// 序列化成ProtoBuf数据结构
		byte[] protobufData = null;
		if (type.compareTo(MessageType.STRING) == 0 || type.compareTo(MessageType.STRING_JSON) == 0) {
			if (message instanceof String) {
				protobufData = ((String) message).getBytes();
			}
		} else {
			if (message instanceof EventTransportEntity) {
				if (type.compareTo(MessageType.TRANSPORT) == 0) {
					protobufData = ZipUtils.gZip(ProtoBufUtils.serializer(message));
				} else if (type.compareTo(MessageType.PROTOBUF) == 0) {
					protobufData = compatibleProcessingSerializer(topic,
							((EventTransportEntity) message).getEventData());
				}
			} else {
				log.warn("主题 {} 为无效主题，自动丢弃", topic);
			}
		}
		if (protobufData == null || protobufData.length == 0) {
			log.warn("主题为 {} 的消息体为空，自动丢弃", topic);
		}

		return protobufData;
	}

	/**
	 * 兼容处理
	 * 
	 * @param topic
	 * @param dataObj
	 * @return
	 */
	private static byte[] compatibleProcessingSerializer(String topic, Object dataObj) {
		// 序列化成ProtoBuf数据结构
		byte[] protobufData = new byte[0];

		if (!(dataObj instanceof Map)) {
			log.warn("消息兼容处理仅支持Map类型，请更换消息体的数据类型");
			return protobufData;
		}

		Map<String, Object> dataMap = (Map<String, Object>) dataObj;

		boolean PB_QUERYIsPresent = isPresent("cn.dcube.goku.net.protobuf.PB_Query");
		boolean MonkeyPackageSrvImplIsPresent = isPresent("cn.dcube.goku.lucy.data.packaging.MonkeyPackageSrvImpl");
		boolean CB_SandyHeaderIsPresent = isPresent("cn.dcube.goku.lucy.data.entity.CB_SandyHeader");
		if (PB_QUERYIsPresent && MonkeyPackageSrvImplIsPresent && CB_SandyHeaderIsPresent) {
			try {
				Class<?> PB_QUERY_Cls = Class.forName("cn.dcube.goku.net.protobuf.PB_Query");
				Class<?> MonkeyPackageSrvImpl_Cls = Class
						.forName("cn.dcube.goku.lucy.data.packaging.MonkeyPackageSrvImpl");
				Class<?> CB_SandyHeader_Cls = Class.forName("cn.dcube.goku.lucy.data.entity.CB_SandyHeader");

				Class<?> PB_QUERY_EventCls = null;
				for (Class<?> aClass : PB_QUERY_Cls.getClasses()) {
					if ("cn.dcube.goku.net.protobuf.PB_Query$Event".equals(aClass.getName())) {
						PB_QUERY_EventCls = aClass;
						break;
					}
				}

				/* 以下调用无参的、私有构造函数 */
				Constructor c0 = PB_QUERY_EventCls.getDeclaredConstructor();
				c0.setAccessible(true);
				Object builder = PB_QUERY_EventCls.getMethod("newBuilder").invoke(c0.newInstance());
				Class<?> builderClass = builder.getClass();
				Method[] methods = builderClass.getMethods();
				Set<String> notExtendsFields = Collections.synchronizedSet(new HashSet<>());
				for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
					String methodName = "set".concat(entry.getKey());
					for (Method method : methods) {
						if (methodName.equals(method.getName())) {
							method.invoke(builder, entry.getValue());
							notExtendsFields.add(entry.getKey());
							break;
						}
					}
				}

				Method putExtendsValmethod = builderClass.getMethod("putMappingfield", String.class, String.class);
				for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
					if (!notExtendsFields.contains(entry.getKey())) {
						putExtendsValmethod.invoke(builder, entry.getKey(), entry.getValue().toString());
					}
				}

				Object build = builderClass.getMethod("build").invoke(builder);

				Object header = CB_SandyHeader_Cls.getMethod("toBytes").invoke(CB_SandyHeader_Cls.newInstance());
				Method doPackage = MonkeyPackageSrvImpl_Cls.getMethod("doPackage", boolean.class, boolean.class,
						byte[].class, MessageLite.class);
				protobufData = (byte[]) doPackage.invoke(MonkeyPackageSrvImpl_Cls.newInstance(), false, false, header,
						(MessageLite) build);
			} catch (Exception e) {
				log.error("发送主题为 {} 的兼容消息时发生异常：", topic, e);
			}
		}

		return protobufData;
	}

	/**
	 * 判断由提供的类名(类的全限定名)标识的类是否存在并可以加载
	 * 
	 * @param name 要检查的类的名称
	 * @return
	 */
	public static boolean isPresent(String name) {
		try {
			Thread.currentThread().getContextClassLoader().loadClass(name);
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}
}