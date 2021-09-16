package cn.dcube.ahead.jpa.entity;

import org.springframework.data.annotation.Version;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public abstract class OptimisticLockJPAEntity extends IdentifierJPAEntity {

	@Version
	private Long version;

}
