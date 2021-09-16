package cn.dcube.ahead.jpa.entity;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@MappedSuperclass
public class AuditJpaEntity extends OptimisticLockJPAEntity {

	@CreatedBy
	@Column(name = "create_user")
	private String createUser;

	@LastModifiedBy
	@Column(name = "last_modify_user")
	private String lastModifyUser;

	@CreatedDate
	@Column(name = "create_time")
	private Instant createTime;

	@LastModifiedDate
	@Column(name = "last_modify_time")
	private Instant lastModifyTime;

}
