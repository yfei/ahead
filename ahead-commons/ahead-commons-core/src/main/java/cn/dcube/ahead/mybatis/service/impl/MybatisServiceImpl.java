package cn.dcube.ahead.mybatis.service.impl;

import java.io.Serializable;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.dcube.ahead.core.entity.IEntity;
import cn.dcube.ahead.mybatis.entity.MybatisEntity;
import cn.dcube.ahead.mybatis.mapper.AheadBaseMapper;
import cn.dcube.ahead.mybatis.service.IMybatisService;

@Service
@MapperScan("cn.dcube.ahead.core")
public class MybatisServiceImpl implements IMybatisService {

	@Autowired
	private AheadBaseMapper<MybatisEntity> mapper;

	@Override
	public <T extends IEntity> int save(T entity) {
		return mapper.insert((MybatisEntity) entity);
	}

	@Override
	public <T extends IEntity> int delete(T entity) {
		return mapper.deleteById(((MybatisEntity) entity).getId());
	}

	@Override
	public <T extends IEntity> int delete(Serializable id) {
		return mapper.deleteById(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends IEntity> T get(Serializable id) {
		return (T) mapper.selectById(id);
	}

	@Override
	public <T extends IEntity> int update(T entity) {
		return mapper.updateById((MybatisEntity) entity);
	}

	@Override
	public AheadBaseMapper<MybatisEntity> getMapper() {
		return this.mapper;
	}

}
