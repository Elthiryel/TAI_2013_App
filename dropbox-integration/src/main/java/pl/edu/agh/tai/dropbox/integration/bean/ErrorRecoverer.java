package pl.edu.agh.tai.dropbox.integration.bean;

import com.vaadin.ui.Notification;

/**
 * Simple 'static' class for error recovering
 * @author konrad
 *
 */
public class ErrorRecoverer {
	
	/**
	 * Default error message
	 */
	private static final String DEFAULT_MESSAGE = "Some error occurred";
	
	/**
	 * Shows notification with default error message and prints stack trace for given exception
	 * @param e exception
	 */
	public static void recoverError(Exception e){
		recoverError(e,DEFAULT_MESSAGE);
	}
	
	/**
	 * Shows notification with custom error message and prints stack trace for given exception
	 * @param e exception
	 * @param message custom message
	 */
	public static void recoverError(Exception e, String message){
		Notification.show(message,e.getMessage(),Notification.Type.ERROR_MESSAGE);
		e.printStackTrace();
	}
}
