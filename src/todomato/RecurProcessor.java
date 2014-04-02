package todomato;

import hirondelle.date4j.DateTime;

import java.util.TimeZone;

import todomato.Processor;

public class RecurProcessor extends Processor {
	

	private static String RECURRING_TASKS_ADDED = "Recurring tasks have been added";
	
	public static String processRecur() {
		for (int i = 0; i < list.getSize(); i++){
			addsRecurringTask(list.getListItem(i));
		}
		fileHandler.updateFile(list);
		return RECURRING_TASKS_ADDED;
	}
	
	/**
	 * Adds tasks that have expired and needs to be recurred
	 * @param task
	 */
	

	protected static void addsRecurringTask(Task task) {
		if (needsToBeRecurred(task)) {
			Task newTask = new Task(task);
			newTask.setDate(task.getDate().plusDays(task.getRecurrencePeriod()));
			list.addToList(newTask);
		}
	}
	/**
	 * Checks if there will be a duplicate task
	 * @param task
	 * @return Boolean
	 */
	protected static Boolean checkIfDuplicateRecurTaskExist (Task task) {

		DateTime recurDate = task.getDate().plusDays(task.getRecurrencePeriod());
		for (int i = 0; i < list.getSize(); i++) {
			if (list.getListItem(i).getDate() == null) {
				return false;
			}
			if (list.getListItem(i).getDate().equals(recurDate)) {
				if (list.getListItem(i).compareDescAndLocation(task)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Checks if the task needs to be readded to the list
	 * These are the two conditions for recurrence:
	 * Task must have expired
	 * There must not be another task with the same description
	 * and location on the date it is set to be recurred
	 * @param task
	 * @return Boolean
	 */

	protected static Boolean needsToBeRecurred(Task task) {
		if (task.getRecurrencePeriod() == 0) {
			return false;
		}
		TimeZone SGT = TimeZone.getTimeZone("GMT+8");
		DateTime recurDate = task.getDate().plusDays(task.getRecurrencePeriod());
		if (DateTime.today(SGT).numDaysFrom(recurDate) < task.getRecurrencePeriod()) {
			if (!checkIfDuplicateRecurTaskExist(task)) {
				return true;
			}
		}
		return false;
	}
	

}
