/*
 *  Copyright (c) Mr.Yang 2012-2016 All Rights Reserved
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package cn.dcube.ahead.udf.service.impl;

import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.dcube.ahead.core.entity.IEntity;
import cn.dcube.ahead.udf.dao.IDao;
import cn.dcube.ahead.udf.dao.IJpaDao;
import cn.dcube.ahead.udf.page.Pagination;
import cn.dcube.ahead.udf.query.QueryCondition;
import cn.dcube.ahead.udf.query.filter.IFilter;
import cn.dcube.ahead.udf.query.sort.ISort;
import cn.dcube.ahead.udf.service.IUDFService;
import cn.dcube.ahead.utils.exception.ServiceException;

/**
 * <p>
 * &nbsp;&nbsp;&nbsp;&nbsp; JPAService实现类,为该框架默认Service实现类.
 * </p>
 * create on 2016/8/22 10:43
 *
 * @author yangfei.
 * @version 1.0
 * @since 1.0
 */
@Service(value = "baseService")
public class DefaultServiceImpl implements IUDFService {
	/**
	 * the logger
	 */
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	protected IJpaDao dao;

	public IJpaDao getDao() {
		return dao;
	}

	@Override
	public void setDao(IDao dao) {
		if (dao instanceof IJpaDao) {
			this.dao = (IJpaDao) dao;
		} else {
			logger.error("the dao is not instance of IJpaDao.This service only support IJpaDao.");
			throw new ServiceException("the dao is not instance of IJpaDao.This service only support IJpaDao.");
		}
	}

	@Transactional
	@Override
	public <T extends IEntity> void persist(T entity) {
		dao.persist(entity);
	}

	@Transactional
	@Override
	public <T extends IEntity> void persistBatch(List<T> entities) {
		//dao.persistBatch(entities);
		for(T entity:entities){
			dao.getEntityManager().merge(entity);
		}
		dao.getEntityManager().flush();
		dao.getEntityManager().clear();
	}

	@Transactional
	@Override
	public <T extends IEntity> void update(T entity) {
		dao.update(entity);
	}

	@Override
	public <T extends IEntity> T getById(Class<T> clazz, Serializable id) {
		return dao.getById(clazz, id);
	}

	@Override
	public <T extends IEntity> List<T> getAll(Class<T> clazz) {
		return dao.getAll(clazz);
	}

	@Transactional
	@Override
	public <T extends IEntity> void delete(T entity) {
		dao.delete(entity);
	}

	@Override
	public <T extends IEntity> List<T> get(Class<T> clazz, QueryCondition<T> condition) {
		return dao.get(clazz, condition);
	}

	@Override
	public <T extends IEntity> List<T> get(Class<T> clazz, IFilter filter) {
		return dao.get(clazz, filter);
	}

	@Override
	public <T extends IEntity> List<T> get(Class<T> clazz, IFilter filter, ISort sort) {
		return dao.get(clazz, filter, sort);
	}

	@Override
	public <T extends IEntity> Pagination<T> getByPageination(Class<T> clazz, QueryCondition<T> condition) {
		return dao.getByPageination(clazz, condition);
	}


}
