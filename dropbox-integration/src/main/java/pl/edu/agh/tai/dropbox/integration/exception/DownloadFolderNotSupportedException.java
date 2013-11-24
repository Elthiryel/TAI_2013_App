package pl.edu.agh.tai.dropbox.integration.exception;

public class DownloadFolderNotSupportedException extends Exception {

	public DownloadFolderNotSupportedException(){
		super("Downloading folder not supported");
	}
}
