package core;

import java.util.Calendar;

public class TimeUtil {
    public static int getTimeInSeconds() {
        return (int) (Calendar.getInstance().getTimeInMillis() / 1000);
    }

    public static long getTimeInMillions() {
        return Calendar.getInstance().getTimeInMillis();
    }

}
