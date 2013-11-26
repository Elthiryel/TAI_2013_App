package pl.edu.agh.tai.dropbox.integration.component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import pl.edu.agh.tai.dropbox.integration.util.ErrorRecoverer;

import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;

/**
 * File uploader extends {@link Upload} implements {@link Receiver}.
 * Provides mechanism of uploading file to server.
 * @author konrad
 *
 */
@Component
@Scope("prototype")
public class FileUploader extends Upload implements Receiver {
	
	private File file;
	
	@PostConstruct
	private void init(){
		setImmediate(true);
		setButtonCaption("Add");
		setReceiver((Receiver)this);
	}

	@Override
	public OutputStream receiveUpload(String filename, String mimeType) {
        FileOutputStream fos = null; 
        if(file != null)
        	file.delete();
        try {
        	
            file = new File(filename);
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            ErrorRecoverer.recoverError(e);
            return null;
        }
        return fos; 
    }
	
	/**
	 * Gets uploaded file
	 * @return File - uploaded file
	 */
	public File getFile() {
		return file;
	}
	
	
}
