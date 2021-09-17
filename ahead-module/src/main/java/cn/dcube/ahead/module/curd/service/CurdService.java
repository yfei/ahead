package cn.dcube.ahead.module.curd.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import cn.dcube.ahead.module.curd.entity.UserEntity;
import cn.dcube.ahead.module.curd.entity.UserJDBCEntity;
import cn.dcube.ahead.module.curd.entity.UserJPAEntity;
import cn.dcube.ahead.module.curd.mapper.UserMapper;
import cn.dcube.ahead.module.curd.repo.UserJDBCRepository;
import cn.dcube.ahead.module.curd.repo.UserJPARepository;
import cn.dcube.ahead.udf.service.impl.UDFServiceImpl;

@Service
public class CurdService {

	@Autowired
	private UserJPARepository userRepo;

	@Autowired
	private UserJDBCRepository userjdbcRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private UserMapper mapper;

	@Autowired
	UDFServiceImpl service;

	// @PostConstruct
	public void test() throws Exception {
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
	}

}