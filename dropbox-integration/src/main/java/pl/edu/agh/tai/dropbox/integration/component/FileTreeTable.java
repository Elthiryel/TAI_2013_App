package pl.edu.agh.tai.dropbox.integration.component;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import pl.edu.agh.tai.dropbox.integration.model.DropboxFile;
import pl.edu.agh.tai.dropbox.integration.util.ErrorRecoverer;

import com.dropbox.core.DbxException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.ui.Tree.ExpandEvent;
import com.vaadin.ui.Tree.ExpandListener;
import com.vaadin.ui.TreeTable;

@Component
@Scope("prototype")
public class FileTreeTable extends TreeTable {

	private HierarchicalContainer hierachicalContainer;
	private Set<DropboxFile> alreadyExpanedItems = new HashSet<DropboxFile>();

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
		setListeners();

	}

	private void setListeners() {
		addExpandListener(new ExpandListener() {

			@Override
			public void nodeExpand(ExpandEvent event) {
				DropboxFile parent = (DropboxFile) event.getItemId();
				try {
					if (!alreadyExpanedItems.contains(parent)) {
						addChildren(parent, parent.getChildren());
						alreadyExpanedItems.add(parent);
					}
				} catch (DbxException e) {
					ErrorRecoverer.recoverError(e);
				}
			}
		});
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
		sort();
		selectFirst();

	}

	private void setChildParentRelation(DropboxFile file) throws DbxException {
		
		hierachicalContainer.setChildrenAllowed(file, file.isFolder());
		
	}

	public void addFile(DropboxFile newFile) throws DbxException {
		hierachicalContainer.addItem(newFile);
		if (newFile.hasParent())
			hierachicalContainer.setParent(newFile, newFile.getParent());
		setChildParentRelation(newFile);
		setProperties(newFile);
		select(newFile);
	}

	private void addChildren(DropboxFile file, Collection<DropboxFile> children)
			throws DbxException {
		for (DropboxFile child : children) {
			hierachicalContainer.addItem(child);
			hierachicalContainer.setParent(child, file);
			setChildParentRelation(child);
			setProperties(child);
		}

	}

	private void setProperties() {
		for (Object item : hierachicalContainer.getItemIds()) {
			setProperties(item);

		}

	}

	public void selectFirst() {
		if(hierachicalContainer.size()>0)
			select(hierachicalContainer.getItemIds().iterator().next());
	}

	public DropboxFile getSelectedDropboxFile() {
		return (DropboxFile) getValue();
	}

	public void removeFile(DropboxFile selectedDropboxFile) {
		if (hierachicalContainer.containsId(selectedDropboxFile)) {
			Object previous = hierachicalContainer.prevItemId(selectedDropboxFile);
			hierachicalContainer.removeItem(selectedDropboxFile);
			select(previous);
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
