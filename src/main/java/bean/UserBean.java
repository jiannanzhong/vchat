package bean;

public class UserBean {
	private String uid;
	private String uuid;
	private Integer lastLogin;

	@Override
	public String toString() {
		return "UserBean [uid=" + uid + ", uuid=" + uuid + ", lastLogin=" + lastLogin + "]";
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Integer getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Integer lastLogin) {
		this.lastLogin = lastLogin;
	}

}
