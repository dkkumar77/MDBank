package Model.Definitions;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class Date
{
	public static String getDate()
	{
		return calcDate();
	}

	public final static String START_TIME = "00:00:00.000";
	public final static String END_TIME = "23:59:59.999";

	private static String calcDate()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date date = new java.util.Date();
		return dateFormat.format(date);
	}

	public static String getFirstDayOfMonth(int month,int year)
	{
		YearMonth yearMonth = YearMonth.of( year, month );
		LocalDate firstOfMonth = yearMonth.atDay(1);
		return firstOfMonth.toString();
	}

	public static String getMonth(int month, int year)
	{
		YearMonth yearMonth = YearMonth.of( year, month);
		return yearMonth.getMonth().toString();
	}

	public static String getLastDayOfMonth(int month, int year)
	{
		YearMonth yearMonth = YearMonth.of( year, month );
		LocalDate last = yearMonth.atEndOfMonth();
		return last.toString();
	}

	public static int getHour()
	{
		return calcHour();
	}

	public static Timestamp getSQLTime()
	{
		return java.sql.Timestamp.from(Instant.now());
	}

	private static int calcHour()
	{
		SimpleDateFormat hourformat = new SimpleDateFormat("HH");
		java.util.Date date = new java.util.Date();
		return Integer.parseInt(hourformat.format(date));
	}

	public static String getTime()
	{
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		return dtf.format(now);
	}

	public static String getGreeting()
	{
		int hour = calcHour();
		String greeting;
		if(hour>=0 && hour<12)
			greeting = "Good Morning ";
		else if(hour>=12 && hour<=16)
			greeting =  "Good Afternoon ";
		else
			greeting  = "Good Evening ";
		return greeting;
	}
}
