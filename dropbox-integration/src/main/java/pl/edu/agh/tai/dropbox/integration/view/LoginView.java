package pl.edu.agh.tai.dropbox.integration.view;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import pl.edu.agh.tai.dropbox.integration.component.LoginLayout;
import ru.xpoft.vaadin.VaadinView;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.CustomComponent;

@Component
@Scope("prototype")
@VaadinView(LoginView.NAME)
public class LoginView  extends CustomComponent implements View {
	
	public static final String NAME = "login";
	
	@Autowired
	private LoginLayout loginLayout;
	
	@PostConstruct
	private void init(){
		setCompositionRoot(loginLayout);
	}
	@Override
	public void enter(ViewChangeEvent event) {
		

	}

}
