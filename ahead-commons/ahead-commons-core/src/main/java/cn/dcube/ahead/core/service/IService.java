package cn.dcube.ahead.core.service;

import java.io.Serializable;

import cn.dcube.ahead.core.entity.IEntity;

/**
 * 抽象service
 * 
 * @author yangfei
 *
 */
public interface IService {
	/**
	 * 保存对象
	 * 
	 * @param <T>
	 * @param entity
	 */
	public <T extends IEntity> int save(T entity);

	/**
	 * 删除对象
	 * 
	 * @param <T>
	 * @param entity
	 */
	public <T extends IEntity> int delete(T entity);

	/**
	 * 根据ID删除对象
	 * 
	 * @param <T>
	 * @param id
	 */
	public <T extends IEntity> int delete(Serializable id);

	/**
	 * 获取对象
	 * 
	 * @param <T>
	 * @param id
	 */
	public <T extends IEntity> T get(Serializable id);

	/**
	 * 更新对象
	 * 
	 * @param <T>
	 * @param entity
	 */
	public <T extends IEntity> int update(T entity);

}
