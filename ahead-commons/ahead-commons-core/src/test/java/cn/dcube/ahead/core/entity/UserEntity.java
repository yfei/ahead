package cn.dcube.ahead.core.entity;


import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;

import lombok.Data;

@Data
@Entity
@Table(name = "tb_user")
public class UserEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String remark;
	private Integer age;
	@CreatedDate
	private Instant createTime;
}
