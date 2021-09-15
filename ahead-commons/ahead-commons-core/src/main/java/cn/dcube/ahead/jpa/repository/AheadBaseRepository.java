package cn.dcube.ahead.jpa.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import cn.dcube.ahead.core.entity.IEntity;

/**
 * 抽象接口
 * @author yangfei
 *
 * @param <T>
 * @param <ID>
 */
@NoRepositoryBean
public interface AheadBaseRepository<T extends IEntity, ID extends Serializable> extends JpaRepository<T, ID> {

}
