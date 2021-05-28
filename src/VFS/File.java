package VFS;

import java.util.ArrayList;

public class File {
	
	private String filePath;
	private ArrayList<Integer> allocatedBlocks; /** Will hold the indexes of the blocks  **/
	private boolean deleted; /** To check if the file has been deleted or not  **/
	
	public File(String filePath) {
		this.filePath = filePath;
		this.deleted = false;
	}
	
	public String getFilePath() {
		return filePath;
	}
	
	

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public void addAllocatedBlocks(int allocatedBlock) {
		this.allocatedBlocks.add(allocatedBlock);
	}
	

}
