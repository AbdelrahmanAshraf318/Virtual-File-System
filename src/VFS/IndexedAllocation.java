package VFS;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class IndexedAllocation {
	
	static Directory rootDirectory = new Directory();
	
	private	int numOfBlocks;
	private	int consumedBlocks;
	private	int sizeOfBlock;
	int counter3 = 0; /** to help in storing the index of the file in the right place **/
	private ArrayList<String> dataOfPath = new ArrayList<String>();
	int counter = 0;
	private ArrayList<Integer> emptyBlocks = new ArrayList<Integer>();
	private int consumed = 0;
	
	public IndexedAllocation()
	{
		this.numOfBlocks = 0;
		this.consumedBlocks = 0;
		this.sizeOfBlock = 0;
	}
	
	
	
	public IndexedAllocation(int numOfBlocks, int sizeOfBlock){
		this.numOfBlocks = numOfBlocks;
		this.sizeOfBlock = sizeOfBlock;
		this.consumedBlocks = 0;
		for(int i=0 ; i<this.numOfBlocks ; i++)
		{
			this.emptyBlocks.add(0);
		}
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
			CreateFile(restOfPath); 
		}
		if(theCommand.equalsIgnoreCase("CreateFolder"))
		{
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
			int numOfEmptyBlocks = 0;
			int numOfAllocated = 0;
			for(int i=0 ; i<this.emptyBlocks.size() ; i++)
			{
				if(this.emptyBlocks.get(i) == 0)
					numOfEmptyBlocks++;
				else
					numOfAllocated++;
			}
			System.out.println("Num Of Empty blocks: " + numOfEmptyBlocks);
			System.out.println("Num Of allocated blocks: " + numOfAllocated);
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
			System.out.println("The allocatedBlocks: ");
			displayAllocatedBlocks(LinkedAllocation.rootDirectory);
			
		}
		if(theCommand.equalsIgnoreCase("DisplayDiskStructure"))
		{
			this.counter = 0;
			System.out.println("<root>");
			for(int i=0 ; i<LinkedAllocation.rootDirectory.files.size() ; i++)
			{
				System.out.println("    <" + LinkedAllocation.rootDirectory.files.get(i).getFileName() + ">");
			}
			if(LinkedAllocation.rootDirectory.subDirectories.size() != 0)
			{
				displayDiskStructure(LinkedAllocation.rootDirectory.subDirectories.get(0));
			}
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
		if(this.counter >= LinkedAllocation.rootDirectory.subDirectories.size())
			return;
		else
			displayDiskStructure(LinkedAllocation.rootDirectory.subDirectories.get(this.counter));
	}
	
	
	private void displayAllocatedBlocks(Directory dir)
	{
			for(int i=0 ; i<dir.files.size() ; i++)
			{
				System.out.print(dir.files.get(i).getFilePath() + "  ");
				
				System.out.println(dir.files.get(i).getIndexedFile());/** The first index **/
				
				for(int j=0 ; j<dir.files.get(i).allocatedBlocks.size() ; j++)
				{
					System.out.print(dir.files.get(i).allocatedBlocks.get(j) + " ");
				}
				System.out.println();
			}
			for(int i=0 ; i<dir.subDirectories.size() ; i++)
			{
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
		 
		 File1 file = new File1();
		 file.setFilePath(thePath);
		 file.setFileSize(temp);
			 
			addFile(file , thePath);
		 
		 
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
	
	
	
	public void addFile(File1 file , String thePath)
	{
		this.dataOfPath = new ArrayList<String>();
		this.counter = 0;
		this.counter++;
		thePath += '/';
		splitDataOfPath(thePath);
		addFileHelper(file, this.dataOfPath.get(this.counter) , LinkedAllocation.rootDirectory);
	}
	
	public void addFolder(Directory folder , String path)
	{
		this.dataOfPath = new ArrayList<String>();
		this.counter = 0;
		this.counter++;
		path += '&';
		splitDataOfPath(path);
		addFolderHelper(folder, this.dataOfPath.get(this.counter) , LinkedAllocation.rootDirectory);
	}
	
	private void addFileHelper(File1 file , String path , Directory dir)
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
			
			
			int value = Integer.parseInt(file.getFileSize());
			
			int numOfBlocksNeeded = Math.round(value / this.sizeOfBlock);
			 if(numOfBlocksNeeded <= (this.numOfBlocks - this.consumed))
			 {
				 ArrayList<Integer> allocate = new ArrayList<Integer>();
				 
				 int counter2 = 0;
				 int index2 = 0;
				 for(int i=0 ; i<this.consumedBlocks ; i++)
				 {
					 if(this.emptyBlocks.get(i) == 0)
					 {
						 if(counter2 == 0)
						 {
							 index2 = i;
						 }
						 counter2++;
					 }
				 }
				 
				if(counter2 != 0)
				{
					for(int i = index2 ; i<(index2+counter2) ; i++)
					 {
						 this.emptyBlocks.set(i, 1);
					 }
					 allocate.add(index2);
					 allocate.add(index2+counter2); 
					 
					 if(counter2 < numOfBlocksNeeded)
					 {
						 int calc = numOfBlocksNeeded - counter2;
						 allocate.add(this.consumedBlocks);
						 allocate.add(this.consumedBlocks+calc);
						 for(int i = this.consumedBlocks ; i<(this.consumedBlocks+calc) ; i++)
						 {
							 this.emptyBlocks.set(i, 1);
						 }
					 }
					 file.addAllocatedBlocks(allocate);
				}
				else
				{
					System.out.println(this.consumedBlocks);
					allocate.add(this.consumedBlocks);
					allocate.add(this.consumedBlocks+numOfBlocksNeeded);
					for(int i = this.consumedBlocks ; i<(this.consumedBlocks+numOfBlocksNeeded) ; i++)
					 {
						this.emptyBlocks.set(i, 1);
					 }
					file.addAllocatedBlocks(allocate);
				}
				
				 
				 this.consumed += numOfBlocksNeeded;
				 this.consumedBlocks = this.consumed;
				 
				 
			 }
			 else {
				 System.out.println("The fileSize is bigger than the available, please try again");
				 return;
			 }
			
			 file.setIndexFile(this.counter3);
			 this.counter3++;
			 
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
					return;
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
		  deleteFileHelper(this.dataOfPath.get(this.counter) , LinkedAllocation.rootDirectory); 
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
					  for(int j=0 ; j<dir.files.get(i).allocatedBlocks.size() ; j=j+2)
					  {
						  for(int k=0 ; 
								  k<dir.files.get(i).allocatedBlocks.size() ; k++)
						  {
							  this.emptyBlocks.set(dir.files.get(i).allocatedBlocks.get(k) , 0);
							  this.consumed--;
						  }
					  }
					  dir.files.remove(i);
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
				  this.counter++;
				  if(this.counter >= this.dataOfPath.size())
					  return;
				  else
					  deleteFileHelper(this.dataOfPath.get(this.counter) , 
							  dir.subDirectories.get(i));
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
			this.dataOfPath = new ArrayList<String>(); 
			splitDataOfPath(path);
			this.counter = 0;
			this.counter++;
			deleteFolderHelper(this.dataOfPath.get(this.counter) , LinkedAllocation.rootDirectory);
	}
	 
	 private void deleteTheDirectoryItself(String path , Directory root)
	 {
		 
		 for(int i=0 ; i<root.subDirectories.size() ; i++)
		 {
			 if(root.subDirectories.get(i).getDirName().equals(path))
			 {
				 root.subDirectories.remove(i);
				 return;
			 }
		 }
	 }
	 
	 
	 private void deleteFolderHelper(String path , Directory dir)
	 {
		 int isFolder = path.indexOf("&");
		  if(isFolder != -1)/** Reaches the end of the path **/
		  {
			  String temp = "";
			  for(int i=0 ; i<path.length()-1 ; i++)
				  temp += path.charAt(i);
			  for(int i=0 ; i<dir.subDirectories.size() ; i++)
			  {
				  if(dir.subDirectories.get(i).getDirName().equals(temp))
				  {
					  deleteAllDataInFolder(dir.subDirectories.get(i));
					  deleteTheDirectoryItself(temp , dir);
					  System.out.println("The folder is deleted successfully....");
					  return;
				  }
			  }
			  System.out.println("You entered a wrong path of the file please try again");
			  return;
		  }
		  if(isFolder == -1)/** it's a directory **/
		  {
			  for(int i=0 ; i<dir.subDirectories.size() ; i++)
			  {
				  this.counter++;
				  if(this.counter >= this.dataOfPath.size())
					  return;
				  else
					  deleteFolderHelper(this.dataOfPath.get(this.counter) , 
							  dir.subDirectories.get(i));
			  }
		  }
	 }
	 
	 private void deleteAllDataInFolder( Directory dir)
	 {
			 for(int i=0 ; i<dir.files.size() ; i++)/** delete all files in the folder **/
			 {
				 for(int j=0 ; j<dir.files.get(i).allocatedBlocks.size() ; j++)
				 {
					 this.emptyBlocks.set(dir.files.get(i).allocatedBlocks.get(j), 0);
					 this.consumed--;
				 }
				 dir.files.get(i).allocatedBlocks.clear();
			 }
			 for(int i=0 ; i<dir.subDirectories.size() ; i++)
			 {
				 deleteAllDataInFolder(dir.subDirectories.get(i));
			 }
			
	 }

	 public void saveTheData() throws InterruptedException, IOException 
	 {
			 this.counter = 0;
			 for(int i=0 ; i<LinkedAllocation.rootDirectory.files.size() ; i++)
			{
				 FileWriter file = new FileWriter("file.txt" , true);
				 file.write(LinkedAllocation.rootDirectory.files.get(i).getFilePath() + 
					 " "	+ LinkedAllocation.rootDirectory.files.get(i).getFileSize() + "\n");
					file.close(); 
					
			}
				if(LinkedAllocation.rootDirectory.subDirectories.size() != 0)
				{
					saveDataToFile(LinkedAllocation.rootDirectory.subDirectories.get(0));
				}
		 
	 }
	 
	 private void saveDataToFile(Directory dir) throws InterruptedException, IOException 
	 {
			 FileWriter file = new FileWriter("file.txt" , true);
			 file.write(dir.getDirectoryPath() + "\n");
			 file.close(); 
				for(int i=0 ; i<dir.files.size() ; i++)
				{
					FileWriter file2 = new FileWriter("file.txt" , true);
					file2.write(dir.files.get(i).getFilePath() + " " + dir.files.get(i).getFileSize() + 
							"\n") ;
					file2.close(); 
				}
				for(int i=0 ; i<dir.subDirectories.size() ; i++)
				{
					displayDiskStructure(dir.subDirectories.get(i));
				}
				this.counter++;
				if(this.counter >= LinkedAllocation.rootDirectory.subDirectories.size())
					return;
				else
					saveDataToFile(LinkedAllocation.rootDirectory.subDirectories.get(this.counter)); 
	 }
	 
	 public void restoreData()
	 {
		 File newFile = new File("file.txt"); 
		 try {
				Scanner scan = new Scanner(newFile);
				int i = 0; 
				while(scan.hasNextLine()) {
					String temp = scan.nextLine();
					int isFile = temp.indexOf(".txt");
					if(isFile != -1)/** it's a file **/
					{
						CreateFile(temp);
					}
					else
					{
						CreateFolder(temp);
					}
				}
			} catch (FileNotFoundException e) {
				System.out.println("An error occured");
				e.printStackTrace();
			}
	 }
	 
}
