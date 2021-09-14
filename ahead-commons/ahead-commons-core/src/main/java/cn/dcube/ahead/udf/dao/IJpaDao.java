package cn.dcube.ahead.udf.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import cn.dcube.ahead.core.entity.IEntity;
import cn.dcube.ahead.udf.page.Pagination;
import cn.dcube.ahead.udf.query.QueryCondition;
import cn.dcube.ahead.udf.query.filter.IFilter;
import cn.dcube.ahead.udf.query.sort.ISort;

/**
 * 
 * 
 * 描述：
 * <p>
 * &nbsp;&nbsp;&nbsp;&nbsp;JPA DAO通用接口。
 * </p>
 * 创建日期：2016年11月16日 下午6:22:17<br>
 * 
 * @author：yangfei<br>
 * 
 * @since 1.0
 */
public interface IJpaDao extends IDao {

	public EntityManager getEntityManager();

	/**
	 * persist the entity.
	 * 
	 * @param entity the entity
	 */
	public <T extends IEntity> void persist(T entity);

	/**
	 * persist the entities.
	 * 
	 * @param entities the entity List
	 */
	public <T extends IEntity> void persistBatch(List<T> entities);

	/**
	 * update the entity.
	 * 
	 * @param entity the entity
	 */
	public <T extends IEntity> void update(T entity);

	/**
	 * get entity by id.
	 * 
	 * @param clazz the entity class.
	 * @param id    the id.
	 * @return the entity.
	 */
	public <T extends IEntity> T getById(Class<T> clazz, Serializable id);

	/**
	 * get all entity.
	 * 
	 * @param clazz the entity class.
	 * @return all of the entity.
	 */
	public <T extends IEntity> List<T> getAll(Class<T> clazz);

	/**
	 * delete entity.
	 * 
	 * @param entity the entity.
	 */
	public <T extends IEntity> void delete(T entity);

	/**
	 * get entity by query conditions.
	 * 
	 * @param clazz     the entity class.
	 * @param condition the conditions.
	 * @return the list of entity.
	 */
	public <T extends IEntity> List<T> get(Class<T> clazz, QueryCondition<T> condition);

	/**
	 * get entity by filter.
	 * 
	 * @param clazz  the entity class.
	 * @param filter the filter.
	 * @return the list of entity.
	 */
	public <T extends IEntity> List<T> get(Class<T> clazz, IFilter filter);

	/**
	 * get entity by filter and then sort the entity.
	 * 
	 * @param clazz  the entity class.
	 * @param filter the filter.
	 * @param sort   the sort info.
	 * @return the list of entity after sorted.
	 */
	public <T extends IEntity> List<T> get(Class<T> clazz, IFilter filter, ISort sort);

	/**
	 * the pagination query.
	 * 
	 * @param clazz     the entity class.
	 * @param condition the condition
	 * @return the pagination object.
	 */
	public <T extends IEntity> Pagination<T> getByPageination(Class<T> clazz, QueryCondition<T> condition);

	/**
	 * the pagination query.
	 * 
	 * @param clazz     the entity class.
	 * @param condition the condition
	 * @return the pagination object.
	 */
	public <T extends IEntity> Long getRecordSize(Class<T> clazz, QueryCondition<T> condition);

	/**
	 * get list by jpql
	 * 
	 * @param jpql    the jpql
	 * @param objects the parameters
	 * @return the list of object T
	 */
	public <T extends IEntity> List<T> getByJpql(String jpql, Object... objects);

	/**
	 * get list by sql
	 * 
	 * @param SQL     the sql
	 * @param objects the parameters
	 * @return the list of object T
	 */
	public <T extends IEntity> List<T> getBySQL(String sql, Object... objects);

	/**
	 * execute up date by jpql.
	 * 
	 * @param jpql   the jpql
	 * @param params the parameters
	 * @return the code returned.
	 */
	public int executeByJpql(String jpql, Object... params);

	/**
	 * execute up date by sql.
	 * 
	 * @param sql    the sql
	 * @param params the parameters
	 * @return the code returned.
	 */
	public int executeBySQL(String sql, Object... params);
}