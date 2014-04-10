/**
 * 
 */
package todomato;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//@author A0101324A
public class Popup extends Processor {

	protected static TaskList myownlist = new TaskList();
	// how many days from today for tasks to pop up
	protected static int daysB4deadline = 3;
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-dd-MM");

	public static void show() {
		// create calendar instance
		Calendar now = Calendar.getInstance();
		now.add(Calendar.DATE, daysB4deadline);

		myownlist.clearList();
		// for notification checking
		for (int i = 0; i < Processor.getList().getSize(); i++) {
			Task item = Processor.getList().getListItem(i);
			// task has date and is not completed
			if ((item.getEndDate() != null) && !item.getCompleted()) {
				int m = now.get(Calendar.MONTH) + 1;
				String deadline = convertDateToStandardForm("" + m,
						"" + now.get(Calendar.DATE));
				String present = CurrentDate.date();
				String item_date = convertDateToStandardForm(item.getEndDate()
						.getMonth().toString(), item.getEndDate().getDay()
						.toString());
				try {
					Date max = sdf.parse(deadline);
					Date min = sdf.parse(present);
					Date d = sdf.parse(item_date);
					// check whether the task falls between today and #days
					// after today
					if ((d.after(min) && d.before(max))) {
						myownlist.addToList(item);
					}
					// check task's date == today
					if (d.equals(min)) {
						myownlist.addToList(item);
					}
					// check task's date == #days after today
					if (d.equals(max)) {
						myownlist.addToList(item);
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if (myownlist.getSize() != 0) {
			Notification.popUpNotice();
		}
	}
}
