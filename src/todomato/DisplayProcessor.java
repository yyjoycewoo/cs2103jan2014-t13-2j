package todomato;

/**
 * This class contains methods to process display commands by the user.
 * It is used to display all of a user's tasks when using the command line,
 * but the GUI automatically displays the user's tasks after each command.
 * It also creates notifications about upcoming tasks.
 *
 */
public class DisplayProcessor extends Processor {
	/**
	 * @author linxuan
	 * @return TaskList
	 */
	private static TaskDTList notify = new TaskDTList();

	/**
	 * @return
	 */
	 */
	public static TaskDTList processDisplay() {
		notify.clearList();
		// for notification checking
		for (int i = 0; i < list.getSize(); i++) {
			TaskDT item = list.getListItem(i);
			if (item.getDate() != null) {
				// ***********need to have a better Date API*************/
				int day = item.getDate().getDay() - 1;
				String deadline = day + "/0" + item.getDate().getMonth() + "/"
						+ item.getDate().getYear();
				// ******************************************************/
				// notification will pop out on that day itself & the day after
				if (deadline.contains(CurrentDate.date())
						|| item.getDate().toString()
								.contains(CurrentDate.date())) {
					notify.addToList(item);
				}
			}
		}
		for (int i = 0; i < notify.getSize(); i++) {
			Notification.popUpNotice(notify.getListItem(i).toString());
		}

		return list;
	}
}
