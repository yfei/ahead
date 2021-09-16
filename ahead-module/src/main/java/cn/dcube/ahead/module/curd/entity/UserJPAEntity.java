package cn.dcube.ahead.module.curd.entity;


import javax.persistence.Entity;

import cn.dcube.ahead.jpa.entity.AuditJpaEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper=false)
@Entity(name="tb_user_mybatis")
public class UserJPAEntity extends AuditJpaEntity {
	
	private String name;
	
	private int age;
	
	private String remark;
	
	private String nickName;

}
