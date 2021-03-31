package cn.dcube.ahead.core.entity;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName(value = "tb_user")
public class UserMybatisEntity {
	@TableId(type = IdType.AUTO)
	private Long id;
	private String name;
	private String remark;
	private Integer age;
	@CreatedDate
	private Instant createTime;
}
