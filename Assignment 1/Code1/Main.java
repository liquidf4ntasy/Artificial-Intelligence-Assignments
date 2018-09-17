/*
Assignment #1
Task: Implement Uniform Cost Search (UCS)
Name: Satyajit Deshmukh
Id: 1001417727
Course Name: Artificial Intelligence
Course Number: 5360
*/

/*
References:
http://stackoverflow.com/
http://www.geeksforgeeks.org/
http://www.w3schools.com/
https://gist.github.com/pif/1948213/revisions
*/

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
//import java.util.*;
import java.io.*;
import java.net.URL;

import java.util.Map;
import java.util.TreeMap;

// define Node class used to store nodes of graph
class Node {
    private Map<Integer, Node> children = new TreeMap<Integer, Node>();
    private String label;

    public void addEdge(Node node, int dist) {
        if (node == null) {
            throw new NullPointerException("edge");
        }
        children.put(dist, node);
    }

    public Node(String label) {
        if (label == null) {
            throw new NullPointerException("null label");
        }
        this.label = label;
    }

    public Map<Integer, Node> getChildren() {
        return children;
    }
    
    @Override
    public String toString() {
        return label;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Node other = (Node) obj;
        return this.label.equals(other.label);
    }

    @Override
    public int hashCode() {
        return label.hashCode();
    }
}

public class Main {

    public static void main(String[] args) throws IOException{
        
		
		final int MAX = 50;
        Node start[] = new Node[MAX];
        String places[] = new String[MAX];
        int connectedNodes[][] = new int[MAX][MAX];
        
         int currentPlacesIndex1 = 0, currentPlacesIndex2 =0, incrementplaces = 0;
               
               
                for(int i =0 ; i< MAX ; i++)
                    for(int j =0 ; j< MAX ; j++)
			 connectedNodes[i][j] = 0;
	
              int part1 = 0; int part2 = 0;
              
        InputStreamReader is = new InputStreamReader(System.in);
        BufferedReader newBuffer = new BufferedReader(is);
        
        String ip = newBuffer.readLine();
        String[] inputText = ip.split("\\s+");
        
        String filename = inputText[0];
        String source = inputText[1];
        String destination = inputText[2];
              
        int src =0 ,dst = 0;     
        
        
        URL path = Main.class.getResource(filename);
		File f = new File(path.getFile());
		BufferedReader br = new BufferedReader(new FileReader(f));
        
        
        //BufferedReader br = new BufferedReader(new FileReader("\\"));
        String line=null;
             
        String s = "END OF INPUT";
        line=br.readLine();
		
		// Run until "END OF INPUT" string appears
        while( (!line.equals(s))) {
                
                 String[] splitted = line.split("\\s+");
                 //System.out.println(splitted[0] + splitted[1] + splitted[2] );
               
                 if (Arrays.asList(places).contains(splitted[0])){
                     
	    		currentPlacesIndex1 =  Arrays.asList(places).indexOf(splitted[0]);
                   
                 }
	    	else{															
	    		places[incrementplaces]=  splitted[0];  
	    		currentPlacesIndex1 = incrementplaces;
	    	start[currentPlacesIndex1] = new Node(splitted[0]);
              
                incrementplaces++;
	    	}
                // System.out.println(incrementplaces);
                   if (Arrays.asList(places).contains(splitted[1]))				
                   {
                       currentPlacesIndex2 =  Arrays.asList(places).indexOf(splitted[1]);
                    
                   }
	    	else{															
	    		places[incrementplaces]=  splitted[1];  
	    		currentPlacesIndex2 = incrementplaces;
	    	start[currentPlacesIndex2] = new Node(splitted[1]);
             
                        incrementplaces++;
	    	
                }
                
              connectNodes(start[currentPlacesIndex1], start[currentPlacesIndex2], Integer.parseInt(splitted[2]));
		// Debug code to print connections taking place between nodes
        // System.out.println("connecting nodes:" + start[currentPlacesIndex1] + " " +  start[currentPlacesIndex2] + " " + Integer.parseInt(splitted[2]) );
		 
        line=br.readLine();
        }  
            for(int i =0 ; i< MAX ; i++){
              src=  Arrays.asList(places).indexOf(source);
                dst =  Arrays.asList(places).indexOf(destination);
                if (places[src] == source && places[dst] == destination )
					
              break;
            }
         
           // System.out.println(start[11] + " " + start[2]);
        System.out.println(findPath(start[src], start[dst]));
        
    }
     
	 
	 // Function findPath to implement the Uniform Cost Search Algorithm to find the Optimal Path.
    public static List<Node> findPath(Node start, Node finish) {
        TreeMap<Integer, Node> fringe = new TreeMap<Integer, Node>();
        Set<Node> explored  = new HashSet<Node>();
        
        fringe.put(0, start);
        while(!fringe.isEmpty()) {
     // imp display code    //
	 	 System.out.println("Finding Optimal Route.........................");
		 System.out.println(fringe);
//            if (fringe.isEmpty()) {
//                throw new IllegalStateException("empty fringe");
//            }
            Entry<Integer, Node> firstEntry = fringe.pollFirstEntry();
            Node closest = firstEntry.getValue();
     // imp display code    //
	 System.out.println(firstEntry.getKey() + " " + firstEntry.getValue());
            if (finish.equals(closest)) {
				System.out.println("--------------------------------------");
                System.out.println("Source-Destination Optimal Path Found!");
				System.out.println("--------------------------------------");

                System.out.println("Distance: " + firstEntry.getKey());
                return Arrays.asList(new Node[]{closest});
            }
            explored.add(closest);
			
            for (Entry<Integer, Node> entry : closest.getChildren().entrySet()) {
                if (!explored.contains(entry.getValue())) {
                    fringe.put(firstEntry.getKey()+entry.getKey(), entry.getValue());
                }
            }
        }
		System.out.println("Distance: infinity (Probably no Connection between Source-Destination)");
        return null;
    }
    
    private static void connectNodes(Node node1, Node node2, int dist) {
        node1.addEdge(node2, dist);
        node2.addEdge(node1, dist);
    }
    
	// Debugging function (unused in main program)
    private static void printGraph(Node start) {
        System.out.println(start);
  //      for (Node node : start.getChildren().keySet()) {
  //          if (node.equals(start)) {
  //              printGraph(node);
  //         }
  //      }
    }
}