package acronym;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class acronym_expansion_2 {
	
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
		AcronymExpander expander = new AcronymExpander();
		acronym_expansion_2 stopWatch = new acronym_expansion_2();
		
		//process of reading the csv file
		FileReader fr = new FileReader("/Users/21choit/eclipse-workspace/acronym/src/acronym/slang.csv"); 
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

			   expander.insert(acronymCell, expansionCell); //store the acronyms and expansion into trie data strcuture   
			}
			i++;
			
		}
		
		//File folder = new File("/Users/21choit/eclipse-workspace/acronym/src/acronym/2.8M App Reviews - 3686 Top Paid Apps - 998.8MB on Disk");
		File folder = new File("/Users/21choit/eclipse-workspace/acronym/src/acronym/demo");
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
			//fr = new FileReader ("/Users/21choit/eclipse-workspace/acronym/src/acronym/2.8M App Reviews - 3686 Top Paid Apps - 998.8MB on Disk/" + fileName);
		    fr = new FileReader("/Users/21choit/eclipse-workspace/acronym/src/acronym/demo/" + fileName);
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
		

		ArrayList<Long> timeList = new ArrayList<Long>();

		
		String expandedText;
		
		stopWatch.startTime();
		for (String text : contentList) {
			expandedText = expander.expand(text);
//			System.out.println(expandedText);
		
		stopWatch.endTime();
		timeList.add(stopWatch.getElapsedTime());
		}
		
		for (Long time : timeList) {
			System.out.println(time);
		}
		
	}

}
