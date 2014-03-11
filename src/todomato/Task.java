package todomato;

import java.util.Arrays;
import java.util.List;

/**
 * Class that stores a Task description, and optional additional information.
 * @author Joyce
 *
 */
public class Task {
	private static final String START_TIME_PREP = " at ";
	private static final String END_TIME_PREP = " until ";
	private static final String DATE_PREP = " on ";
	private static final String LOCATION_PREP = " in ";
	private static final String START_TIME_KEY = "at";
	private static final String END_TIME_KEY = "until";
	private static final String DATE_KEY = "on";
	private static final String LOCATION_KEY = "in";

	
	private String description;
	private Time startTime;
	private Time endTime;
	private Date date;
	private String location;
	
	/**
	 * Create a new Task object with description
	 * @param description The description
	 */
	public Task(String description) {
		this.setDescription(description);
	}
	
	/**
	 * Create a new Task object with description and date
	 * @param description The description
	 * @param date The date
	 */
	public Task(String description, Date date) {
		this.setDescription(description);
		this.setDate(date);
	}
	
	/**
	 * Create a new Task object with description and startTime
	 * @param description The description
	 * @param startTime The start time
	 */
	public Task(String description, Time startTime) {
		this(description);
		this.setStartTime(startTime);
	}
	
	/**
	 * Create a new Task object with description and location
	 * @param description The description
	 * @param location The location
	 */
	public Task(String description, String location) {
		this.setDescription(description);
		this.setLocation(location);
	}

	/**
	 * Create a new Task object with description, startTime and date
	 * @param description The description
	 * @param startTime The start time
	 * @param date The date
	 */
	public Task(String description, Time startTime, Date date) {
		this(description, startTime);
		this.setDate(date);
	}
	
	/**
	 * Create a new Task object with description, startTime and endTime
	 * @param description The description
	 * @param startTime The start time
	 * @param endTime The end time
	 */
	public Task(String description, Time startTime, Time endTime) {
		this(description, startTime);
		this.setEndTime(endTime);
	}
	
	/**
	 * Create a new Task object with description, startTime and location
	 * @param description The description
	 * @param startTime The start time
	 * @param location The location
	 */
	public Task(String description, Time startTime, String location) {
		this(description, startTime);
		this.setLocation(location);
	}
	
	/**
	 * Create a new Task object with description, date and location
	 * @param description The description
	 * @param date The date
	 * @param location The location
	 */
	public Task(String description, Date date, String location) {
		this(description, date);
		this.setLocation(location);
	}
	
	/**
	 * Create a new Task object with description, startTime, endTime and date
	 * @param description The description
	 * @param startTime The start time
	 * @param endTime The end time
	 * @param date The date
	 */
	public Task(String description, Time startTime, Time endTime, Date date) {
		this(description, startTime, endTime);
		this.setDate(date);
	}
	
	/**
	 * Create a new Task object with description, startTime, endTime, location
	 * @param description The description
	 * @param startTime The start time
	 * @param endTime The end time
	 * @param location The location
	 */
	public Task(String description, Time startTime, Time endTime, String location) {
		this(description, startTime, endTime);
		this.setLocation(location);
	}
	
	/**
	 * Create a new Task object with description, startTime, endTime, date and location
	 * @param description The description
	 * @param startTime The start time
	 * @param endTime The end time
	 * @param date The date
	 * @param location The location
	 */
	public Task(String description, Time startTime, Time endTime, Date date, String location) {
		this(description, startTime, endTime, date);
		this.setLocation(location);
	}
	
	/**
	 * Create a new Task object that is a deep copy of the Task t
	 * @param t Task to be copied
	 */
	public Task(Task t) {
		this.setDate(t.getDate());
		this.setDescription(t.getDescription());
		this.setStartTime(t.getStartTime());
		this.setEndTime(t.getEndTime());
		this.setLocation(t.getLocation());
	}

	/**
	 * Create a new Task object from a standard taskString (from data file) 
	 * TODO: need to Refractor
	 * @param taskString "eat at 05:00 until 07:00 on 16/02/2014 in utown"
	 * @return Task object generated from taskString, null if taskString is empty
	 * @author Yiwen
	 * @throws InvalidInputException 
	 */
	public static Task createTaskFromString(String taskString) throws InvalidInputException {
		Task task;
		String description = null;
		Time startTime = null;
		Time endTime = null;
		Date date = null;
		String location = null;
		
		
		if (taskString.isEmpty()) {
			task = null;
			
		} else {
			
			List<String> taskStringList = Arrays.asList(taskString.split(" "));
			
			int descriptionIndex = 0;
			int startTimeIndex = taskStringList.indexOf(START_TIME_KEY) + 1;
			int endTimeIndex = taskStringList.indexOf(END_TIME_KEY) + 1;
			int dateIndex = taskStringList.indexOf(DATE_KEY) + 1;
			int locationIndex = taskStringList.indexOf(LOCATION_KEY) + 1;
			int locationIndexEnd = taskStringList.size();
			
			description = taskStringList.get(descriptionIndex);
			if ( startTimeIndex != 0 ) {
				startTime = new Time(taskStringList.get(startTimeIndex));
			}
			if ( endTimeIndex != 0 ) {
				endTime = new Time(taskStringList.get(endTimeIndex));
			}
			if ( dateIndex != 0 ) {
				date = new Date(taskStringList.get(dateIndex));
			}
			if ( locationIndex != 0 ) {
				location = taskStringList.get(locationIndex);
				for (int i = locationIndex + 1; i < locationIndexEnd; i++ ){
					location = location + " " + taskStringList.get(i) ;
				}
			}
			task = new Task(description, startTime, endTime, date, location);
		}
		
		return task;

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
			task += DATE_PREP + date;
		}
		if (location != null) {
			task += LOCATION_PREP + location;
		}
			
		return task;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Time getStartTime() {
		return startTime;
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	public Time getEndTime() {
		return endTime;
	}

	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	
}
