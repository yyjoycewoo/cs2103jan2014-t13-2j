
public class Todomato {

	public static void main(String[] args) {

		System.out.println("Welcome to Todomato");
		
		//This represents 10:07
		Time sampleStartTime = new Time(10, 07);
		//This represents 22:40. Minutes are optional
		Time sampleEndTime = new Time(22, 4);
		//This represents Feb 8. Year is optional, will only display if it is not the current year.
		Date sampleDate = new Date(2, 8);
		String sampleLocation = "library";
		
		/* I only implemented the basic info for now: desc (mandatory), start/end time, date, location.
		 * I made constructors for all possible combinations of the above,
		 * except you can't have an end time without a start time
		 */
		Task sampleTaskDesc = new Task("Meeting");
		Task sampleTaskDescStart = new Task("Meeting", sampleStartTime);
		Task sampleTaskDescStartEnd = new Task("Meeting", sampleStartTime, sampleEndTime);
		Task sampleTaskDescStartLocation = new Task("Meeting", sampleStartTime, sampleEndTime, sampleLocation);
		Task sampleTaskDescStartEndDate = new Task("Meeting", sampleStartTime, sampleEndTime, sampleDate);
		Task sampleTaskDescStartEndDateLocation = new Task("Meeting", sampleStartTime, sampleEndTime, sampleDate, sampleLocation);
		
		System.out.println(sampleTaskDesc);
		System.out.println(sampleTaskDescStart);
		System.out.println(sampleTaskDescStartEnd);
		System.out.println(sampleTaskDescStartLocation);
		System.out.println(sampleTaskDescStartEndDate);
		System.out.println(sampleTaskDescStartEndDateLocation);

	}
}