package pl.edu.agh.tai.dropbox.integration.component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import pl.edu.agh.tai.dropbox.integration.bean.DropboxManager;
import pl.edu.agh.tai.dropbox.integration.bean.SessionData;
import pl.edu.agh.tai.dropbox.integration.model.DropboxFile;
import pl.edu.agh.tai.dropbox.integration.security.Role;
import pl.edu.agh.tai.dropbox.integration.security.SecurityHelper;

import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

@Component
@Scope("prototype")
public class FilesPanel extends Panel {

	@Autowired
	private FilesList fileList;

	@Autowired
	private FileTreeTable fileTreeTable;

	@Autowired
	private SessionData sessionData;

	@Autowired
	private DropboxManager dropboxManager;

	private VerticalLayout mainLayout = new VerticalLayout();
	private HorizontalLayout footerLayout = new HorizontalLayout();
	private Button downloadButton = new Button("Download");
	private Button addButton = new Button("Add");
	private Button removeButton = new Button("Remove");
	private FileDownloader fileDownloader;

	@PostConstruct
	private void init() {
		setCaption("Files");
		setComponents();
		setListeners();
		setContent(mainLayout);
		DbxEntry.WithChildren listing;
		try {
			Collection<DropboxFile> files = dropboxManager.getFiles();
			fileTreeTable.addFiles(files);
		} catch (DbxException e) {
			// TODO Some error handling ;)
			e.printStackTrace();
		}
		//fileDownloader = new FileDownloader(download());
		//fileDownloader.extend(downloadButton);
	}

	private void setListeners() {

	}

	private FileResource download() {

		final DropboxFile file = (DropboxFile) fileTreeTable.getValue();
		if (file != null) {
			try {
				File fileToDownload = new File(file.getName());
				dropboxManager.downloadFile(fileToDownload, file.getPath());
				FileResource fileResource = new FileResource(fileToDownload);
				return fileResource;
			} catch (DbxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return null;

	}

	private void setComponents() {
		footerLayout.setSpacing(true);
		footerLayout.addComponent(downloadButton);
		footerLayout.addComponent(addButton);
		footerLayout.addComponent(removeButton);

		mainLayout.addComponent(fileTreeTable);
		mainLayout.addComponent(footerLayout);

		addButton.setVisible(SecurityHelper.hasRole(Role.ADMIN));
		removeButton.setVisible(SecurityHelper.hasRole(Role.ADMIN));
	}
}
