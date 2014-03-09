/**
 * Class that stores the hour and minutes of a time
 * @author Joyce
 *
 */
/**
 * @author Joyce
 *
 */
public class Time {
	private static final char TIME_PADDING = '0';
	private static final char TIME_SEPARATOR = ':';
	private static final int MAX_ONE_DIGIT_INT = 9;
	
	private int hour;
	private int min;
	
	/**
	 * Create a new Time object with the specified hour and 0 minutes
	 * @param hour The hour
	 * @throws InvalidInputException 
	 */
	public Time(int hour) throws InvalidInputException {
		this(hour, 0);
	}
	
	/**
	 * Create a new Time object with the specified hour and min
	 * @param hour The hour
	 * @param min The minutes
	 * @throws InvalidInputException 
	 */
	public Time(int hour, int min) throws InvalidInputException {
		if (hour < 0 || hour > 23)
			throw new InvalidInputException("invalid hour specified: " + hour);
		if (min < 0 || min > 59)
			throw new InvalidInputException("invalid minutes specified: " + min);
		this.setHour(hour);
		this.setMin(min);
	}
	
	
	/**
	 * Create a new Time object with a standard string
	 * @param timeString a standard string representing time 00:00
	 */
	public Time(String timeString) {
		String[] timeArray = timeString.split(":");
		int hour = Integer.parseInt(timeArray[0]);
		int min = Integer.parseInt(timeArray[1]);
		this.setHour(hour);
		this.setMin(min);
	}
	
	@Override
	public String toString() {
		String time = "";
		
		//if hour is only one digit, pad it with an extra digit
		if (isOneDigit(hour)) {
			time += TIME_PADDING;
		}
		
		time += (String.valueOf(hour) + TIME_SEPARATOR);
		
		//if min is only one digit, pad it with an extra digit
		if (isOneDigit(min)) {
			time += TIME_PADDING;
		}
		time += String.valueOf(min);
		return time;
	}
	
	private boolean isOneDigit(int x) {
		return (x <= MAX_ONE_DIGIT_INT);
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

}
