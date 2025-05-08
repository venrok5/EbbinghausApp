package ru.alex.project.EbbinghausApp.util;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

public class ExtendedDate extends Date { 

	@SuppressWarnings("deprecation")
	public ExtendedDate(int year, int month, int day) {
		super(year, month, day);
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 1L;

	public static Date addHoursToDate(Date date, int hours) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    calendar.add(Calendar.HOUR_OF_DAY, hours);
	    return (Date)calendar.getTime();
	}
	
	public static Date addDaysToDate(Date date, int days) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    calendar.add(Calendar.DAY_OF_MONTH, days);
	    return (Date)calendar.getTime();
	}
	
	public static String dateToString(Date date, String pattern) {
		SimpleDateFormat formater = new SimpleDateFormat(pattern);
		return formater.format(date);
	}
	
	public static int getNumberDaysBetween(Date firstDate, Date secondDate) {
		return (int)ChronoUnit.DAYS.between(firstDate.toInstant(), secondDate.toInstant());
	}
	
	public static int getNumberOfMinutesBetween(Date start, Date end) {
        long numberOfMilliseconds = end.getTime() - start.getTime();
        long numberOfMinutes = TimeUnit.MINUTES.convert(numberOfMilliseconds, TimeUnit.MILLISECONDS);
        return (int)numberOfMinutes;
    }
	
	public static boolean isPresent(Date date) {
		SimpleDateFormat formater = new SimpleDateFormat(ConstantsClass.DATE_PATTERN);
		
		try {
			Date todayDate = formater.parse(formater.format(new Date()) );
			date = formater.parse(formater.format(date));
			
			if (date.compareTo(todayDate) > 0) { // not expired
				return false;
			} else if (date.compareTo(todayDate) == 0) { // dates are same
				if (date.getTime() < todayDate.getTime()) {
					return false; // time not expired
				} else {
					return true; // expired
				}
			} else {
				return true; // case date.compareTo(todayDate) > 0) - expired
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
			
		}
		return false;
	}
}