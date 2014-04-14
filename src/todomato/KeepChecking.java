package todomato;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

//@author A0101324A
/*
 * Checking the notify time for each task (if possible) and enable pop up 
 * if the current time matches the notify time for that task
 */
public class KeepChecking implements Job {
	private static String timeFormat = "HH:mm";
	TaskList today_pop = new TaskList();

	@Override
	public void execute(JobExecutionContext context) {
		today_pop.clearList();
		// getting current time
		String timeStamp = new SimpleDateFormat(timeFormat).format(Calendar
				.getInstance().getTime());

		for (int i = 0; i < Processor.getList().getSize(); i++) {
			// if there is notify time for that task
			if (Processor.getList().getListItem(i).getNoticeTime() != null) {
				// if the current time matches the notify time
				if (Processor.getList().getListItem(i).getNoticeTime()
						.toString().contains(timeStamp)) {
					// pop up the relevant task at that time
					today_pop.addToList(Processor.getList().getListItem(i));
				}
			}
		}
		if (today_pop.getSize() != 0) {
			// inform the notification to pop up the relevant tasks for today
			Notification.popUpNotice(today_pop);
		}
	}
}
