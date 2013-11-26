package pl.edu.agh.tai.dropbox.integration.bean;

import java.io.File;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import pl.edu.agh.tai.dropbox.integration.exception.NoFileSelectedException;
import pl.edu.agh.tai.dropbox.integration.model.DropboxFile;

import com.google.gwt.thirdparty.guava.common.io.Files;
import com.vaadin.server.FileResource;

@Component
@Scope("session")
public class FileHelperBean {

	@Autowired
	private DropboxManager dropboxManager;
	private File tempDir;
	
	@PostConstruct
	private void init(){
		tempDir = Files.createTempDir();
		tempDir.setExecutable(true);
		tempDir.setReadable(true);
		tempDir.setWritable(true);
	}
	@PreDestroy
	private void destroy(){
		for(File file : tempDir.listFiles())
			file.delete();
		tempDir.delete();
	}
	
	public FileResource downloadAsFileResource(DropboxFile file)
			throws NoFileSelectedException {
		if (file != null) {
			try {
				File outputFile = initTempFile(file.getName());
				
				return new FileResource(dropboxManager.downloadFile(outputFile, file));

			} catch (Exception e) {
				ErrorRecoverer.recoverError(e);
			}
		}
		throw new NoFileSelectedException();

	}

	private File initTempFile(String name) {
		File temp = new File(tempDir, name);
		temp.setExecutable(true);
		temp.setReadable(true);
		temp.setWritable(true);
		return temp;
	}
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

}
