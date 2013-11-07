package pl.edu.agh.tai.dropbox.integration.view;

import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import pl.edu.agh.tai.dropbox.integration.security.Role;
import ru.xpoft.vaadin.VaadinView;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

@Component
@Scope("prototype")
@VaadinView(MainView.NAME)
@Secured(Role.USER)
public class MainView implements View {

	public static final String NAME = "main";
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

}
