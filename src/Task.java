
public class Task {
	private static final String START_TIME_PREP = " at ";
	private static final String END_TIME_PREP = " until ";
	private static final String DATE_PREP = " on ";
	private static final String LOCATION_PREP = " in ";
	
	private String description;
	private Time startTime;
	private Time endTime;
	private Date date;
	private String location;
	
	public Task(String description) {
		this.setDescription(description);
	}
	
	public Task(String description, Date date) {
		this.setDescription(description);
		this.setDate(date);
	}
	
	public Task(String description, Time startTime) {
		this(description);
		this.setStartTime(startTime);
	}
	
	public Task(String description, String location) {
		this.setDescription(description);
		this.setLocation(location);
	}

	public Task(String description, Time startTime, Date date) {
		this(description, startTime);
		this.setDate(date);
	}
	
	public Task(String description, Time startTime, Time endTime) {
		this(description, startTime);
		this.setEndTime(endTime);
	}
	
	public Task(String description, Time startTime, String location) {
		this(description, startTime);
		this.setLocation(location);
	}
	
	public Task(String description, Date date, String location) {
		this(description, date);
		this.setLocation(location);
	}
	
	public Task(String description, Time startTime, Time endTime, Date date) {
		this(description, startTime, endTime);
		this.setDate(date);
	}
	
	public Task(String description, Time startTime, Time endTime, String location) {
		this(description, startTime, endTime);
		this.setLocation(location);
	}
	
	public Task(String description, Time startTime, Time endTime, Date date, String location) {
		this(description, startTime, endTime, date);
		this.setLocation(location);
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
