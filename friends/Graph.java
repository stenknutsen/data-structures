package friends;

import java.io.*;
import java.util.*;

class Neighbor {
	
	public int vertexNum;
	public Neighbor next;
	
	public Neighbor(int vnum, Neighbor nbr){
		
		this.vertexNum = vnum;
		next = nbr;
		
	}
	
}


class Vertex {
	
	String name;
	String school;
	Neighbor adjList;
	
	Vertex(String name, String school, Neighbor neighbors){
		
		this.name = name;
		this.school = school;
		this.adjList = neighbors;
		
	}
}




public class Graph {
	
	Vertex[] adjLists;
	
	public Graph(String file) throws FileNotFoundException {
		
		Scanner sc = new Scanner(new File(file));
		System.out.println();
		adjLists = new Vertex[sc.nextInt()];
		
		sc.nextLine();//advance
		
		for(int v = 0;v < adjLists.length;v++){
			
			String str = sc.nextLine();
			
			//school and student names separated here
			String name = getName(str);
			String school = getSchool(str);
			
			adjLists[v] = new Vertex(name, school, null);
				
		}
		
		
		while(sc.hasNext()){
			String str = sc.nextLine();
			
			String name1 = getName(str);
			String name2 = getSecondName(str);
			
			int v1 = indexForName(name1);
			int v2 = indexForName(name2);
			
			adjLists[v1].adjList = new Neighbor(v2, adjLists[v1].adjList);
			adjLists[v2].adjList = new Neighbor(v1, adjLists[v2].adjList);
			
		}	
		
	}
	
	//extracts name from string
	private static String getName(String str){
		
		String name = str.substring(0, str.indexOf('|'));
		return name;
	}
	
	//extracts second name from string
	private static String getSecondName(String str){
		
		String name = str.substring(str.indexOf('|')+1, str.length());
		return name;	
	}
	
	//extracts school name from string
	private static String getSchool(String str){
			
		String school = str.substring(str.indexOf('|'), str.length());
		if(school.charAt(1) == 'n'){
			return null;
		}
		school = school.substring(3, school.length());	
		return school;
	}
	
	int indexForName(String name){
		for(int v=0;v<adjLists.length; v++){
			if (adjLists[v].name.toLowerCase().equals(name.toLowerCase())){
				return v;
			}
		}
		return -1;
	}
	
	
	String nameForIndex(int index){
		
		return adjLists[index].name;
		
	}
	
	public void print(){
		
		System.out.println();
		for(int v = 0;v<adjLists.length;v++){
			System.out.print(adjLists[v].name);
			if(adjLists[v].school!=null){
				System.out.print("("+adjLists[v].school+")");
			}
			
			for(Neighbor nbr=adjLists[v].adjList;nbr!=null;nbr=nbr.next){
				System.out.print(" --> "+adjLists[nbr.vertexNum].name);
			}
			System.out.println();
		}
		
	}//end print
	
	public void studentsAtSchool(String school){
		boolean atLeast1 = false;
		for(int v = 0; v<adjLists.length;v++){
			if(adjLists[v].school!=null && adjLists[v].school.toLowerCase().equals(school.toLowerCase())){
				atLeast1 = true;
				System.out.println(adjLists[v].name);
			}
		}
		
		if(!atLeast1){
			System.out.println("No students found at that school.");
		}
		
	}//end students at school
	
	public void shortestPath(String startName, String targetName){
		int start = indexForName(startName);
		//return if name not found
		if (start<0){
			System.out.println(startName+" was not found.");
			return;
		}

		int target = indexForName(targetName);
		//return if name not found
		if (target<0){
			System.out.println(targetName+" was not found.");
			return;
		}
		
		//set pathFound to test later if a path exists
		boolean pathFound = false;
		
		//create indegree array using indegree method
		int[] indeg = indegree();
	
		
		//create "from" array and initialize to -1
		int[] from = new int[adjLists.length];
		for (int i = 0;i<from.length;i++){
			from[i] = -1;
		}
		
		Queue<Integer> queue = new LinkedList<Integer>();//initialize queue with starting point
		queue.add(start);
		

		
		while(!queue.isEmpty()){
		//get vert index
		int v = queue.remove();
		//iterate through neighbors of vert
		for (Neighbor nbr=adjLists[v].adjList;nbr!=null;nbr=nbr.next){
			//if this neighbor has not yet been visited, place who visited in "from" array
			if (from[nbr.vertexNum]== -1){
				from[nbr.vertexNum] = v;
			}
			//if target found, empty queue to while loop breaks, set pathFound to true and break out of for loop
			if (nbr.vertexNum == target){
				while(!queue.isEmpty()){
					queue.remove();
				}
				pathFound = true;
				break;
			}
			//if indegree of this neighbor is >0, reduce by 1
			if (indeg[nbr.vertexNum]>0){
				indeg[nbr.vertexNum]--;
			}
			//if indegree of this neighbor is still >0, enqueue
			if (indeg[nbr.vertexNum]>0){
				queue.add(nbr.vertexNum);
			}	
			
		}//end for loop
		
		}//end while
		
		//return if path not found
		if (!pathFound){
			System.out.println("No path found.");
			return;
		}
		
		
		//for (int i = 0 ; i<adjLists.length;i++){
		//	System.out.println("Index: "+i+", Indegree: "+indeg[i]+", From: "+from[i]);
		//}
		
		//creating int stack to record path 
		Stack<Integer> stack = new Stack<Integer>();
		//initialize stack with target
		stack.push(target);
		
		int i = target;
		
		while(true){
			//if we've reached the beginning, add the start an break out of the loop
			if (from[i]==start){
				stack.push(start);
				break;
			}
			stack.push(from[i]);
			i = from[i];
		}
		//print path
		System.out.print(nameForIndex(stack.pop()));
		while (!stack.isEmpty()){
			System.out.print(" --- "+ nameForIndex(stack.pop()));
		}
		
		
		
		
	}//end shortestPath
	
	//creates array of indegree values
	private int[] indegree(){
		int [] indegree = new int [adjLists.length];
		
		for (int v = 0;v<adjLists.length;v++){
			for (Neighbor nbr = adjLists[v].adjList;nbr!=null;nbr = nbr.next){
				indegree[nbr.vertexNum]++;
			}
		}
		
		return indegree;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}//end graph
