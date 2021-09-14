## 框架介绍
该项目提供了基础的开发框架,支持JDBC/JPA-Hibernate/Mybatis Plus.

## 包结构
``` 
  ahead
	|-- core 框架抽象entity和servic、以及其他抽象
	|-- jpa JPA-Hibernate和JDBC支持
		|-- repository  抽象repository
	|-- udf 自定义查询,底层为JPA-hibernate
	|-- mubatis  mybatis支持
```

## 使用说明
该框架中封装了统一实体接口IEntity和统一的服务接口IService，项目中的实体和Service类需要实现统一接口。

### TODO
1. 多数据源配置