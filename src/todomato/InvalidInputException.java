package todomato;
/**
 * 
 * @author Joyce
 *
 */
public class InvalidInputException extends Exception {

	public InvalidInputException(String message) {
		super(message);
	}
	
    public InvalidInputException(String message, Throwable throwable) {
        super(message, throwable);
    }
    
    public String getMessage()
    {
        return super.getMessage();
    }
}
