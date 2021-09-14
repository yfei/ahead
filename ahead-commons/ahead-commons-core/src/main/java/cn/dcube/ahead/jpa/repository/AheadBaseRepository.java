package cn.dcube.ahead.jpa.repository;

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
