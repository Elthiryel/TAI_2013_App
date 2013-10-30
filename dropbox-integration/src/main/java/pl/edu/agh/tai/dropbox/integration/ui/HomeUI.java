package pl.edu.agh.tai.dropbox.integration.ui;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

/**
 * Main vaadin component. It registers all views and creates site template
 * 
 * @author konrad
 * 
 */
@Component
@Scope("session")
public class HomeUI extends UI {

	@Override
	protected void init(VaadinRequest request) {
		setContent(new Label("Hello"));

	}

}
