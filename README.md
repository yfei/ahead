# 开发框架
## 概述
	开发框架基于SpringBoot,提供了快速搭建、开发后台业务代码的能力.框架支持微服务,建议使用nacos配置和管理微服务.当然,您也可以使用自行搭建微服务管理端,只要支持Ahead-gateway的网关协议即可。
## 框架目录
``` 
  ahead
	|-- ahead-commons 通用开发框架
		|-- ahead-commons-utils 通用工具
		|-- ahead-commons-base 框架基础依赖
		|-- ahead-commons-core 核心框架
		|-- ahead-commons-web web框架
		|-- ahead-commons-redis redis 服务
		|-- ahead-commons-kafka kafka 服务
		|-- ahead-commons-elastic es 服务
	|-- ahead-gateway 网关服务
		|-- ahead-gateway-springcloud
		|-- ahead-gateway-gRpc
	|-- ahead-spark 大数据分析平台
```
## 框架使用
### 基础框架
支持Spring-data-jpa、Spring-data-jdbc和Spring-mybatis(plus).具体使用参考相关文档,这里面没做过多封装.
#### 依赖引入
```
<dependency>
	<groupId>cn.dcube</groupId>
	<artifactId>ahead-commons-core</artifactId>
	<version>${ahead.version}</version>
</dependency>
```
#### 使用
支持
### Kafka 支持 
#### 依赖引入
```
<dependency>
	<groupId>cn.dcube</groupId>
	<artifactId>ahead-commons-kafka</artifactId>
	<version>${ahead.version}</version>
</dependency>
```
#### 使用
##### 消费者-编写kafkaListener
kafka消费数据后,通过ApplicationEventPublisher发布出来,需要自己实现KafkaListener来实现数据处理逻辑.

```
@Component
@Slf4j
public class KafkaEventListener implements ApplicationListener<KafkaEvent> {
	
	@Override
	public void onApplicationEvent(KafkaEvent event) {
		// 处理kafkaEvent
		log.info(event.getTopic() + "<<<<<<<<<<<<<" + event.getValue());
	}

}
```
##### 生产者-KafkaProducer
```
	@Autowired
	private KafkaProducer kafkaProducer
	
	// 发送kafka消息,自定义的kafka listener里会对数据做处理
	kafkaProducer.sendStringMessage(${topic}, "this is a test");
	kafkaProducer.sendProtobufMessage(${topic}, protobufMsg);
```
#### kafka配置
参考commons-kafka项目中的kafka-config-template.yml. 具体可参考Spring-kafka.
### Elastic支持
#### 依赖引入
```
<dependency>
	<groupId>cn.dcube</groupId>
	<artifactId>ahead-commons-elastic</artifactId>
	<version>${ahead.version}</version>
</dependency>
```
#### elastic配置
参考commons-elastic项目中的elastic-config-template.yml.
#### 使用
```
	@Autowired
	private ElasticService elasticHelper;
	// 获取索引
	Set<String> indexes = elasticHelper.getIndices();
	indexes.forEach(item -> {
		System.out.println("es>>>>>>>>>>" + item);
	});
```
### Redis支持
#### 依赖引入
```
<dependency>
	<groupId>cn.dcube</groupId>
	<artifactId>ahead-commons-redis</artifactId>
	<version>${ahead.version}</version>
</dependency>
```
#### redis配置
参考commons-elastic项目中的redis-config-template.yml.
#### 使用
```
	@Autowired
	private RedisService redisService;
```

## ROADMAP(TODO)
1. 多数据源支持
2. 节点监控
3. 图数据库支持
4. 网关服务