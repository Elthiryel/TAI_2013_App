package pl.edu.agh.tai.dropbox.integration.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import pl.edu.agh.tai.dropbox.integration.component.LogoLayout;
import pl.edu.agh.tai.dropbox.integration.dao.UserDao;
import pl.edu.agh.tai.dropbox.integration.security.SecurityHelper;
import pl.edu.agh.tai.dropbox.integration.view.ErrorView;
import pl.edu.agh.tai.dropbox.integration.view.LoginView;
import pl.edu.agh.tai.dropbox.integration.view.MainView;
import pl.edu.agh.tai.dropbox.integration.view.RegisterView;
import ru.xpoft.vaadin.DiscoveryNavigator;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Main vaadin component. It registers all views and creates site template.
 * {@link UI}
 * @author konrad
 * 
 */
@Component
@Scope("session")
@Theme("diTheme")
public class HomeUI extends UI {
	
	@Autowired
	private UserDao userDao;
	
	private VerticalLayout viewLayout;
	private VerticalLayout mainLayout;
	private HorizontalLayout upperLayout;
	
	private DiscoveryNavigator navigator;
	
	@Override
	protected void init(VaadinRequest request) {
		initLayouts();
		initNavigator();
		initListeners();
	}

	private void initListeners() {
		navigator.addViewChangeListener(new ViewChangeListener() {
			
			@Override
			public boolean beforeViewChange(ViewChangeEvent event) {
				if ((event.getNewView() instanceof MainView) && SecurityHelper.getUser() == null) {
					navigator.navigateTo(LoginView.NAME);
					Notification.show("Access denied!",Notification.Type.ERROR_MESSAGE);
					return false;
				}
				return true;
			}
			
			@Override
			public void afterViewChange(ViewChangeEvent event) {
				
			}
		});
		
	}

	private void initNavigator() {
		navigator = new DiscoveryNavigator(this, viewLayout);
		navigator.addBeanView(LoginView.NAME, LoginView.class);
		navigator.addBeanView(RegisterView.NAME, RegisterView.class);
		navigator.addBeanView(MainView.NAME, MainView.class);
		navigator.navigateTo(LoginView.NAME);
		navigator.setErrorView(new ErrorView());
		setNavigator(navigator);
	}

	private void initLayouts() {
		mainLayout = new VerticalLayout();
		upperLayout = new HorizontalLayout();
		upperLayout.addComponent(new LogoLayout());
		upperLayout.setSpacing(true);
		upperLayout.setMargin(true);
		viewLayout = new VerticalLayout();
		viewLayout.setSizeUndefined();
		viewLayout.setSpacing(true);
		viewLayout.setMargin(true);
		upperLayout.setSizeUndefined();
		mainLayout.setSpacing(true);
		mainLayout.addComponent(upperLayout);
		mainLayout.addComponent(viewLayout);
		mainLayout.setComponentAlignment(viewLayout, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(upperLayout, Alignment.MIDDLE_CENTER);
		setContent(mainLayout);
	
	}
	
	

}
