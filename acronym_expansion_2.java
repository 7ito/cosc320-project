package acronym;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.HashMap;
import java.util.Map;

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

			   expander.insert(acronymCell, expansionCell); //store the acronyms and expansion into trie data strcuture   
			}
			i++;
			
		}
		
		
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

class TrieNode {
    Map<Character, TrieNode> children;
    String expandedForm;

    TrieNode() {
        children = new HashMap<>();
        expandedForm = null;
    }
}

class AcronymExpander {
    TrieNode root;

    AcronymExpander() {
        root = new TrieNode();
    }

    void insert(String acronym, String expandedForm) {
        TrieNode node = root;

        for (char c : acronym.toCharArray()) {
            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);
        }
        node.expandedForm = expandedForm;
    }

	/*
	 * Expands acronyms from a given input String text using a Trie of acronyms and their expanded forms
	 * 
	 * @param text The input String to expand
	 * @return The text with acronyms expanded to their full forms
	 * 
	 * The method iterates through the input text and searches for acronyms in the Trie built by the csv file
	 * Word boundaries are checked for before and after each word/potential acronym to ensure that only standalone acronyms are expanded
	 * If the word found between each word boundary is a valid acronym that exists in the Trie, the expanded form of the acronym is appended to the output String
	 * If not, the original word is appended to the output String
	 * 
	 */
    String expand(String text) {
        StringBuilder expandedText = new StringBuilder();
        int i = 0;

        while (i < text.length()) {
            TrieNode node = root;
            TrieNode altNode = root;
            int j = i;
            boolean altNodeActive = true;

            while (j < text.length() && (node.children.containsKey(Character.toLowerCase(text.charAt(j))) || (altNodeActive && altNode.children.containsKey(Character.toUpperCase(text.charAt(j)))))) {
                if (node.children.containsKey(Character.toLowerCase(text.charAt(j)))) {
                    node = node.children.get(Character.toLowerCase(text.charAt(j)));
                } else {
                    altNodeActive = false;
                }

                if (altNodeActive && altNode.children.containsKey(Character.toUpperCase(text.charAt(j)))) {
                    altNode = altNode.children.get(Character.toUpperCase(text.charAt(j)));
                } else {
                    altNodeActive = false;
                }

                j++;
            }

            boolean isWordBoundaryBefore = i == 0 || !Character.isLetter(text.charAt(i - 1));
            boolean isWordBoundaryAfter = j == text.length() || !Character.isLetter(text.charAt(j));

            if (isWordBoundaryBefore && isWordBoundaryAfter && node.expandedForm != null) {
                if (i == 0) {
                    expandedText.append(Character.toUpperCase(node.expandedForm.charAt(0)));
                    expandedText.append(node.expandedForm.substring(1));
                } else {
                    expandedText.append(node.expandedForm);
                }
                i = j;
            } else if (isWordBoundaryBefore && isWordBoundaryAfter && altNodeActive && altNode.expandedForm != null) {
                if (i == 0) {
                    expandedText.append(Character.toUpperCase(altNode.expandedForm.charAt(0)));
                    expandedText.append(altNode.expandedForm.substring(1));
                } else {
                    expandedText.append(altNode.expandedForm);
                }
                i = j;
            } else {
                expandedText.append(text.charAt(i));
                i++;
            }
        }

        return expandedText.toString();
    }
}