package cn.dcube.ahead.module.curd.repo;

import org.springframework.stereotype.Repository;

import cn.dcube.ahead.jpa.repository.AheadBaseRepository;
import cn.dcube.ahead.module.curd.entity.UserJPAEntity;

@Repository
public interface UserRepository extends AheadBaseRepository<UserJPAEntity,Long> {

}
