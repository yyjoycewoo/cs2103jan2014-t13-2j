/**
 * 
 */
package todomato;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Hao Eng
 * 
 */
public class CurrentDate {

	protected static String date() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		// get current date time with Date()
		Date date = new Date();
		return dateFormat.format(date);
	}
}
