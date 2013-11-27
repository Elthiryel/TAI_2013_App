package pl.edu.agh.tai.dropbox.integration.view;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import pl.edu.agh.tai.dropbox.integration.bean.SessionData;
import pl.edu.agh.tai.dropbox.integration.component.LoginLayout;
import ru.xpoft.vaadin.VaadinView;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.CustomComponent;

/**
 * Login view. Extends {@link CustomComponent} implements {@link View}.
 * @author konrad
 *
 */
@Component
@Scope("prototype")
@VaadinView(LoginView.NAME)
public class LoginView  extends CustomComponent implements View {
	
	public static final String NAME = "";
	
	@Autowired
	private LoginLayout loginLayout;
	
	@Autowired
	private SessionData sessionData;
	
	@PostConstruct
	private void init(){
		setCompositionRoot(loginLayout);
	}
	
	/**
	 * On enter clears session data
	 */
	@Override
	public void enter(ViewChangeEvent event) {
		SecurityContextHolder.clearContext();
		sessionData.clearData();
	}

}
