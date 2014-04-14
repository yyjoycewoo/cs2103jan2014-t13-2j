package todomato;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

//@author A0101324A
/**
 * This class stores the current date.
 * 
 */
public class CurrentDate {
	private static String format = "yyyy-dd-MM";

	protected static String date() {
		DateFormat dateFormat = new SimpleDateFormat(format);
		// get current date time with Date()
		Date date = new Date();
		return dateFormat.format(date);
	}
}
