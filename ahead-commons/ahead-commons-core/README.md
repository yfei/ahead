## 框架介绍
该项目提供了基础的开发框架,支持JDBC/JPA-Hibernate/Mybatis Plus.

## 包结构
``` 
  ahead
	|-- core 框架抽象entity和servic、以及其他抽象
	|-- jpa JPA-Hibernate和JDBC支持
		|-- repository  抽象repository
	|-- udf 自定义查询,底层为JPA-hibernate
	|-- mybatis  mybatis支持
```

## 使用说明
该框架中封装了统一实体接口IEntity和统一的服务接口IService，项目中的实体和Service类需要实现统一接口。
#### 使用Spring-data-jdbc
```
1. 定义表结构
2. 定义实体对象(@Table和@Column注解使用spring.data包中),实体对象需继承AuditJDBCEntity
3. 定义Repository,继承AheadJDBCRepository(务必不能继承AheadJDBCRepository,否则会报错)
```
#### 使用Spring-data-jpa
```
1. 定义实体对象(@Table和@Column注解使用javax.persistence包中),实体对象需继承AuditJpaEntity
2. 定义Repository,继承AheadBaseRepository
3. 可在application-jpa.yml中配置JPA的相关配置
```

#### 使用mybatis-plus
```
1. 定义实体对象,实体对象需继承MybatisEntity
2. 定义Mapper,继承AheadBaseMapper
3. 可在application-mybatis.yml中配置mybatis的相关配置
```
示例:

```
		// 测试JPA
		UserJPAEntity user = new UserJPAEntity();
		user.setName("SPRING-DATA-JPA");
		user.setNickName("JPA");
		user.setAge(30);
		userRepo.save(user);
		// 测试JPA-UDF
		user.setName("UDF");
		user.setId(null);
		service.persist(user);
		// 测试jdbc
		UserJDBCEntity userjdbc = new UserJDBCEntity();
		userjdbc.setName("SPRING-DATA-JDBC");
		userjdbc.setNickName("JDBC");
		userjdbc.setAge(30);
		userjdbcRepo.save(userjdbc);
		// 测试mybatis
		UserEntity user2 = new UserEntity();
		user2.setName("mybatis");
		user2.setNickName("mybatis");
		user2.setAge(30);
		mapper.insert(user2);
```

### TODO
1. 多数据源配置