package pl.edu.agh.tai.dropbox.integration.component;

import java.util.LinkedList;
import java.util.Locale;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import pl.edu.agh.tai.dropbox.integration.bean.SessionData;
import pl.edu.agh.tai.dropbox.integration.security.Role;
import pl.edu.agh.tai.dropbox.integration.security.SecurityHelper;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

@Component
@Scope("prototype")
public class FilesPanel extends Panel {
	

	
	@Autowired
	private FilesList fileList;
	
	private VerticalLayout mainLayout = new VerticalLayout();
	private HorizontalLayout footerLayout = new HorizontalLayout();
	private Button downloadButton = new Button("Download");
	private Button addButton = new Button("Add");
	private Button removeButton = new Button("Remove");
	
	@PostConstruct
	private void init(){
		setCaption("Files");
		setComponents();
		setListeners();
		setContent(mainLayout);
//		DbxClient client = null; new DbxClient(new DbxRequestConfig("TAIApp/1.0", Locale.getDefault().toString(), 
//				sessionData.getUser().getDropboxToken());
//		DbxEntry.WithChildren listing;
//		try {
//			listing = client.getMetadataWithChildren("/");
//			LinkedList<DbxEntry> files = new LinkedList<DbxEntry>();
//			for (DbxEntry child : listing.children) {
//			    files.add(child);
//			}
//			fileList.addFiles(files);
//		} catch (DbxException e) {
//			// TODO Some error handling ;)
//			e.printStackTrace();
//		}

	}

	private void setListeners() {
		// TODO Auto-generated method stub
		
	}

	private void setComponents() {
		footerLayout.setSpacing(true);
		footerLayout.addComponent(downloadButton);
		footerLayout.addComponent(addButton);
		footerLayout.addComponent(removeButton);
		
		mainLayout.addComponent(fileList);
		mainLayout.addComponent(footerLayout);
		
		addButton.setVisible(SecurityHelper.hasRole(Role.ADMIN));
		removeButton.setVisible(SecurityHelper.hasRole(Role.ADMIN));
	}
}
