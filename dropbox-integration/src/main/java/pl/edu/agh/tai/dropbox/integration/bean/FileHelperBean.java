package pl.edu.agh.tai.dropbox.integration.bean;

import java.io.File;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import pl.edu.agh.tai.dropbox.integration.exception.NoFileSelectedException;
import pl.edu.agh.tai.dropbox.integration.model.DropboxFile;
import pl.edu.agh.tai.dropbox.integration.util.ErrorRecoverer;

import com.google.gwt.thirdparty.guava.common.io.Files;
import com.vaadin.server.FileResource;

/**
 * Session bean class. Helper for file operations. Provides required conversions
 * and types.
 * 
 * @author konrad
 * 
 */
@Component
@Scope("session")
public class FileHelperBean {

	/**
	 * DropboxManager for operation on dropbox service.
	 */
	@Autowired
	private DropboxManager dropboxManager;

	/**
	 * Temporary directory for downloaded files. Created when bean is
	 * initialized. Removed when bean is destroyed.
	 */
	private File tempDir;

	/**
	 * Initialization of bean Creation of temporary directory.
	 */
	@PostConstruct
	private void init() {
		tempDir = Files.createTempDir();
		tempDir.setExecutable(true);
		tempDir.setReadable(true);
		tempDir.setWritable(true);
	}

	/**
	 * Cleans and delete temporary directory before bean destruction.
	 */
	@PreDestroy
	private void destroy() {
		for (File file : tempDir.listFiles())
			file.delete();
		tempDir.delete();
	}

	/**
	 * Downloads file from dropbox and converting it to FileResource.
	 * 
	 * @param file
	 *            DropboxFile to download
	 * @return FileResource created from downloaded file
	 * @throws NoFileSelectedException
	 *             when selected file is null
	 */
	public FileResource downloadAsFileResource(DropboxFile file)
			throws NoFileSelectedException {
		if (file != null) {
			try {
				File outputFile = initTempFile(file.getName());

				return new FileResource(dropboxManager.downloadFile(outputFile,
						file));

			} catch (Exception e) {
				ErrorRecoverer.recoverError(e);
			}
		}
		throw new NoFileSelectedException();

	}
	
	/**
	 * Uploads new file to dropbox.
	 * 
	 * @param target parent/selected file/place to upload new file
	 * @param file file to upload
	 * @return DropboxFile witch uploaded content
	 */
	public DropboxFile uploadToParent(DropboxFile target, File file) {

		try {
			if (target != null) {
				if (target.isFolder()) {
					return dropboxManager.uploadFile(file, target);

				} else {
					DropboxFile parent = target.getParent();
					if (parent != null)
						return dropboxManager.uploadFile(file, parent);
				}
			}
			return dropboxManager.uploadFile(file, null);
		} catch (Exception e) {
			ErrorRecoverer.recoverError(e);
		}
		return null;

	}

	private File initTempFile(String name) {
		File temp = new File(tempDir, name);
		temp.setExecutable(true);
		temp.setReadable(true);
		temp.setWritable(true);
		return temp;
	}

}
