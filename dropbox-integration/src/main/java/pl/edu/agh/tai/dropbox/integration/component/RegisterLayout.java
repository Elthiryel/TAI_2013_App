package pl.edu.agh.tai.dropbox.integration.component;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import pl.edu.agh.tai.dropbox.integration.bean.SessionData;
import pl.edu.agh.tai.dropbox.integration.model.User;

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
