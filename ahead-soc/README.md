### cn.dcube.ahead.soc.redis
#### 介绍
该包为处理redis数据同步的业务代码
#### 设计思路
```
1.使用策略模式,不同类型的数据使用不同的策略进行处理。
2.统一在RedisSyncContext中进行策略分发。
3.提供restful接口 /redis/sync/{type} 供其他方调用
```
### cn.dcube.ahead.soc.dp
### 介绍
该包下为dp处理逻辑
### 设计思路
```
1. 所有的数据回填统一使用redis
```