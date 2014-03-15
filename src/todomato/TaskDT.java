package todomato;

import java.util.Locale;

import hirondelle.date4j.DateTime;


public class TaskDT {
	private static final String START_TIME_PREP = " at ";
	private static final String END_TIME_PREP = " until ";
	private static final String LOCATION_PREP = " in ";
	private static final String DATE_PREP = " on ";
	
	private String description;
	private DateTime startTime;
	private DateTime endTime;
	private DateTime date;
	private String location;
	
	public TaskDT(String userDes) {
		description = userDes;
	}
	
	public TaskDT(String userDes, DateTime userStart, DateTime userEnd, DateTime userDate, String userLocation) {
		description = userDes;
		startTime = userStart;
		date = userDate;
		endTime = userEnd;
		location = userLocation;
	}
	
	public TaskDT(TaskDT copy) {
		description = copy.getDescription();
		startTime = copy.getStartTime();
		endTime = copy.getEndTime();
		date = copy.getDate();
		location = copy.getLocation();
	}
	
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
			
		return task;
	}
	
	/**
	 * Create a new Task object from a standard taskString (from data file) 
	 * TODO: need to Refractor
	 * @param taskString "eat at 05:00 until 07:00 on 16/02/2014 in utown"
	 * @return Task object generated from taskString, null if taskString is empty
	 * @author Yiwen
	 * @throws InvalidInputException 
	 */
	public static TaskDT createTaskFromString(String taskString) throws InvalidInputException {
		TaskDT task = null;
		
		if (taskString.isEmpty()) {
			task = null;
			
		} else {
			task = AddProcessor.parseTask(taskString);
		}
		return task;

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
}
