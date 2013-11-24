package pl.edu.agh.tai.dropbox.integration.component;

import java.util.Collection;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import pl.edu.agh.tai.dropbox.integration.model.DropboxFile;

import com.vaadin.ui.ListSelect;

/**
 * Class used on first metting for simple dropbox file listing.
 * Instead of this, use {@link FileTreeTable}
 * @author konrad
 * @deprecated
 */
@Component
@Scope("prototype")
@Deprecated
public class FilesList extends ListSelect {

	@PostConstruct
	private void init(){
		setCaption("Files");
		setSizeFull();
		setImmediate(true);
	}
	
	/**
	 * Add files to list component
	 * @deprecated
	 * @param files
	 */
	public void addFiles(Collection<DropboxFile> files){
		removeAllItems();
		for(DropboxFile file : files){
			addItem(file);
			setItemCaption(file, file.getName());
		}
	}
}
