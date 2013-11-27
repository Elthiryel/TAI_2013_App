package pl.edu.agh.tai.dropbox.integration.component;

import java.io.File;

import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;

/**
 * Layout with upper logo. Extends {@link HorizontalLayout}
 * 
 * @author konrad
 * 
 */
public class LogoLayout extends HorizontalLayout {


	/**
	 * Default constructor. Logo image.
	 */
	public LogoLayout() {
		String basepath = VaadinService.getCurrent().getBaseDirectory()
				.getAbsolutePath();
		FileResource aghResource = new FileResource(new File(basepath
				+ File.separator + "WEB-INF" + File.separator + "images"
				+ File.separator + "logo.png"));
		Image logo = new Image(null, aghResource);
		addComponent(logo);
		setComponentAlignment(logo, Alignment.MIDDLE_LEFT);
		setSizeFull();
	}
}
