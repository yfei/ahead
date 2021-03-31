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

package cn.dcube.ahead.core.service.impl;

import cn.dcube.ahead.commons.page.Pagination;
import cn.dcube.ahead.core.dao.AheadBaseRepository;
import cn.dcube.ahead.core.query.QueryCondition;
import cn.dcube.ahead.core.service.IJpaService;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public abstract class AbstractJpaServiceImpl implements IJpaService {
	
	private AheadBaseRepository repository;
	
	@Override
	public <T> Pagination<T> getByPageination(Class<T> clazz, QueryCondition<T> condition) {
		return null;
	}

}
