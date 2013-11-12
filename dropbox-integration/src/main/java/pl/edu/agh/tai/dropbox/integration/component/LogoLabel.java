package pl.edu.agh.tai.dropbox.integration.component;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;

public class LogoLabel extends Label {

	private final String LOGO_TITTLE = "<font color=blue size=6>"
			+ "Dropbox Integration"
			+ "</font>";
	
	public LogoLabel(){
		setContentMode(ContentMode.HTML);
		setValue(LOGO_TITTLE);
	}
}
