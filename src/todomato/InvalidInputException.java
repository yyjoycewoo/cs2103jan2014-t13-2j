package todomato;

//@author A0120766H
/**
 * This class signals that the user has entered an unknown input.
 *
 */
@SuppressWarnings("serial")
public class InvalidInputException extends Exception {

	/**
	 * @param message
	 */
	public InvalidInputException(String message) {
		super(message);
	}
	
    /**
     * @param message
     * @param throwable
     */
    public InvalidInputException(String message, Throwable throwable) {
        super(message, throwable);
    }
    
    /* (non-Javadoc)
     * @see java.lang.Throwable#getMessage()
     */
    public String getMessage()
    {
        return super.getMessage();
    }
}
