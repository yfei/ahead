package cn.dcube.ahead.core.service;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

import cn.dcube.ahead.commons.page.Pagination;
import cn.dcube.ahead.core.query.QueryCondition;
import cn.dcube.ahead.core.query.filter.IFilter;
import cn.dcube.ahead.core.query.sort.ISort;

/**
 * jpa service接口
 * 
 * @author：yangfei<br>
 * @date：2021年3月24日上午11:25:33
 * @since 1.0
 */
public interface IJpaService extends IService {

	public <T> Pagination<T> getByPageination(Class<T> clazz, QueryCondition<T> condition);

}
