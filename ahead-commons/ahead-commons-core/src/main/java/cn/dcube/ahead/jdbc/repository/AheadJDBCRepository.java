package cn.dcube.ahead.jdbc.repository;

import java.io.Serializable;

import org.springframework.boot.autoconfigure.data.ConditionalOnRepositoryType;
import org.springframework.boot.autoconfigure.data.RepositoryType;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.dcube.ahead.core.entity.IEntity;

/**
 * 抽象接口
 * 
 * @author yangfei
 *
 * @param <T>
 * @param <ID>
 */
@NoRepositoryBean
public interface AheadJDBCRepository<T extends IEntity, ID extends Serializable>
		extends PagingAndSortingRepository<T, ID> {

}
