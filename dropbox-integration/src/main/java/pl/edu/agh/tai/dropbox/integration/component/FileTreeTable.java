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

/**
 * File table. Extends {@link TreeTable}. Columns: name, size, modification
 * date. Single selectable. Default width 800px. Default page length 20. Sorting
 * enabled, default sorting by name.
 * 
 * @author konrad
 * 
 */
@Component
@Scope("prototype")
public class FileTreeTable extends TreeTable {

	/**
	 * Default width
	 */
	private static final int DEAFULT_WIDTH = 800;

	/**
	 * Default page lenght
	 */
	private static final int DEAFULT_PAGE_LENGHT = 20;

	/**
	 * Item container. {@link HierarchicalContainer}
	 */
	private HierarchicalContainer hierachicalContainer;

	/**
	 * Set of already expanded items
	 */
	private Set<DropboxFile> alreadyExpandedItems = new HashSet<DropboxFile>();

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
		setWidth(DEAFULT_WIDTH, Unit.PIXELS);
		setPageLength(DEAFULT_PAGE_LENGHT);
		setListeners();

	}

	/**
	 * Gets selected item
	 * 
	 * @return Selected DropboxFile
	 */
	public DropboxFile getSelectedDropboxFile() {
		return (DropboxFile) getValue();
	}

	/**
	 * Removes from container selected dropboxfile and selects previous item.
	 * 
	 * @param selectedDropboxFile
	 */
	public void removeFile(DropboxFile selectedDropboxFile) {
		if (hierachicalContainer.containsId(selectedDropboxFile)) {
			Object previous = hierachicalContainer
					.prevItemId(selectedDropboxFile);
			hierachicalContainer.removeItem(selectedDropboxFile);
			select(previous);
		}
	}

	/**
	 * Adds new file to container. Selects new file. Sets parental relation
	 * between items. Sets properties of new item.
	 * 
	 * @param newFile
	 * @throws DbxException
	 */
	public void addFile(DropboxFile newFile) throws DbxException {
		hierachicalContainer.addItem(newFile);
		if (newFile.hasParent())
			hierachicalContainer.setParent(newFile, newFile.getParent());
		setChildParentRelation(newFile);
		setProperties(newFile);
		select(newFile);
	}
	
	/**
	 * Adds files to container. 
	 * Removes existed items in container.
	 * Selects first file. Sets parental relation
	 * between items. Sets properties of new items.
	 * Sorts container.
	 * @param files Collecion of new file.
	 * @throws DbxException
	 */
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

	private void setListeners() {
		addExpandListener(new ExpandListener() {

			@Override
			public void nodeExpand(ExpandEvent event) {
				DropboxFile parent = (DropboxFile) event.getItemId();
				try {
					if (!alreadyExpandedItems.contains(parent)) {
						addChildren(parent, parent.getChildren());
						alreadyExpandedItems.add(parent);
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

	private void setChildParentRelation(DropboxFile file) throws DbxException {

		hierachicalContainer.setChildrenAllowed(file, file.isFolder());

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

	private void selectFirst() {
		if (hierachicalContainer.size() > 0)
			select(hierachicalContainer.getItemIds().iterator().next());
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
