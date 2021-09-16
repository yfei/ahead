package cn.dcube.ahead.jdbc.entity;

import java.time.Instant;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AuditJDBCEntity extends IdentifierJDBCEntity {

	@CreatedBy
	@Column("create_user")
	private String createUser;

	@LastModifiedBy
	@Column("last_modify_user")
	private String lastModifyUser;

	@CreatedDate
	@Column("create_time")
	private Instant createTime;

	@LastModifiedDate
	@Column("last_modify_time")
	private Instant lastModifyTime;

}
