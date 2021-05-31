package VFS;

import java.io.IOException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws IOException, InterruptedException {
		
		Scanner input = new Scanner(System.in);
		Scanner input2 = new Scanner(System.in);
		ContigousAllocation contigous = new ContigousAllocation(100 , 5);
		LinkedAllocation linked = new LinkedAllocation(100 , 5);
		IndexedAllocation indexed = new IndexedAllocation(100, 5);
		
		
		
		
		
		String command = "";
		String cont = "";
		int choose = 0;
		
		System.out.println("Choose one of the following options: ");
		System.out.println("1)Contigous Allocation");
		System.out.println("2)Linked Allocation");
		System.out.println("3)Indexed Allocation");
		choose = input2.nextInt();
		if(choose == 1)
		{
			contigous.restoreData();
			do
			{
				System.out.println("Enter Command: ");
				command = input.nextLine();
				
				contigous.spitTheCommand(command);
				
				System.out.println("Do you want to continue?");
				cont = input.nextLine();
			}while(cont.equalsIgnoreCase("yes"));
		}
		else if(choose == 2)
		{
			linked.restoreData();
			do
			{
				System.out.println("Enter Command: ");
				command = input.nextLine();
				
				
				linked.spitTheCommand(command);
				
				System.out.println("Do you want to continue?");
				cont = input.nextLine();
			}while(cont.equalsIgnoreCase("yes"));
		}
		else if(choose == 3)
		{
			indexed.restoreData();
			do
			{
				System.out.println("Enter Command: ");
				command = input.nextLine();
				
				
				indexed.spitTheCommand(command);
				
				System.out.println("Do you want to continue?");
				cont = input.nextLine();
			}while(cont.equalsIgnoreCase("yes"));
		}
		
		String ans = "";
		System.out.println("Do you want to save the data?");
		ans = input.nextLine();
		if(ans.equalsIgnoreCase("yes"))
		{
			if(choose == 1)
			{
				contigous.saveTheData();
			}
			else if(choose == 2)
			{
				linked.saveTheData();
			}
			else if(choose == 3)
			{
				indexed.saveTheData();
			}
		}
		else
			System.out.println("Thanks for using my app");
			
	}

}
