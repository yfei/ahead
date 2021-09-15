package cn.dcube.ahead.jpa.entity;

import javax.persistence.Version;

public abstract class OptimisticLockEntity extends IdentifierEntity {

	@Version
	private Long version;

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
}
