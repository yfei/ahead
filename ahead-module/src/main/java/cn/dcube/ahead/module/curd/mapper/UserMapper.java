package cn.dcube.ahead.module.curd.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import cn.dcube.ahead.module.curd.entity.UserEntity;
import cn.dcube.ahead.mybatis.mapper.AheadBaseMapper;

@Mapper
@Repository
public interface UserMapper extends AheadBaseMapper<UserEntity> {

}
