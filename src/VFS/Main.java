package VFS;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		
		Scanner input = new Scanner(System.in);
		ContigousAllocation contigous = new ContigousAllocation(100 , 20);
		
		String command = "";
		String cont = "";
		do
		{
			System.out.println("Enter Command: ");
			command = input.nextLine();
			
			contigous.spitTheCommand(command);
			
			System.out.println("Do you want to continue?");
			cont = input.nextLine();
		}while(cont.equalsIgnoreCase("yes"));

	}

}
