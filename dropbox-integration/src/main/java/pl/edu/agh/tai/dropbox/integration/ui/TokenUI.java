package pl.edu.agh.tai.dropbox.integration.ui;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import pl.edu.agh.tai.dropbox.integration.bean.SessionData;
import pl.edu.agh.tai.dropbox.integration.dao.UserDao;
import pl.edu.agh.tai.dropbox.integration.model.User;

import com.dropbox.core.DbxAuthFinish;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;

/**
 * UI for dropbox token request
 * {@link UI}
 * @author konrad
 *
 */
@Component
@Scope("prototype")
public class TokenUI extends UI {

	@Autowired
	private SessionData sessionData;

	@Autowired
	private UserDao userDao;

	private Button redirectButton = new Button("Back to login page");

	@Override
	protected void init(VaadinRequest request) {
		initContent();
		if (request.getParameter("error") != null)
			recoverError();
		else {
			Map<String, String[]> paramMap = request.getParameterMap();
			try {
				DbxAuthFinish authFinish = sessionData.getAuth().finish(paramMap);
				register(authFinish.accessToken);
			} catch (Exception e) {
				Notification notification = new Notification(e.getMessage());
				notification.setDelayMsec(Notification.DELAY_FOREVER);
				notification.show(getPage());
			}
		}

	}

	private void initContent() {
		redirectButton.setSizeUndefined();
		redirectButton.setStyleName(BaseTheme.BUTTON_LINK);
		redirectButton.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				redirectToHome();

			}
		});
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSpacing(true);
		mainLayout.setMargin(true);
		mainLayout.addComponent(redirectButton);
		mainLayout.setComponentAlignment(redirectButton,
				Alignment.MIDDLE_CENTER);
		setContent(mainLayout);

	}

	private void register(String token) {
		User newUser = sessionData.getUser();
		newUser.setDropboxToken(token);
		userDao.create(newUser);
		promptNotification();
	}

	private void promptNotification() {
		Notification notification = new Notification("Registration succeed");
		notification.setDelayMsec(Notification.DELAY_FOREVER);
		notification.show(getPage());
	}

	private void recoverError() {
		Notification.show("Registration failed",
				Notification.Type.ERROR_MESSAGE);

	}

	private void redirectToHome() {
		Page.getCurrent().setLocation(
				"http://localhost:8080/DropboxIntegration/");

	}

}
