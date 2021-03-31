package cn.dcube.ahead.core.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import cn.dcube.ahead.core.dao.UserJDBCRepository;
import cn.dcube.ahead.core.dao.UserJpaRepository;
import cn.dcube.ahead.core.entity.UserEntity;
import cn.dcube.ahead.core.entity.UserJDBCEntity;
import cn.dcube.ahead.core.entity.UserMybatisEntity;
import cn.dcube.ahead.core.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@MapperScan("cn.dcube.ahead.core.mapper")
public class UserService {

	@Autowired
	private UserJDBCRepository userRepository;

	@Autowired
	private UserJpaRepository user2Repository;

	@Autowired
	private UserMapper userMapper;

	@Test
	public void test() {
		userRepository.deleteAll();
		log.info(">>>>>>>>>>>>>");
		UserJDBCEntity user = new UserJDBCEntity();
		user.setName("JDBC保存");
		userRepository.save(user);

		UserEntity user2 = new UserEntity();
		user2.setName("JPA保存");
		user2Repository.save(user2);
		Pageable pageable = PageRequest.of(0, 10);

		Page<UserEntity> users = user2Repository.findAll(pageable);
		
		users.forEach((item) -> {
			log.info(item.getName());
		});
		UserMybatisEntity user3 = new UserMybatisEntity();
		user3.setName("mybatis保存");
		userMapper.insert(user3);

	}

}
