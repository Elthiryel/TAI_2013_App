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
 * Bean class for Dropbox operations.
 * Simple wrapper of DbxClient for required operations.
 * @author konrad
 *
 */
@Component
@Scope("prototype")
@DependsOn("sessionData")
public class DropboxManager {
	
	/**
	 * registered application name
	 */
	private static final String APP ="TAIApp/1.0";
	
	/**
	 * Dropbox client
	 */
	private DbxClient client;
	
	/**
	 * Initializes Dropbox client.
	 * @param sessionData injected sessionData for user Dropbox token
	 */
	@Autowired
	public DropboxManager(SessionData sessionData) {
		this.client = new DbxClient(new DbxRequestConfig(APP, Locale
				.getDefault().toString()), sessionData.getUser()
				.getDropboxToken());
	}
	
	/**
	 * Gets all files from Dropbox root directory.
	 * @return files from Dropbox root directory
	 * @throws DbxException
	 */
	public Collection<DropboxFile> getFiles() throws DbxException {
		DropboxFile root = new DropboxFile("/", client);
		return root.getChildren();
	}

	
	
	/**
	 * Downloads file from Dropbox.
	 * @param file file to download
	 * @param outputFile file to write data
	 * @return java.io.File class instance containing downloaded file
	 * @throws DbxException
	 * @throws IOException
	 */
	public File downloadFile(File outputFile, DropboxFile file) throws DbxException, IOException {
		client.getFile(file.getPath(), null, new FileOutputStream(outputFile));
		return outputFile;
	}

	/**
	 * Uploads file to Dropbox.
	 * @param inputFile java.io.File instance containing uploaded file
	 * @param parent parent Dropbox directory
	 * @return uploaded Dropbox file
	 * @throws FileNotFoundException
	 * @throws DbxException
	 * @throws IOException
	 */
	public DropboxFile uploadFile(File inputFile, DropboxFile parent)
			throws FileNotFoundException, DbxException, IOException {
		DropboxFile uploadedFile;
		String path;
		String fileName = inputFile.getName();
		
		
		if (parent != null)
			path = parent.getPath() + "/" + fileName;
		else
			path = "/" + fileName;

		com.dropbox.core.DbxEntry.File result = client.uploadFile(path, DbxWriteMode.add(), inputFile.length(),
				new FileInputStream(inputFile));
		uploadedFile = new DropboxFile(result.path, client);
		uploadedFile.setParent(parent);
		return uploadedFile;
	}

	/**
	 * Removes file from Dropbox.
	 * @param file removed file
	 * @throws DbxException
	 */
	public void deleteFile(DropboxFile file) throws DbxException {
		client.delete(file.getPath());
	}

}
