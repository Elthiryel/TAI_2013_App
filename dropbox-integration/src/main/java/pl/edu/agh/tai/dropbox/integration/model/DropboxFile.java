package pl.edu.agh.tai.dropbox.integration.model;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;

/**
 * Class representing Dropbox file.
 * @author elthiryel
 */
public class DropboxFile {

	private DbxClient client;
	private DbxEntry entry;
	private DbxEntry.File file;
	private DbxEntry.WithChildren withChildren;
	
	private DropboxFile parent;

	/**
	 * Creates the instance representing a Dropbox file.
	 * @param path full Dropbox file path
	 * @param client Dropbox client
	 * @throws DbxException
	 */
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

	/**
	 * Returns file name (last part of the path).
	 * @return file name
	 */
	public String getName() {
		return entry.name;
	}

	/**
	 * Returns full file path.
	 * @return file path
	 */
	public String getPath() {
		return entry.path;
	}

	/**
	 * Returns true if this file is regular (non-directory), otherwise returns false.
	 * @return true if file is regular, otherwise false
	 */
	public boolean isFile() {
		return entry.isFile();
	}

	/**
	 * Returns true if this file is a directory, otherwise returns false.
	 * @return true if file is a directory, otherwise false
	 */
	public boolean isFolder() {
		return entry.isFolder();
	}

	/**
	 * Returns file size in human-readable format.
	 * @return file size in human-readable format
	 */
	public String getSize() {
		if (file != null) {
			return file.humanSize;
		} else {
			return "";
		}
	}

	/**
	 * Returns file last modification time.
	 * @return file last modification time
	 */
	public Date getModificationTime() {
		if (file != null) {
			return file.lastModified;
		} else {
			return null;
		}
	}

	/**
	 * Returns immediate children of this file.
	 * @return immediate children of this file
	 * @throws DbxException
	 */
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
	
	/**
	 * Returns parent directory (file) of this file.
	 * @return parent directory
	 */
	public DropboxFile getParent() {
		return parent;
	}

	/**
	 * Sets parent directory (file) of this file.
	 * @param parent directory
	 */
	public void setParent(DropboxFile parent) {
		this.parent = parent;
	}
	
	/**
	 * Returns true if this file has a parent set, otherwise returns false.
	 * @return true if this file has a parent set, otherwise false
	 */
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
