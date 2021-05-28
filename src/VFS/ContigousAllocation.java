package VFS;

import java.util.ArrayList;

public class ContigousAllocation {
	
	private ArrayList<File> theFiles;
	private ArrayList<Directory> directories;
	private int numOfBlocks;
	private int sizeOfBlock;
	private String rootDirectory;
	
	
	public ContigousAllocation(int numOfBlocks, int sizeOfBlock) {
		this.numOfBlocks = numOfBlocks;
		this.sizeOfBlock = sizeOfBlock;
		this.rootDirectory = "root";
	}

	public int getNumOfBlocks() {
		return numOfBlocks;
	}
	
	public void setNumOfBlocks(int numOfBlocks) {
		this.numOfBlocks = numOfBlocks;
	}
	
	public int getSizeOfBlock() {
		return sizeOfBlock;
	}
	
	public void setSizeOfBlock(int sizeOfBlock) {
		this.sizeOfBlock = sizeOfBlock;
	}
	
	public void CreateFile(String creation)/** Calling function parse to split the string the user enter **/
	{
		
	}
	
}
