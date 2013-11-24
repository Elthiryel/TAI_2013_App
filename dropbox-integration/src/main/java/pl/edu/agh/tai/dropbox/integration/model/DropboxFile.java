package pl.edu.agh.tai.dropbox.integration.model;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;

public class DropboxFile {

	private DbxClient client;
	private DbxEntry entry;
	private DbxEntry.File file;
	private DbxEntry.WithChildren withChildren;
	
	private DropboxFile parent;

	public DropboxFile(String path, DbxClient client) throws DbxException {
		System.out.println(path);
		this.client = client;
		this.withChildren = client.getMetadataWithChildren(path);
		this.entry = this.withChildren.entry;
		if (this.entry.isFile()) {
			this.file = this.entry.asFile();
		} else {
			this.file = null;
		}
		
	}

	public String getName() {
		return entry.name;
	}

	public String getPath() {
		return entry.path;
	}

	public boolean isFile() {
		return entry.isFile();
	}

	public boolean isFolder() {
		return entry.isFolder();
	}

	public String getSize() {
		if (file != null) {
			return file.humanSize;
		} else {
			return "";
		}
	}

	public Date getModificationTime() {
		if (file != null) {
			return file.lastModified;
		} else {
			return null;
		}
	}

	public Collection<DropboxFile> getChildren() throws DbxException {
		List<DropboxFile> list = new LinkedList<DropboxFile>();
		DropboxFile file;
		for (DbxEntry e : withChildren.children) {
			file = new DropboxFile(e.path, client);
			file.setParent(this);
			list.add(file);
		}
		return list;
	}
	
	
	public DropboxFile getParent() {
		return parent;
	}

	public void setParent(DropboxFile parent) {
		this.parent = parent;
	}
	
	public boolean hasParent(){
		if(parent == null)
			return false;
		return true;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DropboxFile) {
			DropboxFile df = (DropboxFile) obj;
			return this.getPath().equals(df.getPath());
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return this.getPath().hashCode();
	}

}
