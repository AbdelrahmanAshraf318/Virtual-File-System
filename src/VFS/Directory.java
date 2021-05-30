package VFS;

import java.util.ArrayList;
import java.util.LinkedList;

public class Directory {
	
	
	
	private String directoryPath;
	public ArrayList<File> files;
	public ArrayList<Directory> subDirectories;
	private boolean deleted = false;
	private ArrayList<Integer> allocatedBlocks;
	private String dirName;
	
	
	
	
	static int counter = 0;
	
	
	public Directory() {
		this.directoryPath = "";
		this.files = new ArrayList<File>();
		this.subDirectories = new ArrayList<Directory>();
		this.deleted = false;
		this.allocatedBlocks = new ArrayList<Integer>();
		this.dirName = "";
	}
	
	public Directory(String directorypath) {
		this.directoryPath = directorypath;
		this.files = null;
		this.subDirectories = null;
		this.deleted = false;
	}
	
	public String getDirName()
	{
		return this.dirName;
	}
	
	
	public String getDirectoryPath() {
		return directoryPath;
	}

	public void setDirectoryPath(String directoryPath) {
		this.directoryPath = directoryPath;
		setDirName();
	}

	private  void setDirName()
	{
		String temp = "";
		int index = 0;
		

		for (int i = this.directoryPath.length()-1 ; i >= 0 ; i--) 
		{
			
			if(this.directoryPath.charAt(i) == '/')
				break;
			else
				temp += this.directoryPath.charAt(i);
		}
		for(int i=temp.length()-1 ; i >= 0 ; i--)
		{
			this.dirName += temp.charAt(i);
		}
	}
	
	
	public void setDirectoryName(String Name)
	{
		this.dirName = Name;
	}
		
	public void addAllocatedBlocks(ArrayList<Integer> allocate) {
		this.allocatedBlocks = allocate;
	}

	
	
		
}
