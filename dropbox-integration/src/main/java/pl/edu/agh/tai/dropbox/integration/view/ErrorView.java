package pl.edu.agh.tai.dropbox.integration.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

public class ErrorView extends CustomComponent implements View {

	@Override
	public void enter(ViewChangeEvent event) {
		Notification.show("Wrong URL",Notification.Type.ERROR_MESSAGE);
		UI.getCurrent().getNavigator().navigateTo(MainView.NAME);

	}

}
