package cn.dcube.ahead.core.dao;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 
 * Ahead Repository
 * 
 * @author yangfei
 *
 * @param <T>
 * @param <ID>
 */
@NoRepositoryBean
public interface AheadBaseRepository<T, ID> extends PagingAndSortingRepository<T, ID> {

}
