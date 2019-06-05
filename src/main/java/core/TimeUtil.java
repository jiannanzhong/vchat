package core;

import java.util.Calendar;

public class TimeUtil {
	public static int getTimeInSeconds() {
		return (int) (Calendar.getInstance().getTimeInMillis() / 1000);
	}

}
