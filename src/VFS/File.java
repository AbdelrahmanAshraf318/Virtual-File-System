package VFS;

import java.util.ArrayList;

public class File {
	
	private String filePath;
	public ArrayList<Integer> allocatedBlocks; /** Will hold the indexes of the blocks  **/
	private boolean deleted; /** To check if the file has been deleted or not  **/
	public String fileName;
	
	public File() {
		this.filePath = "";
		this.deleted = false;
		this.fileName = "";
	}
	
	
	public File(String filePath) {
		this.filePath = filePath;
		this.deleted = false;
	}
	
	public String getFilePath() {
		return filePath;
	}
	
	

	public void setFilePath(String filePath) {
		this.filePath = filePath;
		setFileName();
	}
	
	public void addAllocatedBlocks(ArrayList<Integer> allocate) {
		this.allocatedBlocks = allocate;
	}
	
	
	private  void setFileName()
	{
		String temp = "";
		int index = 0;
		

		for (int i = this.filePath.length()-1 ; i >= 0 ; i--) 
		{
			if(this.filePath.charAt(i) == '/')
				break;
			else
				temp += this.filePath.charAt(i);
		}
		for(int i=temp.length()-1 ; i >= 0 ; i--)
		{
			this.fileName += temp.charAt(i);
		}
	}


	public String getFileName() {
		return fileName;
	}


	

}
