package pl.edu.agh.tai.dropbox.integration.component;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import pl.edu.agh.tai.dropbox.integration.bean.SessionData;
import pl.edu.agh.tai.dropbox.integration.dao.UserDao;
import pl.edu.agh.tai.dropbox.integration.security.AdminAuthenticationManager;
import pl.edu.agh.tai.dropbox.integration.security.SecurityHelper;
import pl.edu.agh.tai.dropbox.integration.security.UserAuthenticationManager;
import pl.edu.agh.tai.dropbox.integration.view.MainView;
import pl.edu.agh.tai.dropbox.integration.view.RegisterView;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;

/**
 * Login layout. 
 * Contains form with fields:
 * -login
 * -password
 * -as admin checkbox
 * -link to registration page
 * 
 * Extends {@link VerticalLayout}
 * @author konrad
 *
 */
@Component
@Scope("prototype")
public class LoginLayout extends VerticalLayout {

	@Autowired
	private UserAuthenticationManager userAuthenticationManager;

	@Autowired
	private AdminAuthenticationManager adminAuthenticationManager;
	
	@Autowired
	private SessionData sessionData;
	
	@Autowired
	private UserDao userDao;

	private TextField loginField = new TextField("Login");
	private PasswordField passwordField = new PasswordField("Password");
	private CheckBox isAdminCheckBox = new CheckBox("Log as admin");
	private Button loginButton = new Button("Login");
	private Button registerButton = new Button("Register");

	@PostConstruct
	private void init() {
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

		loginButton.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				Boolean logAsAdmin = isAdminCheckBox.getValue();
				String login = loginField.getValue();
				String password = passwordField.getValue();
				UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
						login, password);
				try{
					if (logAsAdmin.booleanValue())
						logAsAdmin(token);
					else
						logAsUser(token);
				}catch(AuthenticationException e){
					Notification.show(e.getMessage(),Notification.Type.ERROR_MESSAGE);
				}
			}
		});
		
		loginButton.addShortcutListener(new Button.ClickShortcut(loginButton, KeyCode.ENTER));

	}

	private void logAsUser(UsernamePasswordAuthenticationToken token) {
		login(userAuthenticationManager.authenticate(token));

	}

	private void logAsAdmin(UsernamePasswordAuthenticationToken token) {
		login(adminAuthenticationManager.authenticate(token));

	}

	private void login(Authentication result) {
		SecurityContextHolder.getContext().setAuthentication(result);
		sessionData.setUser(userDao.findByLogin(SecurityHelper.getUser().getUsername()));
		UI.getCurrent().getNavigator().navigateTo(MainView.NAME);
	}

	private void setComponents() {
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
