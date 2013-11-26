package pl.edu.agh.tai.dropbox.integration.component;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;

/**
 * Label with upper logo.
 * Extends {@link Label}
 * @author konrad
 *
 */
public class LogoLabel extends Label {

	private final String LOGO_TITTLE = "DROPBOX INTEGRATION" ; 
	
	/**
	 * Default constructor.
	 * Sets content to HTML mode.
	 * Sets custom styles.
	 */
	public LogoLabel(){
		setContentMode(ContentMode.HTML);
		setValue(LOGO_TITTLE);
		setStyleName("logoLayoutLabel");
		setSizeFull();
	}
}
