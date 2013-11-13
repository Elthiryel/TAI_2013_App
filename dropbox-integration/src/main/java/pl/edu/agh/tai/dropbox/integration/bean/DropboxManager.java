package pl.edu.agh.tai.dropbox.integration.bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Locale;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import pl.edu.agh.tai.dropbox.integration.model.DropboxFile;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWriteMode;

@Component
@Scope("session")
public class DropboxManager {

	private DbxClient client;
	
	public DropboxManager(String dropboxToken) {
		this.client = new DbxClient(new DbxRequestConfig("TAIApp/1.0", Locale.getDefault().toString()), dropboxToken);
	}
	
	public Collection<DropboxFile> getFiles() throws DbxException {
		DropboxFile root = new DropboxFile("/", client);
		return root.getChildren();
	}
	
	public void downloadFile(File outputFile, String path) throws DbxException, IOException {
		client.getFile(path, null, new FileOutputStream(outputFile));
	}
	
	public void uploadFile(File inputFile, String path) throws FileNotFoundException, DbxException, IOException {
		client.uploadFile(path, DbxWriteMode.add(), inputFile.length(), new FileInputStream(inputFile));
	}
	
	public void deleteFile(String path) throws DbxException {
		client.delete(path);
	}
	
}
