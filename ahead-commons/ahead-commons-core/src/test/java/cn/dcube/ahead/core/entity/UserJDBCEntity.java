package cn.dcube.ahead.core.entity;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Table(value = "tb_user")
public class UserJDBCEntity {
	@Id
	private Long id;
	private String name;
	private String remark;
	private Integer age;
	@CreatedDate
	private Instant createTime;
}
