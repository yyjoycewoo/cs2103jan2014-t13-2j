/**
 * 
 */
package todomato;

/**
 * @author Hao Eng
 * 
 */
public class Popup {

	static TaskDTList myownlist = new TaskDTList();

	public static void show() {

		myownlist.clearList();
		// for notification checking
		for (int i = 0; i < Processor.getList().getSize(); i++) {
			TaskDT item = Processor.getList().getListItem(i);
			if (item.getDate() != null) {
				// ***********need to have a better Date API*************
				int day = item.getDate().getDay() - 1;
				String deadline = day + "/0" + item.getDate().getMonth() + "/"
						+ item.getDate().getYear();
				// ******************************************************
				// notification will pop out on that day itself & the day after
				if (deadline.contains(CurrentDate.date())
						|| item.getDate().toString()
								.contains(CurrentDate.date())) {
					myownlist.addToList(item);
				}
			}
		}
		for (int i = 0; i < myownlist.getSize(); i++) {
			Notification.popUpNotice(myownlist.getListItem(i).toString());
		}
	}
}
