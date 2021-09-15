package cn.dcube.ahead.mybatis.service;

import cn.dcube.ahead.core.service.IService;
import cn.dcube.ahead.mybatis.entity.MybatisEntity;
import cn.dcube.ahead.mybatis.mapper.AheadBaseMapper;

@Deprecated
public interface IMybatisService extends IService {
	/**
	 * 得到mapper引用
	 * 
	 * @return
	 */
	public AheadBaseMapper<MybatisEntity> getMapper();

}
