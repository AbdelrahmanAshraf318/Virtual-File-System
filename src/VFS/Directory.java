package VFS;

import java.util.ArrayList;

public class Directory {
	
	private String directorypath;
	private ArrayList<File> files;
	private ArrayList<Directory> subDirectories;
	private boolean deleted;
	
	
	
	
	public Directory(String directorypath) {
		this.directorypath = directorypath;
		this.files = null;
		this.subDirectories = null;
		this.deleted = false;
	}

	public String getDirectorypath() {
		return directorypath;
	}
	
	public void setDirectorypath(String directorypath) {
		this.directorypath = directorypath;
	}
	
	public void addSubDirectory(Directory dir)
	{
		this.subDirectories.add(dir);
	}
	
	public void addFile(File file)
	{
		this.files.add(file);
	}
	

}
