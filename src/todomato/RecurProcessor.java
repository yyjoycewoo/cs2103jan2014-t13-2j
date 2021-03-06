package todomato;

import hirondelle.date4j.DateTime;

import java.util.TimeZone;

import todomato.Processor;

//@author A0096620E
public class RecurProcessor extends Processor {
	
	private static String RECURRING_TASKS_ADDED = "Recurring tasks have been added";
	
	public static String processRecur(String input) {
		storeCurrentList();
		for (int i = 0; i < list.getSize(); i++){
			addsRecurringTask(list.getListItem(i));
		}
		fileHandler.updateFile(list);
		return RECURRING_TASKS_ADDED;
	}
	
	/**
	 * Adds tasks that have expired (date is after current date) and needs to be recurred
	 * @param task
	 */
	
	private static void addsRecurringTask(Task task) {
		if (needsToBeRecurred(task)) {
			Task newTask = new Task(task);
			if (newTask.getStartDate() != null) {
				newTask.setStartDate(task.getStartDate().plusDays(task.getRecurrencePeriod()));
			}
			newTask.setEndDate(task.getEndDate().plusDays(task.getRecurrencePeriod()));
			newTask.setTimeCreated(DateTime.now(TimeZone.getTimeZone(SG_TIMEZONE)));
			newTask.setCompleted(false);
			list.addToList(newTask);
		}
	}
	/**
	 * Checks if there will be a duplicate task
	 * @param task
	 * @return Boolean
	 */
	private static Boolean checkIfDuplicateRecurTaskExist (Task task) {

		DateTime recurEndDate = task.getEndDate().plusDays(task.getRecurrencePeriod());
		for (int i = 0; i < list.getSize(); i++) {
			if (list.getListItem(i).getEndDate() == null) {
				return false;
			}
			if (list.getListItem(i).getEndDate().equals(recurEndDate)) {
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
		TimeZone SGT = TimeZone.getTimeZone(SG_TIMEZONE);
		DateTime recurEndDate = task.getEndDate().plusDays(task.getRecurrencePeriod());
		if (DateTime.today(SGT).numDaysFrom(recurEndDate) < task.getRecurrencePeriod()) {
			if (!checkIfDuplicateRecurTaskExist(task)) {
				return true;
			}
		}
		return false;
	}
	

}
