//@author A0101324A

package todomato;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class stores the current date.
 * 
 */
public class CurrentDate {

	/**
	 * @return
	 */
	protected static String date() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-dd-MM");
		// get current date time with Date()
		Date date = new Date();
		return dateFormat.format(date);
	}

}
