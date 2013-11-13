package pl.edu.agh.tai.dropbox.integration.component;

import java.util.Collection;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.dropbox.core.DbxEntry;
import com.vaadin.ui.ListSelect;

@Component
@Scope("prototype")
public class FilesList extends ListSelect {

	@PostConstruct
	private void init(){
		setCaption("Files");
		setSizeFull();
		setImmediate(true);
	}
	
	public void addFiles(Collection<DbxEntry> files){
		removeAllItems();
		for(DbxEntry file : files){
			addItem(file.name);
		}
	}
}
