package cn.dcube.ahead.module.curd.entity;

import javax.persistence.Entity;

import com.baomidou.mybatisplus.annotation.TableField;

import cn.dcube.ahead.jpa.entity.AuditEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper=false)
@Entity(name = "tb_user_mybatis")
public class UserJPAEntity extends AuditEntity {
	
	private String name;
	
	private int age;
	
	private String remark;
	
	private String nickName;

}
