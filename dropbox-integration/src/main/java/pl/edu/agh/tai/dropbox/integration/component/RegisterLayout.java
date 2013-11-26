package pl.edu.agh.tai.dropbox.integration.component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import pl.edu.agh.tai.dropbox.integration.bean.SessionData;
import pl.edu.agh.tai.dropbox.integration.dao.UserDao;
import pl.edu.agh.tai.dropbox.integration.model.User;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxSessionStore;
import com.dropbox.core.DbxStandardSessionStore;
import com.dropbox.core.DbxWebAuth;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.UI;

/**
 * Registration layout.
 * Contains fields:
 * -user name
 * -user password
 * -admin password
 * -register button
 * Extends {@link FormLayout}
 * @author konrad
 *
 */
@Component
@Scope("prototype")
public class RegisterLayout extends FormLayout {

	private Button registerButton = new Button("Register");
	BeanFieldGroup<User> binder = new BeanFieldGroup<User>(User.class);

	@Autowired
	private SessionData sessionData;
	
	@Autowired
	private UserDao userDao;

	@PostConstruct
	private void init() {
	
		initUserLayout();
		setListeners();
	}

	private void setListeners() {
		registerButton.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				try{
					binder.commit();
					onRegisterAction();
				}catch(CommitException e){
					Notification.show("Wrong data provided",Notification.Type.ERROR_MESSAGE);
				}
				

			}
		});

	}

	private void onRegisterAction() {
		sessionData.setUser(
				binder.getItemDataSource().getBean());
		
		DbxRequestConfig requestConfig = new DbxRequestConfig("text-edit/0.1", null);
		DbxAppInfo appInfo = new DbxAppInfo("tog0pn54fns1v07", "bag11rqa652hg1s");
		String redirectUri = "http://localhost:8080/DropboxIntegration/token/";
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession session = attr.getRequest().getSession(true);
		String sessionKey = "dropbox-auth-csrf-token";
		DbxSessionStore csrfTokenStore = new DbxStandardSessionStore(session, sessionKey);
		DbxWebAuth webAuth = new DbxWebAuth(requestConfig, appInfo, redirectUri, csrfTokenStore);
		sessionData.setAuth(webAuth);
		String authUrl = webAuth.start();
		UI.getCurrent().getPage().setLocation(authUrl);
	}

	private void initUserLayout() {
		User user = new User();
		binder.setItemDataSource(user);
	
		Field<?> loginField = binder.buildAndBind("login");
		loginField.addValidator(new UserValidator());
	
		addComponent(loginField);
		addComponent(binder.buildAndBind("User password", "userPassword", PasswordField.class));
		addComponent(binder.buildAndBind("Admin password", "adminPassword", PasswordField.class));
		addComponent(registerButton);
		binder.setBuffered(true);
	}
	
	/**
	 * Validator for user login field.
	 * Check if login is unique.
	 * {link {@link Validator}
	 * @author konrad
	 *
	 */
	private class UserValidator implements Validator{



		@Override
		public void validate(Object value) throws InvalidValueException {
			String login;
			if(value != null){
				 login = value.toString();
				 if(!userDao.isLoginUnique(login))
					 throw new InvalidValueException("Login already exists");
			}
			
		}}
	
}
