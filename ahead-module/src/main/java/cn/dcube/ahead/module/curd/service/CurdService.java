package cn.dcube.ahead.module.curd.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import cn.dcube.ahead.module.curd.entity.UserEntity;
import cn.dcube.ahead.module.curd.entity.UserJPAEntity;
import cn.dcube.ahead.module.curd.mapper.UserMapper;
import cn.dcube.ahead.module.curd.repo.UserRepository;
import cn.dcube.ahead.udf.service.impl.UDFServiceImpl;

@Service
public class CurdService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private UserMapper mapper;

	@Autowired
	UDFServiceImpl service;

	@PostConstruct
	public void test() throws Exception {
		// 测试JPS
		UserJPAEntity user = new UserJPAEntity();
		user.setName("jpa");
		user.setNickName("手动阀发送");
		user.setAge(30);
		userRepo.save(user);
		// 测试JPA-UDF
		user.setName("UDF");
		user.setId(null);
		service.persist(user);
		// 测试mybatis
		UserEntity user2 = new UserEntity();
		user2.setName("mybatis");
		user2.setNickName("手动阀发送");
		user2.setAge(30);
		mapper.insert(user2);
	}

}