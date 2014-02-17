
public class Date {
	private static final int CURR_YEAR = 2014;

	private static final char DATE_SEPARATOR = '/';
	
	private int year = CURR_YEAR;
	private int month;
	private int day;
	
	public Date(int day, int month) {
		this.setMonth(month);
		this.setDay(day);
	}
	
	public Date(int day, int month, int year) {
		this(month, day);
		this.setYear(year);
	}
	
	@Override
	public String toString() {
		String date = "";

		//if day is only one digit, pad it with an extra 0
		if (day < 10) {
			date += '0';
		}
		date += String.valueOf(day);
		
		date += DATE_SEPARATOR;

		//if month is only one digit, pad it with an extra 0
		if (month < 10) {
			date += '0';
		}
		date += String.valueOf(month);
		
		if (year != CURR_YEAR) {
			date += DATE_SEPARATOR + String.valueOf(year);
		}
		return date;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}
}
