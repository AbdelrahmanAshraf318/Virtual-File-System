package VFS;

import java.io.IOException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws IOException {
		
		Scanner input = new Scanner(System.in);
		ContigousAllocation contigous = new ContigousAllocation(100 , 5 , "H:/Computer Science Department/Level 3_Second Semester/Advanced Operating System/Assignments/Assignment 3/Virtual File System/src/VFS/file.txt");
		LinkedAllocation linked = new LinkedAllocation(100 , 5 , "H:/Computer Science Department/Level 3_Second Semester/Advanced Operating System/Assignments/Assignment 3/Virtual File System/src/VFS/file.txt");
		IndexedAllocation indexed = new IndexedAllocation(100, 5);
		
		String command = "";
		String cont = "";
		do
		{
			System.out.println("Enter Command: ");
			command = input.nextLine();
			
			indexed.spitTheCommand(command);
			
			System.out.println("Do you want to continue?");
			cont = input.nextLine();
		}while(cont.equalsIgnoreCase("yes"));
		/*String ans = "";
		System.out.println("Do you want to save the data?");
		ans = input.nextLine();
		if(ans.equalsIgnoreCase("yes"))
		{
			linked.saveDataToFile();
		}*/
	}

}
