package Model;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Date
{

	public static String getDate()
	{
		return calcDate();
	}
	private static String calcDate()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date date = new java.util.Date();
		return dateFormat.format(date);
	}

	public static String getMonthYear()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yyyy");
		java.util.Date date = new java.util.Date();
		return dateFormat.format(date);
	}

	public static int getHour()
	{
		return calcHour();
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
