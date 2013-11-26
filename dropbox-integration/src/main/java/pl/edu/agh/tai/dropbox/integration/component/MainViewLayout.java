package pl.edu.agh.tai.dropbox.integration.component;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import pl.edu.agh.tai.dropbox.integration.view.LoginView;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * Main view layout. Contains {@link FilesPanel} and logout button.
 * Extends {@link VerticalLayout}
 * @author konrad
 *
 */
@Component
@Scope("prototype")
public class MainViewLayout extends VerticalLayout {
	
	@Autowired
	private FilesPanel filesPanel;
	
	private Button logoutButton = new Button("Logout");
	
	@PostConstruct
	private void init(){
		setSpacing(true);
		setMargin(true);
		logoutButton.setSizeUndefined();
		addComponent(filesPanel);
		addComponent(logoutButton);
		setComponentAlignment(logoutButton, Alignment.BOTTOM_CENTER);
		logoutButton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				UI.getCurrent().getNavigator().navigateTo(LoginView.NAME);
				
			}
		});
	}
}
