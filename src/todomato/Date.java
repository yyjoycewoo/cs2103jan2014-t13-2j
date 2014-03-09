package todomato;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Class to store the day, month and year of a date
 * 
 * @author Joyce
 * 
 */
public class Date {
	private static final String INVALID_YEAR_SPECIFIED = "year cannot be in the past: ";
	private static final String INVALID_DAY_SPECIFIED = "invalid day for the specified month: ";
	private static final String INVALID_MONTH_SPECIFIED = "invalid month specified: ";
	
	private static final int CURR_YEAR = 2014;
	private static final int MAX_ONE_DIGIT_INT = 9;

	private static final char DATE_SEPARATOR = '/';
	private static final char DATE_PADDING = '0';

	private static final Set<Integer> MONTHS_WITH_31_DAYS = new HashSet<Integer>(Arrays.asList(1, 3, 5, 7, 8, 10, 12));
	private static final Set<Integer> MONTHS_WITH_30_DAYS = new HashSet<Integer>(Arrays.asList(4, 6, 9, 11));
	private static final int FEBRUARY = 2;
	
	private int year = CURR_YEAR;
	private int month;
	private int day;

	/**
	 * Create a new Date object with the specified day, month, and the current
	 * year
	 * 
	 * @param day
	 *            The day
	 * @param month
	 *            The month
	 * @throws InvalidInputException 
	 */
	public Date(int day, int month) throws InvalidInputException {
		if (month < 1 || month > 12)
			throw new InvalidInputException(INVALID_MONTH_SPECIFIED + month);
		
		if (MONTHS_WITH_31_DAYS.contains(month))
			if (day < 1 || day > 31)
				throw new InvalidInputException(INVALID_DAY_SPECIFIED + day);
		
		if (MONTHS_WITH_30_DAYS.contains(month))
			if (day < 1 || day > 30)
				throw new InvalidInputException(INVALID_DAY_SPECIFIED + day);
		
		if (month == FEBRUARY)
			if (day < 1 || day > 28)
				//TODO: deal with leap years
				throw new InvalidInputException(INVALID_DAY_SPECIFIED + day);
				
		this.setMonth(month);
		this.setDay(day);
	}

	/**
	 * Create a new Date object with the specified day, month, and year
	 * 
	 * @param day
	 *            The day
	 * @param month
	 *            The month
	 * @param year
	 *            The year
	 * @throws InvalidInputException 
	 */
	public Date(int day, int month, int year) throws InvalidInputException {
		this(day, month);
		if (year < CURR_YEAR)
			throw new InvalidInputException(INVALID_YEAR_SPECIFIED + year);
		this.setYear(year);
	}

	/**
	 * Create a new Date object with a standard string
	 * 
	 * @param dateString
	 *            a standard string representing date 01/01/2001
	 */
	public Date(String dateString) {
		String[] dateArray = dateString.split("/");
		int day = Integer.parseInt(dateArray[0]);
		int month = Integer.parseInt(dateArray[1]);
		int year = Integer.parseInt(dateArray[2]);
		this.setDay(day);
		this.setMonth(month);
		this.setYear(year);
	}

	@Override
	public String toString() {
		String date = "";

		// if day is only one digit, pad it with an extra digit
		if (isOneDigit(day)) {
			date += DATE_PADDING;
		}
		date += String.valueOf(day);

		date += DATE_SEPARATOR;

		// if month is only one digit, pad it with an extra digit
		if (isOneDigit(month)) {
			date += DATE_PADDING;
		}
		date += String.valueOf(month);

		date += DATE_SEPARATOR + String.valueOf(year);

		return date;
	}

	private boolean isOneDigit(int x) {
		return (x <= MAX_ONE_DIGIT_INT);
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
