package cn.dcube.ahead.mybatis.mapper;

import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import cn.dcube.ahead.mybatis.entity.MybatisEntity;

@Repository
public interface AheadBaseMapper<T extends MybatisEntity> extends BaseMapper<T> {

}
