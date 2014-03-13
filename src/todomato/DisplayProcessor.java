package todomato;

public class DisplayProcessor extends Processor {
	/**
	 * @author linxuan
	 * @return TaskList
	 */
	private static TaskList notify = new TaskList();

	public static TaskList processDisplay() {

		// for notification checking
		for (int i = 0; i < list.getSize(); i++) {
			Task item = list.getListItem(i);
			if (item.getDate() != null) {
				// ***********need to have a better Date API*************/
				int day = item.getDate().getDay() - 1;
				String deadline = day + "/0" + item.getDate().getMonth() + "/"
						+ item.getDate().getYear();
				// ******************************************************/
				if (deadline.contains(CurrentDate.date())
						|| item.getDate().equals(CurrentDate.date())) {
					System.out.println("deadline: " + deadline + ".. curdate: "
							+ CurrentDate.date() + "item: " + item.getDate());
					System.out.println(item.getDescription());
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
