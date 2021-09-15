package cn.dcube.ahead.mybatis.entity;

import com.baomidou.mybatisplus.annotation.Version;

public abstract class MybatisOptimisticLockEntity extends MyBatisIdentifierEntity {

	@Version
	private Long version;

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
}
