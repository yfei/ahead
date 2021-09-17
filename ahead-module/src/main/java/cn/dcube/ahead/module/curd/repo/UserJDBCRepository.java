package cn.dcube.ahead.module.curd.repo;

import org.springframework.stereotype.Repository;

import cn.dcube.ahead.jdbc.repository.AheadJDBCRepository;
import cn.dcube.ahead.module.curd.entity.UserJDBCEntity;

@Repository
public interface UserJDBCRepository extends AheadJDBCRepository<UserJDBCEntity,Long> {

}
