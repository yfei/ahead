package cn.dcube.ahead.jpa.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
public class AuditEntity extends OptimisticLockEntity {
	/**
	 * 创建用户
	 */
	@Column(name="create_user")
	private String createUser;

	/**
	 * 创建时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_time")
	private Date createTime;
	/**
	 * 最后修改用户
	 */
	@Column(name="last_modify_user")
	private String lastModifyUser;

	/**
	 * 最后修改时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="last_modify_time")
	private Date lastModifyTime;

	/**
	 * @return the createUser
	 */
	public String getCreateUser() {
		return createUser;
	}

	/**
	 * @param createUser the createUser to set
	 */
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the lastModifyUser
	 */
	public String getLastModifyUser() {
		return lastModifyUser;
	}

	/**
	 * @param lastModifyUser the lastModifyUser to set
	 */
	public void setLastModifyUser(String lastModifyUser) {
		this.lastModifyUser = lastModifyUser;
	}

	/**
	 * @return the lastModifyTime
	 */
	public Date getLastModifyTime() {
		return lastModifyTime;
	}

	/**
	 * @param lastModifyTime the lastModifyTime to set
	 */
	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

}
