package todomato;

//@author A0101578H
/**
 * This class contains methods to process display commands by the user. 
 * 
 * <p>
 * The following ways to display are supported: 
 * <ul>
 * <li> display all tasks
 * <ul> <li> "display" </ul>
 * </ul>
 * 
 */
public class DisplayProcessor extends Processor {
	private static final String SUCCESS_DISPLAY = "All tasks have been displayed: ";
	
	/**
	 * @param argument  
	 * @return Status message along with a String of the list 
	 */
	public static String processDisplay(String argument) throws InvalidInputException {
		return SUCCESS_DISPLAY + list.toString();
	}
}
