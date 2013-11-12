package pl.edu.agh.tai.dropbox.integration.component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import pl.edu.agh.tai.dropbox.integration.bean.SessionData;
import pl.edu.agh.tai.dropbox.integration.model.User;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxSessionStore;
import com.dropbox.core.DbxStandardSessionStore;
import com.dropbox.core.DbxWebAuth;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.LegacyWindow;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

@Component
@Scope("prototype")
public class RegisterLayout extends FormLayout {

	private Button registerButton = new Button("Register");
	BeanFieldGroup<User> binder = new BeanFieldGroup<User>(User.class);

	@Autowired
	private SessionData sessionData;

	@PostConstruct
	private void init() {

		initUserLayout();
		setListeners();
	}

	private void setListeners() {
		registerButton.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				onRegisterAction();

			}
		});

	}

	private void onRegisterAction() {
		sessionData.setUser(
				binder.getItemDataSource().getBean());
		
		//TODO Add getting dropobox token
		// UI.getCurrent().getPage().setLocation("http://www.dropbox.com");
		DbxRequestConfig requestConfig = new DbxRequestConfig("text-edit/0.1", null);
		// APP_KEY and APP_SECRET should come from static class probably, hardcoded here just for tests
		DbxAppInfo appInfo = new DbxAppInfo("tog0pn54fns1v07", "bag11rqa652hg1s");
		String redirectUri = "http://localhost:8080/DropboxIntegration/#!token";
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession session = attr.getRequest().getSession(true);
		String sessionKey = "dropbox-auth-csrf-token";
		DbxSessionStore csrfTokenStore = new DbxStandardSessionStore(session, sessionKey);
		DbxWebAuth webAuth = new DbxWebAuth(requestConfig, appInfo, redirectUri, csrfTokenStore);
		String authUrl = webAuth.start();
		UI.getCurrent().getPage().setLocation(authUrl);
	}

	private void initUserLayout() {
		User user = new User();
		binder.setItemDataSource(user);
		addComponent(binder.buildAndBind("login"));
		addComponent(binder.buildAndBind("User password", "userPassword", PasswordField.class));
		addComponent(binder.buildAndBind("Admin password", "adminPassword", PasswordField.class));
		addComponent(registerButton);
	}
}
