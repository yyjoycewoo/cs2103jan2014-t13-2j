package todomato;

import hirondelle.date4j.DateTime;

import java.util.Locale;
import java.util.TimeZone;

public class Task {
	private static final String START_TIME_PREP = " at ";
	private static final String END_TIME_PREP = " until ";
	private static final String LOCATION_PREP = " in ";
	private static final String DATE_PREP = " on ";
	private static final String SEPERATOR = "#";
	private static final String PRIORITY_LOW = "LOW";
	private static final String IS_TRUE = "true";

	private String description;
	private DateTime startTime;
	private DateTime endTime;
	private DateTime noticeTime;
	private DateTime date;
	private String location;
	private String eventId;
	private DateTime updateTime;
	private int recurrencePeriod;
	private int id;
	private String priorityLevel;
	private DateTime timeCreated;
	private Boolean isCompleted;

	public Task(String userDes) {
		description = userDes;
		timeCreated = DateTime.now(TimeZone.getDefault());
		id = timeCreated.toString().hashCode();
		priorityLevel = PRIORITY_LOW;
		isCompleted = false;
	}

	public Task(String userDes, DateTime userStart, DateTime userEnd,
			DateTime userDate, String userLocation) {
		description = userDes;
		startTime = userStart;
		date = userDate;
		endTime = userEnd;
		location = userLocation;
		recurrencePeriod = 0;
		timeCreated = DateTime.now(TimeZone.getDefault());
		id = timeCreated.toString().hashCode();
		priorityLevel = PRIORITY_LOW;
		isCompleted = false;
	}

	public Task(String userDes, DateTime userStart, DateTime userEnd,
			DateTime userDate, String userLocation, int userRecurrencePeriod) {
		description = userDes;
		startTime = userStart;
		date = userDate;
		endTime = userEnd;
		location = userLocation;
		recurrencePeriod = userRecurrencePeriod;
		timeCreated = DateTime.now(TimeZone.getDefault());
		id = timeCreated.toString().hashCode();
		priorityLevel = PRIORITY_LOW;
		isCompleted = false;
	}

	public Task(Task copy) {
		description = copy.getDescription();
		startTime = copy.getStartTime();
		endTime = copy.getEndTime();
		date = copy.getDate();
		location = copy.getLocation();
		recurrencePeriod = copy.getRecurrencePeriod();
		timeCreated = copy.getTimeCreated();
		id = copy.getId();
		priorityLevel = copy.getPriorityLevel();
		isCompleted = false;
	}

	public static boolean isEqual(String string1, String string2) {
		return (string1 == string2)
				|| ((string1 != null) && string1.equals(string2));
	}

	public Boolean compareDescAndLocation(Task task) {
		if (isEqual(this.description, task.getDescription())
				&& isEqual(this.location, task.getLocation())) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		String task = description;
		if (startTime != null) {
			task += START_TIME_PREP + startTime;
		}
		if (endTime != null) {
			task += END_TIME_PREP + endTime;
		}
		if (date != null) {
			task += DATE_PREP + date.format("MMM DD YYYY", new Locale("US"));
		}
		if (location != null) {
			task += LOCATION_PREP + location;
		}
		if (recurrencePeriod != 0) {
			task += " recurring every " + recurrencePeriod + " days";
		}

		return task;
	}

	/**
	 * Creates a String from task of the following format:
	 * <desc>#<startTime>#<endTime
	 * >#<date>#<location>#<recurrencePeriod>#<id>#<timeCreated
	 * >#<priorityLevel>#<isCompleted>#<eventId>#<updateTime>#<noticeTime>
	 * 
	 * @return
	 */
	public String toFileString() {
		String task = description;
		task += SEPERATOR + startTime + SEPERATOR + endTime + SEPERATOR + date
				+ SEPERATOR + location + SEPERATOR + recurrencePeriod
				+ SEPERATOR + id + SEPERATOR + timeCreated + SEPERATOR
				+ priorityLevel + SEPERATOR + isCompleted + SEPERATOR + eventId
				+ SEPERATOR + updateTime + SEPERATOR + noticeTime;
		return task;
	}

	/**
	 * Create a new Task object from a standard taskString (from data file)
	 * 
	 * @return Task object generated from taskString, null if taskString is
	 *         empty
	 * @author Daryl
	 * @throws InvalidInputException
	 */
	public static Task createTaskFromFileString(String fileInput) {
		String[] parts = fileInput.split(SEPERATOR);
		String description = parts[0];
		DateTime startTime = null;
		DateTime endTime = null;
		DateTime date = null;
		String location = null;
		String eventId = null;
		DateTime updateTime = null;
		DateTime noticeTime = null;
		if (DateTime.isParseable(parts[1])) {
			startTime = new DateTime(parts[1]);
		}
		if (DateTime.isParseable(parts[2])) {
			endTime = new DateTime(parts[2]);
		}
		if (DateTime.isParseable(parts[3])) {
			date = new DateTime(parts[3]);
		}
		if (!parts[4].equals("null")) {
			location = parts[4];
		}
		int recurrencePeriod = Integer.parseInt(parts[5]);
		int id = Integer.parseInt(parts[6]);
		DateTime timeCreated = new DateTime(parts[7]);
		String prioritylevel = parts[8];
		Boolean isComplete = false;
		if (parts[9].equals(IS_TRUE)) {
			isComplete = true;
		}
		eventId = parts[10];
		if (DateTime.isParseable(parts[10])) {
			updateTime = new DateTime(parts[10]);
		}
		if (DateTime.isParseable(parts[11])) {
			noticeTime = new DateTime(parts[11]);
		}
		Task userTask = new Task(description);
		userTask.setStartTime(startTime);
		userTask.setEndTime(endTime);
		userTask.setDate(date);
		userTask.setLocation(location);
		userTask.setRecurrencePeriod(recurrencePeriod);
		userTask.setId(id);
		userTask.setTimeCreated(timeCreated);
		userTask.setPriorityLevel(prioritylevel);
		userTask.setCompleted(isComplete);
		userTask.setEventId(eventId);
		userTask.setUpdateTime(updateTime);
		userTask.setNoticeTime(noticeTime);
		return userTask;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public DateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(DateTime startTime) {
		this.startTime = startTime;
	}

	public DateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(DateTime endTime) {
		this.endTime = endTime;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setDate(DateTime date) {
		this.date = date;
	}

	public DateTime getDate() {
		return date;
	}

	public int getRecurrencePeriod() {
		return recurrencePeriod;
	}

	public void setRecurrencePeriod(int recurrencePeriod) {
		this.recurrencePeriod = recurrencePeriod;
	}

	public String getPriorityLevel() {
		return priorityLevel;
	}

	public void setPriorityLevel(String priorityLevel) {
		this.priorityLevel = priorityLevel;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public DateTime getTimeCreated() {
		return timeCreated;
	}

	public void setTimeCreated(DateTime timeCreated) {
		this.timeCreated = timeCreated;
	}

	public Boolean getCompleted() {
		return isCompleted;
	}

	public void setCompleted(Boolean isCompleted) {
		this.isCompleted = isCompleted;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public void setUpdateTime(DateTime updateTime) {
		this.updateTime = updateTime;
	}

	public String getEventId() {
		return eventId;
	}

	public DateTime getUpdateTime() {
		return updateTime;
	}

	public DateTime getNoticeTime() {
		return noticeTime;
	}

	public void setNoticeTime(DateTime time) {
		this.noticeTime = time;

	}
}
