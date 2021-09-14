package cn.dcube.ahead.udf.dao.impl;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import cn.dcube.ahead.core.entity.IEntity;
import cn.dcube.ahead.udf.dao.IJpaDao;
import cn.dcube.ahead.udf.page.Pagination;
import cn.dcube.ahead.udf.query.QueryCondition;
import cn.dcube.ahead.udf.query.filter.IFilter;
import cn.dcube.ahead.udf.query.sort.ISort;

/**
 * 
 * 
 * 描述：
 * <p>
 * &nbsp;&nbsp;&nbsp;&nbsp;默认的JPA DAO实现类。
 * </p>
 * 创建日期：2016年11月16日 下午6:27:39<br>
 * @author：yangfei<br>
 * 
 * @since 1.0
 */
@Repository(value = "jpaDao")
public class JpaDaoImpl implements IJpaDao {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * the entity manager.
	 */
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * get the entity manager.
	 * 
	 * @return the entity manager.
	 */
	public EntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 * set the entity manager.
	 * 
	 * @param entityManager
	 *            the entity manager.
	 */
	public void setEntityManager(EntityManager entityManager) {
		logger.info("set entityManager to jpa dao.");
		this.entityManager = entityManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.cecgw.venus.core.dao.jpa.IJpaDao#persist(java.lang.Object)
	 */
	@Override
	public <T extends IEntity> void persist(T entity) {
		entityManager.persist(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.cecgw.venus.core.dao.jpa.IJpaDao#getById(java.lang.Class,
	 * java.io.Serializable)
	 */
	@Override
	public <T extends IEntity> T getById(Class<T> clazz, Serializable id) {
		return entityManager.find(clazz, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.cecgw.venus.core.dao.jpa.IJpaDao#getAll(java.lang.Class)
	 */
	@Override
	public <T extends IEntity> List<T> getAll(Class<T> clazz) {
		String className = clazz.getName();
		StringBuffer jpql = new StringBuffer("select o from ");
		jpql.append(className).append(" o ");
		Query query  = entityManager.createQuery(jpql.toString());
		return query.getResultList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.cecgw.venus.core.dao.jpa.IJpaDao#delete(java.lang.Object)
	 */
	@Override
	public <T extends IEntity> void delete(T entity) {
		// Removing a detached instance
		entityManager.remove(entityManager.merge(entity));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.cecgw.venus.core.dao.jpa.IJpaDao#get(java.lang.Class,
	 * cn.cecgw.venus.core.query.QueryCondition)
	 */
	@Override
	public <T extends IEntity> List<T> get(Class<T> clazz, QueryCondition<T> condition) {
		Query query = generateQuery(clazz, condition.getFilter(), condition.getSort(), condition.getPagination());
		return query.getResultList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.cecgw.venus.core.dao.jpa.IJpaDao#get(java.lang.Class,
	 * cn.cecgw.venus.core.query.filter.IFilter)
	 */
	@Override
	public <T extends IEntity> List<T> get(Class<T> clazz, IFilter filter) {
		Query query = generateQuery(clazz, filter, null, null);
		return query.getResultList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.cecgw.venus.core.dao.jpa.IJpaDao#get(java.lang.Class,
	 * cn.cecgw.venus.core.query.filter.IFilter,
	 * cn.cecgw.venus.core.query.sort.ISort)
	 */
	@Override
	public <T extends IEntity> List<T> get(Class<T> clazz, IFilter filter, ISort sort) {
		Query query = generateQuery(clazz, filter, sort, null);
		return query.getResultList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.cecgw.venus.core.dao.jpa.IJpaDao#getByPageination(java.lang.Class,
	 * cn.cecgw.venus.core.query.QueryCondition)
	 */
	@Override
	public <T extends IEntity> Pagination<T> getByPageination(Class<T> clazz, QueryCondition<T> condition) {
		Pagination<T> pagination = condition.getPagination();
		Long totalCount = this.getRecordSize(clazz, condition);
		pagination.setRecordsCount(totalCount);
		Query query = generateQuery(clazz, condition.getFilter(), condition.getSort(), pagination);
		pagination.setRecords(query.getResultList());
		return pagination;
	}

	
	/* (non-Javadoc)
	 * @see cn.cecgw.venus.core.dao.jpa.IJpaDao#getRecordSize(java.lang.Class, cn.cecgw.venus.core.query.QueryCondition)
	 */
	@Override
	public <T extends IEntity> Long getRecordSize(Class<T> clazz, QueryCondition<T> condition) {
		Query countQuery = generateCountQuery(clazz, condition.getFilter(), condition.getSort());
		Long totalCount = (Long) countQuery.getResultList().get(0);
		return totalCount;
	}
	/**
	 * generate query object of hibernate/JPA.
	 * 
	 * @param clazz
	 *            the entity class.
	 * @param filter
	 *            the filter
	 * @param sort
	 *            the sort
	 * @param pagination
	 *            the pagination
	 * @return the Query object
	 */
	private <T extends IEntity> Query generateQuery(Class<T> clazz, IFilter filter, ISort sort, Pagination pagination) {
		StringBuffer jpql = new StringBuffer("select o from ");
		jpql.append(clazz.getName()).append(" ").append(QueryCondition.DEFAULT_ALIAS).append(" ");
		jpql.append(" where 1=1 ");
		// set alias
		if (filter != null) {
			filter.setTableAlias(QueryCondition.DEFAULT_ALIAS);
			jpql.append(" and ").append(filter.getString());
		}
		if (sort != null) {
			sort.setAlias(QueryCondition.DEFAULT_ALIAS);
			jpql.append(" order by ").append(sort.getString());
		}

		Query query = entityManager.createQuery(jpql.toString());

		if (pagination != null) {
			query.setFirstResult(pagination.getStartRowPosition());
			query.setMaxResults(pagination.getRecordsPerPage());
		}
		if (filter != null) {
			int position = 1;
			for (Object value : filter.getValues()) {
				query.setParameter(position, value);
				position++;
			}
		}
		return query;
	}

	/**
	 * generate query object of hibernate/JPA.
	 * 
	 * @param clazz
	 *            the entity class.
	 * @param filter
	 *            the filter
	 * @param sort
	 *            the sort
	 * @param pagination
	 *            the pagination
	 * @return the Query object
	 */
	private <T extends IEntity> Query generateCountQuery(Class<T> clazz, IFilter filter, ISort sort) {
		StringBuffer jpql = new StringBuffer("select count(1) from ");
		jpql.append(clazz.getName()).append(" ").append(QueryCondition.DEFAULT_ALIAS).append(" ");
		jpql.append(" where 1=1 ");
		// set alias
		if (filter != null) {
			filter.setTableAlias(QueryCondition.DEFAULT_ALIAS);
			jpql.append(" and ").append(filter.getString());
		}
		if (sort != null) {
			sort.setAlias(QueryCondition.DEFAULT_ALIAS);
			jpql.append(" order by ").append(sort.getString());
		}

		Query query = entityManager.createQuery(jpql.toString());

		if (filter != null) {
			int position = 1;
			for (Object value : filter.getValues()) {
				query.setParameter(position, value);
				position++;
			}
		}
		return query;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.cecgw.venus.core.dao.jpa.IJpaDao#getByJpql(java.lang.String,
	 * java.lang.Object[])
	 */
	@Override
	public <T extends IEntity> List<T> getByJpql(String jpql, Object... objects) {
		Query query = this.entityManager.createQuery(jpql);
		if (objects != null) {
			for (int i = 0; i < objects.length; i++) {
				query.setParameter(i + 1, objects[i]);
			}
		}
		return query.getResultList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.cecgw.venus.core.dao.jpa.IJpaDao#getBySQL(java.lang.String,
	 * java.lang.Object[])
	 */
	public <T extends IEntity> List<T> getBySQL(String sql, Object... objects) {
		Query query = this.entityManager.createNativeQuery(sql);
		if (objects != null) {
			for (int i = 0; i < objects.length; i++) {
				query.setParameter(i + 1, objects[i]);
			}
		}
		return query.getResultList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.cecgw.venus.core.dao.jpa.IJpaDao#executeByJpql(java.lang.String,
	 * java.lang.Object[])
	 */
	public int executeByJpql(String jpql, Object... params) {
		Query query = this.entityManager.createQuery(jpql);
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				query.setParameter(i + 1, params[i]);
			}
		}
		return query.executeUpdate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.cecgw.venus.core.dao.jpa.IJpaDao#executeBySQL(java.lang.String,
	 * java.lang.Object[])
	 */
	public int executeBySQL(String sql, Object... params) {
		Query query = this.entityManager.createNativeQuery(sql);
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				query.setParameter(i + 1, params[i]);
			}
		}
		return query.executeUpdate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.cecgw.venus.core.dao.jpa.IJpaDao#update(java.lang.Object)
	 */
	@Override
	public <T extends IEntity> void update(T entity) {
		this.entityManager.merge(entity);
	}

	@Override
	public <T extends IEntity> void persistBatch(List<T> entities) {
		EntityManager entityManagerNew = entityManager.getEntityManagerFactory().createEntityManager();
		EntityTransaction transaction = entityManagerNew.getTransaction();
		transaction.begin();
		for (int i = 0; i < entities.size(); i++) {
			entityManagerNew.persist(entities.get(i));
		}
		entityManagerNew.flush();
		transaction.commit();
		
	}

}
