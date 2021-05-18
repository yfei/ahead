### cn.dcube.ahead.soc.redis
#### 介绍
该包为处理redis数据同步的业务代码
#### 设计思路
```
1.使用策略模式,不同类型的数据使用不同的策略进行处理。
2.统一在RedisSyncContext中进行策略分发。
3.提供restful接口 /redis/sync/{type} 供其他方调用
```
##### 资产数据同步
```
redisKey为soc_asset
key为IP
value为json格式的字符串
```
#### 业务系统数据
```
redisKey为soc_busi_system
key为IP:PORT(如无port,直接使用ip)
value为json格式的业务系统字符串(会通过配置解析json的特定字符串回填)
```
#### 情报数据
```
redisKey为soc_bad_ip/url/email
key为恶意IP/恶意URL/恶意email
value为json格式的字符串
通过redis-ttl设值情报失效时间，不在业务代码中处理
```
#### 黑/白名单数据（仅支持IP）
```
redisKey为soc_ip_black/white_list
黑名单和白名单分为两个redis key
key为黑/白名单IP
value为json格式字符串
```
#### 网络位置
```
这部分正在研究如何用redis实现范围检索
```

### cn.dcube.ahead.soc.dp
### 介绍
该包下为dp处理逻辑
### 设计思路
```
1. 所有的数据回填统一使用redis
```

### 说明
1.ip如无特殊说明,ipv4转换为int无符号数,ipv6使用字符串