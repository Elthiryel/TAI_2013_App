package pl.edu.agh.tai.dropbox.integration.component;

import java.util.Collection;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import pl.edu.agh.tai.dropbox.integration.model.DropboxFile;

import com.dropbox.core.DbxException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.ui.TreeTable;

@Component
@Scope("prototype")
public class FileTreeTable extends TreeTable {

	private HierarchicalContainer hierachicalContainer;

	@PostConstruct
	private void init() {
		hierachicalContainer = new HierarchicalContainer();
		initContainerProperties();
		setContainerDataSource(hierachicalContainer);
		setSortContainerPropertyId("name");
		setSortEnabled(true);
		setSelectable(true);
		setMultiSelect(false);
		setImmediate(true);
		setWidth(800, Unit.PIXELS);
		setPageLength(20);

	}

	private void initContainerProperties() {

		hierachicalContainer.addContainerProperty("name", String.class, "");
		hierachicalContainer.addContainerProperty("size", String.class, "");
		hierachicalContainer.addContainerProperty("modificationTime",
				Date.class, null);

	}

	public void addFiles(Collection<DropboxFile> files) throws DbxException {
		hierachicalContainer.removeAllItems();
		for (DropboxFile file : files) {
			hierachicalContainer.addItem(file);
			setChildParentRelation(file);
		}
		setProperties();
		selectFirst();
		sort();

	}

	private void setChildParentRelation(DropboxFile file) throws DbxException {
		if (file.isFolder()) {
			hierachicalContainer.setChildrenAllowed(file, true);
			addChildren(file, file.getChildren());
		} else
			hierachicalContainer.setChildrenAllowed(file, false);
	}

	public void addFile(DropboxFile newFile) throws DbxException {
		hierachicalContainer.addItem(newFile);
		if (newFile.hasParent())
			hierachicalContainer.setParent(newFile, newFile.getParent());
		setChildParentRelation(newFile);
		setProperties(newFile);
	}

	private void addChildren(DropboxFile file, Collection<DropboxFile> children)
			throws DbxException {
		for (DropboxFile child : children) {
			hierachicalContainer.addItem(child);
			hierachicalContainer.setParent(child, file);
			setChildParentRelation(child);
		}

	}

	private void setProperties() {
		for (Object item : hierachicalContainer.getItemIds()) {
			setProperties(item);

		}

	}

	public void selectFirst() {
		select(hierachicalContainer.getItemIds().iterator().next());
	}

	public DropboxFile getSelectedDropboxFile() {
		return (DropboxFile) getValue();
	}

	public void removeFile(DropboxFile selectedDropboxFile) {
		if (hierachicalContainer.containsId(selectedDropboxFile)) {
			hierachicalContainer.removeItem(selectedDropboxFile);
		}
	}

	private void setProperties(Object item) {
		BeanItem<DropboxFile> beanItem = new BeanItem<DropboxFile>(
				(DropboxFile) item);
		for (Object property : hierachicalContainer.getContainerPropertyIds()) {
			hierachicalContainer.getItem(item).getItemProperty(property)
					.setValue(beanItem.getItemProperty(property).getValue());
		}
	}
}
