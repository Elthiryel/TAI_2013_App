package pl.edu.agh.tai.dropbox.integration.bean;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import pl.edu.agh.tai.dropbox.integration.exception.NoFileSelectedException;
import pl.edu.agh.tai.dropbox.integration.model.DropboxFile;

import com.vaadin.server.FileResource;

@Component
@Scope("session")
public class FileHelperBean {

	@Autowired
	private DropboxManager dropboxManager;

	public FileResource downloadAsFileResource(DropboxFile file)
			throws NoFileSelectedException {
		if (file != null) {
			try {
				
				return new FileResource(dropboxManager.downloadFile(file));

			} catch (Exception e) {
				ErrorRecoverer.recoverError(e);
			}
		}
		throw new NoFileSelectedException();

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
