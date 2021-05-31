package VFS;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

public class ContigousAllocation {
	
	
	static Directory rootDirectory = new Directory();
	
	private	int numOfBlocks;
	private	int consumedBlocks;
	private	int sizeOfBlock;
	private ArrayList<String> dataOfPath = new ArrayList<String>();
	int counter = 0;
	private ArrayList<Integer> emptyBlocks = new ArrayList<Integer>();
	private ArrayList<Integer> keepIndexOfFolder = new ArrayList<Integer>();/** to help in removing folder **/ 
	private int consumed = 0;
	
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
			if(ContigousAllocation.rootDirectory.subDirectories.size() != 0)
			{
				displayDiskStructure(ContigousAllocation.rootDirectory.subDirectories.get(0));
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
						  this.consumed--;
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
		deleteFolderHelper(this.dataOfPath.get(this.counter) , ContigousAllocation.rootDirectory);
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
				 for(int j=dir.files.get(i).allocatedBlocks.get(0) ; 
						 j<dir.files.get(i).allocatedBlocks.get(1) ; j++)
				 {
					 this.emptyBlocks.set(j, 0);
					 this.consumed--;
				 }
				 dir.files.get(i).allocatedBlocks.clear();
			 }
			 for(int i=0 ; i<dir.subDirectories.size() ; i++)
			 {
				 deleteAllDataInFolder(dir.subDirectories.get(i));
			 }
			
	 }
	 
	 private ArrayList<Integer> readFile(String fileName)
		{
		 	
		 File file = new File(fileName);
			
			try {
				Scanner scan = new Scanner(file);
				int i = 0; 
				ArrayList<Integer> tall = new ArrayList<Integer>();
				while(scan.hasNextInt()) {
					tall.add(scan.nextInt());
				}
				return tall;
			} catch (FileNotFoundException e) {
				System.out.println("An error occured");
				e.printStackTrace();
				return null;
			}

		}
	 
	
	 
	 public void saveTheData() throws InterruptedException, IOException 
	 {
			
			 
			 this.counter = 0;
			 for(int i=0 ; i<ContigousAllocation.rootDirectory.files.size() ; i++)
			{
				 FileWriter file = new FileWriter("file.txt" , true);
				 file.write(ContigousAllocation.rootDirectory.files.get(i).getFilePath() + 
					 " "	+ ContigousAllocation.rootDirectory.files.get(i).getFileSize() + "\n");
					file.close(); 
					
			}
				if(ContigousAllocation.rootDirectory.subDirectories.size() != 0)
				{
					saveDataToFile(ContigousAllocation.rootDirectory.subDirectories.get(0));
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
					saveDataToFile(dir.subDirectories.get(i));
				}
				this.counter++;
				if(this.counter >= ContigousAllocation.rootDirectory.subDirectories.size())
					return;
				else
					saveDataToFile(ContigousAllocation.rootDirectory.subDirectories.get(this.counter)); 
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
