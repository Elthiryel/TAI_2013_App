package pl.edu.agh.tai.dropbox.integration.bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import pl.edu.agh.tai.dropbox.integration.model.DropboxFile;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWriteMode;

/**
 * Bean class for dropbox operation.
 * Simple wrap DbxClient for required operations
 * @author konrad
 *
 */
@Component
@Scope("prototype")
@DependsOn("sessionData")
public class DropboxManager {
	
	/**
	 * Registered application name
	 */
	private static final String APP ="TAIApp/1.0";
	
	/**
	 * Dropobox client
	 */
	private DbxClient client;
	
	/**
	 * Initialize dropbox client
	 * @param sessionData injected sessionData for user dropbox token
	 */
	@Autowired
	public DropboxManager(SessionData sessionData) {
		this.client = new DbxClient(new DbxRequestConfig(APP, Locale
				.getDefault().toString()), sessionData.getUser()
				.getDropboxToken());
	}
	

	public Collection<DropboxFile> getFiles() throws DbxException {
		DropboxFile root = new DropboxFile("/", client);
		return root.getChildren();
	}

	public File downloadFile(DropboxFile file) throws DbxException, IOException {
		File outputFile = new File(file.getName());
		client.getFile(file.getPath(), null, new FileOutputStream(outputFile));
		return outputFile;
	}

	public DropboxFile uploadFile(File inputFile, DropboxFile parent)
			throws FileNotFoundException, DbxException, IOException {
		DropboxFile uploadedFile;
		String path;
		String fileName = inputFile.getName();
		
		
		if (parent != null)
			path = parent.getPath() + "/" + fileName;
		else
			path = "/" + fileName;

		client.uploadFile(path, DbxWriteMode.add(), inputFile.length(),
				new FileInputStream(inputFile));
		uploadedFile = new DropboxFile(path, client);
		uploadedFile.setParent(parent);
		return uploadedFile;
	}

	public void deleteFile(DropboxFile file) throws DbxException {
		client.delete(file.getPath());
	}

}
