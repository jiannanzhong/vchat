package core;

import java.util.UUID;

public class MyUUID {
	public static synchronized String createUUID() {
		return UUID.randomUUID().toString();
	}
}
