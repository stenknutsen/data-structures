package friends;

import java.io.*;
import java.util.*;
/**
 * Authors:
 * Olaolu Emmanuel
 * and
 * Sten Knutsen
 * 
 */

public class Friends {

	public static void main(String[] args) throws IOException {
		
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter file name: ");
		String file = sc.nextLine();
		Graph friends = new Graph(file);
		
		//friends.print();//for testing only---remove
		

		while(true){
			System.out.println();
			System.out.println("1. Students at a school");
			System.out.println("2. Shortest intro chain");
			System.out.println("3. Cliques at school");
			System.out.println("4. Connectors");
			System.out.println("5. Quit");
			System.out.println();
			System.out.print("Select 1 - 5: ");
			Scanner sn = new Scanner(System.in);
			String sl = sn.nextLine();
			
			char selection = sl.charAt(0);
			
			if(selection == '1'){
				//students at school
				System.out.print("Enter school name: ");
				Scanner s1 = new Scanner(System.in);
				String school = s1.nextLine();
				friends.studentsAtSchool(school);
				
				continue;
			}
			if(selection == '2'){
				//shortest path
				System.out.print("Enter name of first person: ");
				Scanner s1 = new Scanner(System.in);
				String start = s1.nextLine();
				System.out.print("Enter name of second person: ");
				Scanner s2 = new Scanner(System.in);
				String target = s1.nextLine();
				friends.shortestPath(start, target);
				continue;
			}
			if(selection == '3'){
				//cliques
				continue;
			}
			if(selection == '4'){
				//connectors
				continue;
			}
			if(selection == '5'){
				System.out.println("Session ended.");
				break;
			}
			
			System.out.println("Invalid entry.");
			System.out.println();
			
		}//end while
		

	}

}
