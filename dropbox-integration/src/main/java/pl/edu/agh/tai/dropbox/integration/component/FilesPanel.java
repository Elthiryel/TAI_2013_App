package pl.edu.agh.tai.dropbox.integration.component;

import java.util.Collection;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import pl.edu.agh.tai.dropbox.integration.bean.DropboxManager;
import pl.edu.agh.tai.dropbox.integration.bean.ErrorRecoverer;
import pl.edu.agh.tai.dropbox.integration.bean.FileHelperBean;
import pl.edu.agh.tai.dropbox.integration.bean.SessionData;
import pl.edu.agh.tai.dropbox.integration.exception.NoFileSelectedException;
import pl.edu.agh.tai.dropbox.integration.model.DropboxFile;
import pl.edu.agh.tai.dropbox.integration.security.Role;
import pl.edu.agh.tai.dropbox.integration.security.SecurityHelper;

import com.dropbox.core.DbxException;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FileDownloader;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;

@Component
@Scope("prototype")
public class FilesPanel extends Panel {

	@Autowired
	private FileHelperBean fileHelperBean;

	@Autowired
	private FileTreeTable fileTreeTable;

	@Autowired
	private SessionData sessionData;

	@Autowired
	private DropboxManager dropboxManager;

	@Autowired
	private FileUploader fileUpload;

	private VerticalLayout mainLayout = new VerticalLayout();
	private HorizontalLayout footerLayout = new HorizontalLayout();
	private Button downloadButton = new Button("Download");
	private Button removeButton = new Button("Remove");
	private FileDownloader fileDownloader;

	@PostConstruct
	private void init() {
		setCaption("Files");
		setComponents();
		setContent(mainLayout);
		setListeners();
		getFiles();
		initDownloader();
		
	}

	private void initDownloader() {

		try {
			fileDownloader = new FileDownloader(
					fileHelperBean.downloadAsFileResource(fileTreeTable
							.getSelectedDropboxFile()));
			fileDownloader.extend(downloadButton);
			fileDownloader.beforeClientResponse(true);
		} catch (NoFileSelectedException e) {
			downloadButton.setEnabled(false);
		}

	}

	private void getFiles() {
		try {
			Collection<DropboxFile> files = dropboxManager.getFiles();
			fileTreeTable.addFiles(files);
		} catch (DbxException e) {
			e.printStackTrace();
		}
	}

	private void setListeners() {

		downloadButton.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				download();
			}
		});

		fileUpload.addSucceededListener(new SucceededListener() {

			@Override
			public void uploadSucceeded(SucceededEvent event) {
				try {
					fileTreeTable.addFile(fileHelperBean.uploadToParent(
							fileTreeTable.getSelectedDropboxFile(),
							fileUpload.getFile()));
				} catch (DbxException e) {
					e.printStackTrace();
					ErrorRecoverer.recoverError(e);
				}
			}
		});

		removeButton.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				DropboxFile selectedFile = fileTreeTable
						.getSelectedDropboxFile();
				try {
					if(selectedFile != null){
						dropboxManager.deleteFile(selectedFile);
						fileTreeTable.removeFile(selectedFile);
					}
				} catch (DbxException e) {
					ErrorRecoverer.recoverError(e);
				}

			}
		});
		
		fileTreeTable.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				DropboxFile file = fileTreeTable.getSelectedDropboxFile();
				if(file != null){
					downloadButton.setEnabled(file.isFile());
					removeButton.setEnabled(file.isFile());
				}
				else{
					downloadButton.setEnabled(false);
					removeButton.setEnabled(false);
				}
				
				if(fileDownloader == null)
					initDownloader();
			}
		});
	}

	private void setComponents() {
		downloadButton.setImmediate(true);
		footerLayout.setSpacing(true);
		footerLayout.addComponent(downloadButton);
		footerLayout.addComponent(fileUpload);
		footerLayout.addComponent(removeButton);
		mainLayout.setSpacing(true);
		mainLayout.addComponent(fileTreeTable);
		mainLayout.addComponent(footerLayout);

		fileUpload.setVisible(SecurityHelper.hasRole(Role.ADMIN));
		removeButton.setVisible(SecurityHelper.hasRole(Role.ADMIN));
	}

	private void download() {
		try {
			DropboxFile fileToDownload = fileTreeTable.getSelectedDropboxFile();
			fileDownloader.setFileDownloadResource(fileHelperBean
					.downloadAsFileResource(fileToDownload));
		} catch (Exception e) {
			ErrorRecoverer.recoverError(e);
			fileDownloader.setFileDownloadResource(null);
		}
	}
}
