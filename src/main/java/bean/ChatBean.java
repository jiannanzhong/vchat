package bean;

public class ChatBean {
	private String id1;
	private String id2;
	private String message;
	private Integer time;

	@Override
	public String toString() {
		return "ChatBean [id1=" + id1 + ", id2=" + id2 + ", message=" + message + ", time=" + time + "]";
	}

	public String getId1() {
		return id1;
	}

	public void setId1(String id1) {
		this.id1 = id1;
	}

	public String getId2() {
		return id2;
	}

	public void setId2(String id2) {
		this.id2 = id2;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

}
