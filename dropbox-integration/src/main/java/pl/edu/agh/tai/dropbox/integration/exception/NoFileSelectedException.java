package pl.edu.agh.tai.dropbox.integration.exception;

/**
 * Exception thrown when selected null file from table.
 * {link {@link Exception}
 * @author konrad
 *
 */
public class NoFileSelectedException extends Exception {
	
	/**
	 * Default message
	 */
	private static final String DEFAULT_MESSAGE = "No file selected";
	
	/**	
	 * Deafault constructor. Sets default message.
	 */
	public NoFileSelectedException(){
		super(DEFAULT_MESSAGE);
	}
}
