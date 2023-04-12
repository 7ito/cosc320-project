package acronym;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;


public class acronym_expansion {
	
	private long startTime;
	private long endTime;
	
	public void startTime() {
		startTime = System.currentTimeMillis();
	}
	
	public void endTime() {
		endTime = System.currentTimeMillis();
	}
	
	public long getElapsedTime() {
		return endTime-startTime;
	}
	
	public static void main (String[] args) throws IOException {
		
		acronym_expansion stopWatch = new acronym_expansion();
		
		HashMap<String,String> map = new HashMap<>();
		//process of reading the csv file
		FileReader fr = new FileReader("./slang.csv"); 
		BufferedReader input = new BufferedReader(fr);
		
		String acronymCell, expansionCell;
		String row;
		
		int i = 0;
		//iterate through each cells in the csv file and store the acronym and expansion in hashmap
      	
		while((row = input.readLine()) != null) {
			
			if (i > 0) {
				// this will read first line and separates values by (,) and stores them in tokens.
			    StringTokenizer tokens = new StringTokenizer( (String) row, ",");
			    tokens.nextToken(); // this method will read the tokens values on each call.
			    
			    //assign acronyms and expansions 
			    acronymCell = tokens.nextToken();
			    expansionCell = tokens.nextToken();

			    map.put(acronymCell, expansionCell); //store the acronyms and expansion into a hash map    
			}
			i++;
		}
		
		//----------------------------------------------------------------------------------------------------------------------------------------	
		
		String datasetPath = "./dataset"; // Please put dataset files into this directory
		File folder = new File(datasetPath);
		File[] files = folder.listFiles();
		ArrayList<String> filePaths = new ArrayList<String>();
		ArrayList<String> contentList = new ArrayList<String>();

		//iterate through each file in the dataset folder and get all of the files' name
		
		for (File file : files) {
			if (file.isFile()) {
				filePaths.add(file.getName());
		    }
		}
		
		for(String fileName:filePaths) {
		    fr = new FileReader(datasetPath + "/" + fileName);
		    input = new BufferedReader(fr);
		
		//iterate through each cells in the csv file and store the content section into the arraylist
		    while ((row = input.readLine()) != null) {
		    	String[] sections = row.split(",");
		    	if (sections.length >= 4) {
		    		String content = sections[3];
		    		if (!(content.equals("content"))) {
		    			contentList.add(content);
		    		}
		    	}
		    }
		}
		
		String[] splitText = null;
		String newText = "";
		
		ArrayList<Long> timeList = new ArrayList<Long>();
		
		//iterate through the contentList array list
		stopWatch.startTime();
		for (String text : contentList) {
			splitText = text.split(("(?<=\\W)|(?=\\W)"));
			for (int j = 0; j < splitText.length; j++) {
				String originalWord = splitText[j];
		        String lowercaseWord = originalWord.toLowerCase();
		        
				//Check if the word is same in the key from the hashmap. If true, replace with the long-form from the value of the hashmap.
		        if(map.containsKey(lowercaseWord)) {
		            String longForm = map.get(lowercaseWord);
		            
		            // Check whether the original word in the text is in uppercase or not
		            if (originalWord.equals(originalWord.toUpperCase())) {
		                // replace with uppercase first letter
		                splitText[j] = longForm.toUpperCase().charAt(0) + longForm.substring(1);
		            } else {
		                // replace with lowercase first letter
		                splitText[j] = longForm.charAt(0) + longForm.substring(1);
		            }
		            stopWatch.endTime();
		            timeList.add(stopWatch.getElapsedTime());
		        }
		       
			    newText += splitText[j];
			}
			newText += "\n";
		}
		//System.out.println(newText);
		
		for (Long time : timeList) {
			System.out.println(time);
		}
		

		input.close();  
		fr.close();      
	}
	
	
}
