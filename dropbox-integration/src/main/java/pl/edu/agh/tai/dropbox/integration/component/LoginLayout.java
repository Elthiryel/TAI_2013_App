package pl.edu.agh.tai.dropbox.integration.component;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import pl.edu.agh.tai.dropbox.integration.view.RegisterView;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;

@Component
@Scope("prototype")
public class LoginLayout extends VerticalLayout {
	
	private TextField loginField = new TextField("Login");
	private TextField passwordField = new TextField("Password");
	private CheckBox  isAdminCheckBox = new CheckBox("Log as admin");
	private Button loginButton = new Button("Login");
	private Button registerButton = new Button("Register");
	
	@PostConstruct
	private void init(){
		setSizeFull();
		setSpacing(true);
		setComponents();
		setListeners();
	}
	
	private void setListeners() {
		registerButton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				UI.getCurrent().getNavigator().navigateTo(RegisterView.NAME);
				
			}
		});
		
	}

	private void setComponents(){
		loginButton.setImmediate(true);
		registerButton.setImmediate(true);
		registerButton.setStyleName(BaseTheme.BUTTON_LINK);
		isAdminCheckBox.setImmediate(true);
		
		addComponent(loginField);
		addComponent(passwordField);
		addComponent(isAdminCheckBox);
		addComponent(loginButton);
		addComponent(registerButton);
		
	}


}
