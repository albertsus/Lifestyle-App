package cs6018.lifestyleapp.utils;

import java.util.Calendar;

/**
 * Created by suchaofan on 10/26/18.
 */

public class DateUtils {
    /**
     * @return milliseconds since 1.1.1970 for today 0:00:00 local timezone
     */
    public static long getToday() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis();
    }

    /**
     * @return milliseconds since 1.1.1970 for tomorrow 0:00:01 local timezone
     */
    public static long getTomorrow() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 1);
        c.set(Calendar.MILLISECOND, 0);
        c.add(Calendar.DATE, 1);
        return c.getTimeInMillis();
    }

    /**
     * @return any date of the week
     */
    public static long getNday(int n) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.MILLISECOND, 0);
        if (n != 0) {
            c.set(Calendar.SECOND, 1);
            c.add(Calendar.DATE, n);
        } else {
            c.set(Calendar.SECOND, 0);
        }
        return c.getTimeInMillis();
    }

    public static int indexOfNday(String date) {
        String[] week = new String[] {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (int i = 0; i < week.length; i++) {
            if (week[i].equals(date)) {
                return i;
            }
        }
        return -1;
    }
}
