package pl.edu.agh.tai.dropbox.integration.view;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import pl.edu.agh.tai.dropbox.integration.component.RegisterLayout;
import ru.xpoft.vaadin.VaadinView;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.CustomComponent;

/**
 * Register view. Extends {@link CustomComponent} implements {@link View}.
 * @author konrad
 *
 */
@Component
@Scope("prototype")
@VaadinView(RegisterView.NAME)
public class RegisterView extends CustomComponent implements View {

	public static final String NAME = "register";
	
	@Autowired
	private RegisterLayout registerLayout;
	
	@PostConstruct
	private void init(){
		setCompositionRoot(registerLayout);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {

	}

}
