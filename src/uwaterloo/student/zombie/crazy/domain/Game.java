package uwaterloo.student.zombie.crazy.domain;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Game {
	Structure node = null;  // arbitrary building in the map to keep the memory here
	
	public static void main(String[] args){ 
		System.out.println("HELLO WORLD");
	}
	
	public void constructMap(String[] args) throws IOException{
		String mapFileName = ""; // TODO: figure out where to put this file
		
		FileReader input = new FileReader(mapFileName);
		BufferedReader bufRead = new BufferedReader(input);
		String line = null;

		line = bufRead.readLine();
		int numOfStructures = Integer.parseInt(line);
		
		for (int i = 0; i < numOfStructures; ++i) {
			line = bufRead.readLine();
			
			String[] lineItems = line.split(" ");
			if (3 != lineItems.length) {
				throw new RuntimeException("Incorrect format for structure line! Expected 3 items!");
			}
			
			Structure newStructure = null;
			if (lineItems[2] == "B") {
				newStructure = new Building(Integer.parseInt(lineItems[1]), lineItems[0]);
			} else if (lineItems[2] == "L") {
				newStructure = new Link(Integer.parseInt(lineItems[1]), lineItems[0]);
			} else {
				throw new RuntimeException("Incorrect format for structure line! "
						+ "Expected either 'B' or 'L' at the end of line!");
			}
			
			if (null == node) {
				node = newStructure;
			}
		}
	}
}
