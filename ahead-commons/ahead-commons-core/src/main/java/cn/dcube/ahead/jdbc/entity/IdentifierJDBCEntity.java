
package cn.dcube.ahead.jdbc.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import cn.dcube.ahead.core.entity.IEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public abstract class IdentifierJDBCEntity implements IEntity {

	@Id
	@Column("id")
	private Long id;


}
