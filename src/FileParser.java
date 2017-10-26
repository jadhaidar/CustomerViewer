/*
 * =================================================================
 * =================================================================
 * 
 * Name:			Jad Haidar
 * Date:			11/04/2015
 * Version:			2.5
 * 
 * ========================== Changelog: ===========================
 * 
 * 11/05/2015:	-	Updated htmlExport method with code to check if
 * 					the HTML Directory already exist in the target
 * 					output and update method behavior accordingly
 * 				-	Updated comments
 * 				-	Removed now not so useful Reset menu item and
 * 					corresponding actionListener
 * 11/04/2015:	-	Created a new helper method to display a success
 * 					message once the HTML directory is generated and
 * 					to automatically execute the index.html file
 * 				-	Added CSS styling to index.html
 * 				-	Implemented HtmlExport
 * 				-	Merged HtmlHelper with HtmlExport and cleaned up
 * 					the code
 * 				-	Added success message for Generate HTML Directory
 * 11/03/2015:	-	Implemented HtmlHelper class
 * 				-	Added a new Generate HTML Directory menu item
 * 					under File
 * 10/28/2015:	-	Updated the code to enable support for selection
 * 					of an unsorted array
 * 				-	Updated the search method to account for the
 * 					unsorted array
 * 				-	Update the goToButton actionListener to account
 * 					for invalid user input and display the 
 * 					corresponding error message in txtRecordOf
 * 10/25/2015:	-	Implemented SortOrder enum, reducing required
 * 					Comparator classes by half (now one for each
 * 					Customer field)
 * 10/23/2015:	- 	Updated jFileChooser to open inside the same
 * 					directory as the source/jar file
 * 				-	Disabled the "All files" option when browsing
 * 					a file inside the jFileChooser
 * 				-	Implemented sort functionality in ascending
 * 					and descending orders (created 24 specialized
 * 					classes that implement Comparator<E>)
 * 				- 	Moved the clearUserInput method inside the
 * 					displayCustomer method and cleaned resulting
 * 					unnecessary code inside actionListener 
 * 10/19/2015:	-	Broke down initial CustomerViewer class into
 * 					separate UserInterface & MouseActions classes
 * 10/18/2015: 	-	Implemented Reset, Clear and Exit actionListeners
 * 				-	Removed empty toolbar
 * 10/17/2015: 	-	Added lowercase search
 * 10/16/2015:	-	Implemented jFileChooser and updated FileParser
 * 					accordingly
 * 
 * =================================================================
 * =================================================================
 */

import java.io.*;
import java.util.Scanner;

public class FileParser {
	private Scanner input;
	private File filePath;
	
	//=================================================================
	//==================== overloaded constructor =====================
	
	public FileParser(File file) throws FileNotFoundException {
		input = new Scanner(file);
		filePath = file;
	}
	
	//=================================================================
	//========================= getter method =========================
	
	public Customer[] getCustomers() throws FileNotFoundException {
		String line;
		String[] tokens;
		
		Customer[] customers = new Customer[arrayCapacity(filePath)];
		input.nextLine(); // gets rid of header
		
		for(int i=0; i<customers.length; i++){
			line = input.nextLine();					// tokenizes each line of input
			line = line.substring(1, line.length()-1);	// removes the first and last characters (")
			tokens = line.split("\",\"");				// splits each line into an array of strings to construct a customer
			customers[i] = new Customer(tokens[0],tokens[1],tokens[2],tokens[3], tokens[4], tokens[5], tokens[6], tokens[7], tokens[8], tokens[9], tokens[10], tokens[11]);
		}
		
		input.close();
		return customers;
	}
	
	//=================================================================
	//==================== private helper method ======================
	
	// Determines how many customers are in the specified .csv file
	private int arrayCapacity(File filePath) throws FileNotFoundException {
		Scanner scanLines = new Scanner(filePath);
		int arraySize = -1;
		
		while(scanLines.hasNextLine()){
			arraySize++;
			scanLines.nextLine();
		}
		
		scanLines.close();
		return arraySize;
	}
	
	//=================================================================
	//========================= end of code ===========================
}

