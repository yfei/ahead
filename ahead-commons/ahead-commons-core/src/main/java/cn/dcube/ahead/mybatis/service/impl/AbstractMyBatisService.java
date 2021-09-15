package cn.dcube.ahead.mybatis.service.impl;

import java.io.Serializable;

import cn.dcube.ahead.core.entity.IEntity;
import cn.dcube.ahead.mybatis.entity.MybatisEntity;
import cn.dcube.ahead.mybatis.mapper.AheadBaseMapper;
import cn.dcube.ahead.mybatis.service.IMybatisService;

/**
 * 封装mybatis通用service.暂时没有好的思路,暂停
 * 
 * @author yangfei
 *
 */
@Deprecated
public class AbstractMyBatisService implements IMybatisService {

	protected AheadBaseMapper<MybatisEntity> mapper;

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
