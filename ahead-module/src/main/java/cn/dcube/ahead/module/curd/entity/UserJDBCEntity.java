package cn.dcube.ahead.module.curd.entity;


import org.springframework.data.relational.core.mapping.Table;

import cn.dcube.ahead.jdbc.entity.AuditJDBCEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper=false)
@Table("tb_user_mybatis")
public class UserJDBCEntity extends AuditJDBCEntity {
	
	private String name;
	
	private int age;
	
	private String remark;
	
	private String nickName;

}
