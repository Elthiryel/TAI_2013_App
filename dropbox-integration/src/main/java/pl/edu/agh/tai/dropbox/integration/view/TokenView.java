package pl.edu.agh.tai.dropbox.integration.view;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import pl.edu.agh.tai.dropbox.integration.bean.SessionData;
import ru.xpoft.vaadin.VaadinView;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@Component
@Scope("prototype")
@VaadinView(TokenView.NAME)
public class TokenView extends CustomComponent implements View {
	
	public static final String NAME = "token";
	
	@Autowired
	private SessionData sessionData;
	
	private VerticalLayout mainLayout = new VerticalLayout();
	
	@PostConstruct
	private void init(){
		setCompositionRoot(mainLayout);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		mainLayout.removeAllComponents();
		setToken(event.getParameters());
		mainLayout.addComponent(new Label(event.getParameters()));
	}

	private void setToken(String parameters) {
		// TODO Auto-generated method stub
		
	}

}
