package pl.edu.agh.tai.dropbox.integration.view;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import pl.edu.agh.tai.dropbox.integration.component.FilesPanel;
import pl.edu.agh.tai.dropbox.integration.security.Role;
import ru.xpoft.vaadin.VaadinView;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.CustomComponent;

@Component
@Scope("prototype")
@VaadinView(MainView.NAME)
@Secured(Role.USER)
public class MainView extends CustomComponent implements View {

	public static final String NAME = "main";
	
	@Autowired
	private FilesPanel filesPanel;
	
	@PostConstruct
	private void init(){
		setCompositionRoot(filesPanel);
	}
	@Override
	public void enter(ViewChangeEvent event) {

	}

}
