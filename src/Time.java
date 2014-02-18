/**
 * Class that stores the hour and minutes of a time
 * @author Joyce
 *
 */
public class Time {
	private static final char TIME_PADDING = '0';
	private static final char DATE_SEPARATOR = ':';
	private static final int MAX_ONE_DIGIT_INT = 9;
	
	private int hour;
	private int min = 0;
	
	/**
	 * Create a new Time object with the specified hour and 0 minutes
	 * @param hour The hour
	 */
	public Time(int hour) {
		this.setHour(hour);
	}
	
	/**
	 * Create a new Time object with the specified hour and min
	 * @param hour The hour
	 * @param min The minutes
	 */
	public Time(int hour, int min) {
		this(hour);
		this.setMin(min);
	}
	
	@Override
	public String toString() {
		String time = String.valueOf(hour) + DATE_SEPARATOR;
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
