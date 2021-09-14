package cn.dcube.ahead.mybatis.entity;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import cn.dcube.ahead.core.entity.IEntity;
import lombok.Data;

/**
 * mybatis实体抽象类
 * 
 * @author yangfei
 *
 */
@Data
public abstract class MybatisEntity implements IEntity {
	@TableId(type = IdType.AUTO)
	private Long id;

	@CreatedDate
	private Instant createTime;
	
	@CreatedDate
	private Instant lastModifyTime; 
}
