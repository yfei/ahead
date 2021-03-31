# Ahead 开发框架
## 概述
	Ahead开发框架基于SpringBoot,提供了快速搭建、开发后台业务代码的能力.框架支持微服务,建议使用nacos配置和管理微服务.当然,您也可以使用自行搭建微服务管理端,只要支持Ahead-gateway的网关协议即可。
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
