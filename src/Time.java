
public class Time {
	private int hour;
	private int min = 0;
	
	public Time(int hour) {
		this.setHour(hour);
	}
	
	public Time(int hour, int min) {
		this(hour);
		this.setMin(min);
	}
	
	@Override
	public String toString() {
		String time = String.valueOf(hour) + ':';
		//if min is only one digit, pad it with an extra 0
		if (min < 10) {
			time += '0';
		}
		time += String.valueOf(min);
		return time;
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
