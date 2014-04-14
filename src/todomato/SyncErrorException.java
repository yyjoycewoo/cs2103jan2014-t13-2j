
package todomato;

//@author A0099332Y

/**
 * This class signals that there is an error occur in sync process.
 *
 */
@SuppressWarnings("serial")
public class SyncErrorException extends Exception {

	/**
	 * @param message
	 */
	public SyncErrorException(String message) {
		super(message);
	}
	
    /**
     * @param message
     * @param throwable
     */
    public SyncErrorException(String message, Throwable throwable) {
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
