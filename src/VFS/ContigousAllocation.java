package VFS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

public class ContigousAllocation {
	
	
	static Directory rootDirectory = new Directory();
	
	private	int numOfBlocks;
	private	int consumedBlocks;
	private	int sizeOfBlock;
	private ArrayList<String> dataOfPath = new ArrayList<String>();
	int counter = 0;
	private ArrayList<Integer> emptyBlocks = new ArrayList<Integer>();
	private ArrayList<Integer> keepIndexOfFolder = new ArrayList<Integer>();/** to help in removing folder **/ 
	
	public ContigousAllocation()
	{
		this.numOfBlocks = 0;
		this.consumedBlocks = 0;
		this.sizeOfBlock = 0;
	}
	
	
	
	public ContigousAllocation(int numOfBlocks, int sizeOfBlock) {
		this.numOfBlocks = numOfBlocks;
		this.sizeOfBlock = sizeOfBlock;
		this.consumedBlocks = 0;
	}
	
	
	public void spitTheCommand(String command)
	{
		String restOfPath = "";
		String theCommand = "";
		int index = 0;
		for(int i=0 ; i<command.length() ; i++)
		{
			if(command.charAt(i) == ' ') 
			{
				index = i;
				break;
			}
			else
				theCommand += command.charAt(i);
		}
		
		for(int i=index+1 ; i<command.length() ; i++)
		{
			restOfPath += command.charAt(i);
		}
		if(theCommand.equalsIgnoreCase("CreateFile"))
		{
			System.out.println(restOfPath);
			CreateFile(restOfPath); 
		}
		if(theCommand.equalsIgnoreCase("CreateFolder"))
		{
			System.out.println(restOfPath);
			CreateFolder(restOfPath);
		}
		if(theCommand.equalsIgnoreCase("DeleteFile"))
		{
			DeleteFile(restOfPath);
		}
		if(theCommand.equalsIgnoreCase("DeleteFolder"))
		{
			DeleteFolder(restOfPath);
		}
		if(theCommand.equalsIgnoreCase("DisplayDiskStatus"))
		{
			System.out.print("Empty Blocks: ");
			for(int i=0 ; i<this.numOfBlocks ; i++)
			{
				if(i >= this.emptyBlocks.size())
				{
					System.out.print(0);
				}
				else
				{
					System.out.print(this.emptyBlocks.get(i));
				}
			}
			System.out.println();
			this.counter = 0;
			displayAllocatedBlocks(ContigousAllocation.rootDirectory);
			
		}
		if(theCommand.equalsIgnoreCase("DisplayDiskStructure"))
		{
			this.counter = 0;
			System.out.println("<root>");
			for(int i=0 ; i<ContigousAllocation.rootDirectory.files.size() ; i++)
			{
				System.out.println("    <" + ContigousAllocation.rootDirectory.files.get(i).getFileName() + ">");
			}
			displayDiskStructure(ContigousAllocation.rootDirectory.subDirectories.get(0));
		}
	}
	
	private void displayDiskStructure(Directory dir)
	{
		System.out.println("   <" + dir.getDirName() + ">");
		for(int i=0 ; i<dir.files.size() ; i++)
		{
			System.out.println("    <" + dir.files.get(i).getFileName() + ">");
		}
		for(int i=0 ; i<dir.subDirectories.size() ; i++)
		{
			displayDiskStructure(dir.subDirectories.get(i));
		}
		this.counter++;
		if(this.counter >= ContigousAllocation.rootDirectory.subDirectories.size())
			return;
		else
			displayDiskStructure(ContigousAllocation.rootDirectory.subDirectories.get(this.counter));
	}
	
	
	private void displayAllocatedBlocks(Directory dir)
	{
			for(int i=0 ; i<dir.files.size() ; i++)
			{
				System.out.println(dir.files.get(i).getFilePath() + "  " + 
						dir.files.get(i).allocatedBlocks.get(0) + " " + dir.files.get(i).allocatedBlocks.get(1));
			}
			for(int i=0 ; i<dir.subDirectories.size() ; i++)
			{
				this.counter++;
				displayAllocatedBlocks(dir.subDirectories.get(i));
			}
	}
	
	
	private void CreateFile(String restOfPath)
	 {
		 String Size = "";
		 int index = 0;
		 for(int i=restOfPath.length()-1 ; i>=0 ; i--)
		 {
			 if(restOfPath.charAt(i) == ' ')
			 {
				 index = i;
				 break;
			 }
			 else
				 Size += restOfPath.charAt(i);
		 }
		 String temp = "";
		 for(int i=Size.length()-1 ; i>=0 ; i--)
		 {
			 temp += Size.charAt(i);
		 };
		 int theSizeNeeded = Integer.parseInt(temp);
		 
		 String thePath = "";
		 for(int i=0 ; i<index ; i++)
		 {
			 thePath += restOfPath.charAt(i);
		 }
		 
		 int numOfBlocksNeeded = Math.round(theSizeNeeded / this.sizeOfBlock);
		 if(numOfBlocksNeeded <= (this.numOfBlocks - this.consumedBlocks))
		 {
			 File file = new File();
			 file.setFilePath(thePath);
			 ArrayList<Integer> allocate = new ArrayList<Integer>();
			 allocate.add(this.consumedBlocks);
			 allocate.add(this.consumedBlocks+numOfBlocksNeeded); 
			 file.addAllocatedBlocks(allocate);
			 for(int i=this.consumedBlocks ; i<(this.consumedBlocks+numOfBlocksNeeded) ; i++)
			 {
				 this.emptyBlocks.add(1);
			 }
			 this.consumedBlocks += numOfBlocksNeeded;
			 
			 
			addFile(file , thePath);
		 }
		 else
			 System.out.println("The fileSize is bigger than the available, please try again");
		 
	 }
	
	
	private void splitDataOfPath(String thePath)
	{
		String temp =  "";
		for(int i=0 ; i<thePath.length() ; i++)
		{
			if(thePath.charAt(i) == '/' || thePath.charAt(i) == '&')
			{
				if(thePath.charAt(i) == '&')
				{
					temp += thePath.charAt(i);
					this.dataOfPath.add(temp);
					temp = "";
				}
				else
				{
					this.dataOfPath.add(temp);
					temp = "";
				}
			}
			
			else
				temp += thePath.charAt(i);
		}
		
		
	}
	
	
	
	public void addFile(File file , String thePath)
	{
		this.dataOfPath = new ArrayList<String>();
		this.counter = 0;
		this.counter++;
		thePath += '/';
		splitDataOfPath(thePath);
		addFileHelper(file, this.dataOfPath.get(this.counter) , ContigousAllocation.rootDirectory);
	}
	
	public void addFolder(Directory folder , String path)
	{
		this.dataOfPath = new ArrayList<String>();
		this.counter = 0;
		this.counter++;
		path += '&';
		splitDataOfPath(path);
		addFolderHelper(folder, this.dataOfPath.get(this.counter) , ContigousAllocation.rootDirectory);
	}
	
	private void addFileHelper(File file , String path , Directory dir)
	{
		int isFound = path.indexOf(".txt");
		if(isFound != -1)
		{
			for(int j=0 ; j<dir.files.size() ; j++)
			{
				if(dir.files.get(j).getFileName().equals(path))
				{
					System.out.println("The file is already exist");
					return;
				}
			}
			dir.files.add(file);
			System.out.println("The file is created successfully.......");
			return;
		}
		else if(isFound == -1)/** It's a directory **/
		{
			for(int i=0 ; i<dir.subDirectories.size() ; i++)
			{
				if(dir.subDirectories.get(i).getDirName().equals(path))
				{
					this.counter++;
					addFileHelper(file , this.dataOfPath.get(this.counter) , dir.subDirectories.get(i));
				}
				if(!dir.subDirectories.get(i).getDirName().equals(path) && i == dir.subDirectories.size()-1)
				{
					System.out.println("You entered unfounded directory");
					return;
				}
			}
		}
	}
	
	private void addFolderHelper(Directory folder , String path , Directory dir)
	{
		int isFound = path.indexOf("&");
		if(isFound != -1)
		{
			String newPath = "";
			for(int i=0 ; i<path.length()-1 ; i++)
			{
				newPath += path.charAt(i);
			}
			for(int j=0 ; j<dir.subDirectories.size() ; j++)
			{
				if(dir.subDirectories.get(j).getDirName().equals(newPath))
				{
					System.out.println("The directory is already exist");
					return;
				}
			}
			folder.setDirectoryName(newPath); 
			dir.subDirectories.add(folder);
			System.out.println("The folder is created successfully.......");
			return;
		}
		else if(isFound == -1)/** It's a subDirectory **/
		{
			for(int i=0 ; i<dir.subDirectories.size() ; i++)
			{
				if(dir.subDirectories.get(i).getDirName().equals(path))
				{
					this.counter++;
					addFolderHelper(folder , this.dataOfPath.get(this.counter) , dir.subDirectories.get(i));
				}
				else if(!dir.subDirectories.get(i).getDirName().equals(path) && i == dir.subDirectories.size()-1)
				{
					System.out.println("You entered unfounded directory");
					return;
				}
			}
		}
	}
	
	
	

	    
	    
	  private void CreateFolder(String restOfPath)
	  {
			 
			 Directory folder = new Directory();
			
			 folder.setDirectoryPath(restOfPath);
			
		   addFolder(folder , restOfPath);
			 
	  }

	  private void DeleteFile(String restOfPath)
	  {
		  deleteFile(restOfPath);
	  }
	  
	  private void deleteFile(String restOfPath)
	  {
		  this.dataOfPath = new ArrayList<String>();
		  this.counter = 0;
		  this.counter++;
		  restOfPath += '/';
		  splitDataOfPath(restOfPath);
		  deleteFileHelper(this.dataOfPath.get(this.counter) , ContigousAllocation.rootDirectory); 
	  }
	  
	  private void deleteFileHelper(String restOfPath , Directory dir)
	  {
		  int isFile = restOfPath.indexOf(".txt");
		  if(isFile != -1)/** Reaches the end of the path **/
		  {
			  for(int i=0 ; i<dir.files.size() ; i++)
			  {
				  if(dir.files.get(i).getFileName().equals(restOfPath))
				  {
					  /** Release the consumedBlocks **/
					  for(int j=dir.files.get(i).allocatedBlocks.get(0) ; 
							  j<dir.files.get(i).allocatedBlocks.get(1) ; j++)
					  {
						  this.emptyBlocks.set(j, 0);
						  this.consumedBlocks--;
					  }
					  System.out.println("The file is deleted successfully....");
					  return;
				  }
			  }
			  System.out.println("You entered a wrong path of the file please try again");
			  return;
		  }
		  if(isFile == -1)/** it's a directory **/
		  {
			  for(int i=0 ; i<dir.subDirectories.size() ; i++)
			  {
				  if(dir.subDirectories.get(i).getDirName().equals(restOfPath))
				  {
					  this.counter++;
					  System.out.println(this.counter);
					  deleteFileHelper(this.dataOfPath.get(this.counter) , 
							  dir.subDirectories.get(i));
				  }
				  else if(!dir.subDirectories.get(i).getDirName().equals(restOfPath) && 
						  i == dir.subDirectories.size()-1)
				  {
					  System.out.println("You enterd a wrong path");
					  return;
				  }
			  }
		  }
	  }
	    
	 private void DeleteFolder(String restOfPath)
	{
		deleteFolder(restOfPath);
	}
		
	 private void deleteFolder(String path)
	{
		path += '&';
		splitDataOfPath(path);
		this.counter = 0;
		this.counter = this.dataOfPath.size()-1;
		String temp = "";
		for(int i=0 ; i<this.dataOfPath.get(this.counter).length()-1 ; i++)
		{
			temp += this.dataOfPath.get(this.counter).charAt(i);
		}
		deleteFolderHelper(temp , ContigousAllocation.rootDirectory.subDirectories.get(0));
	}
	 
	 private void deleteTheDirectoryItself(Directory dir , Directory root)
	 {
		 
			 for(int i=0 ; i<root.subDirectories.size() ; i++)
			 {
				 if(dir.getDirName().equals(root.subDirectories.get(i).getDirName()))
				 {
					 root.subDirectories.remove(i);
					 return;
				 }
			 }
	 }
	 
	 private void deleteFolderHelper(String path , Directory dir)
	 {
		 if(dir.getDirName().equals(path))//base case
		 {
			 deleteAllDataInFolder(0 , dir);
			 this.counter = 0;
			 deleteTheDirectoryItself(dir , ContigousAllocation.rootDirectory);
			 return;
		 }
		
		 else
		 {
			for(int i=0 ; i<dir.subDirectories.size() ; i++)
			{
				deleteFolderHelper(path , dir.subDirectories.get(i));
			}
			System.out.println("There is no folder with this path");
			return;
		 }
	 }
	 
	 private void deleteAllDataInFolder(int counting ,  Directory dir)
	 {
			 for(int i=0 ; i<dir.files.size() ; i++)/** delete all files in the folder **/
			 {
				 for(int j=dir.files.get(i).allocatedBlocks.get(0) ; 
						 j<dir.files.get(i).allocatedBlocks.get(1) ; j++)
				 {
					 this.emptyBlocks.set(j, 0);
					 this.consumedBlocks--;
				 }
				 dir.files.get(i).allocatedBlocks.clear();
			 }
			 for(int i=0 ; i<dir.subDirectories.size() ; i++)
			 {
				 deleteAllDataInFolder(counting , dir.subDirectories.get(i));
			 }
			
	 }
	 
	 
	 
}
