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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;

public class HtmlExport {
	
	//	Generates a directory of HTML files corresponding to each customer
	public void generateDirectory(File csvFile) throws IOException {
		
		//=============================================================
		//================ create array sorted by state ===============
		
		FileParser f = new FileParser(csvFile);
		Customer[] customers = f.getCustomers();
		Arrays.sort(customers, new StateComparator(SortOrder.ascending));
		
		//=============================================================
		//======== create HTML Directory & generate index file ========
		
		new File("HTML Directory/").mkdir();
		PrintStream htmlIndex = new PrintStream(new File("index.html"));
		
		//=============================================================
		//========= create first folder inside HTML Directory =========
		
		String state = customers[0].getState(); 
		String currentDirectory = "HTML Directory/" + state;
		new File(currentDirectory).mkdir();

		//=============================================================
		//==== populate index file & generate customer directories ====
		
		htmlIndex.print(	"<!DOCTYPE html>\n"
						+	"<html>\n"
						+		"\t<head>\n"
						+			"\t\t<title>Index</title>\n"
						+			"\t\t<meta charset=\"utf-8\" />\n"
						+			"\n"
									//// CSS STYLING
						+			"\t\t<style type=\"text/css\">\n"
										// BODY
						+				"\t\t\tbody {\n"
						+					"\t\t\t\tbackground-color:#333333;\n"
						+					"\t\t\t\tfont-family:tahoma, helvetica, sans-serif;\n"
						+					"\t\t\t\tpadding:20px;\n"
						+					"\t\t\t\tmargin:0;\n"
						+ 				"\t\t\t}\n"
										// END OF BODY
						+				"\n"
										// #MAIN
						+				"\t\t\t#main {\n"
						+					"\t\t\t\tbackground-color:white;\n"
						+					"\t\t\t\tcolor:black;\n"
						+					"\t\t\t\ttext-align:center;\n"
						+					"\t\t\t\tmargin:auto;\n"
						+					"\t\t\t\twidth:30%;\n"
						+					"\t\t\t\tborder-radius:20px;\n"
						+					"\t\t\t\tpadding:20px;\n"
						+ 				"\t\t\t}\n"
										// END OF #MAIN
						+				"\n"
										// ANCHOR
						+				"\t\t\ta {\n"
						+					"\t\t\t\tline-height:1em;\n"
						+					"\t\t\t\tdisplay:inline-block;\n"
						+					"\t\t\t\tpadding:10px;\n"
						+					"\t\t\t\twidth:90%;\n"
						+					"\t\t\t\ttext-decoration:none;\n"
						+					"\t\t\t\tcolor:black;\n"
						+ 				"\t\t\t}\n"
										// END OF ANCHOR
						+				"\n"
										// A:HOVER
						+				"\t\t\ta:hover {\n"
						+					"\t\t\t\tcolor:white;\n"
						+					"\t\t\t\tbackground-color:#333333;\n"
						+ 				"\t\t\t}\n"
										// END OF A:HOVER
						+				"\n"
										// H1
						+				"\t\t\th1 {\n"
						+					"\t\t\t\tfont-size:3em;\n"
						+					"\t\t\t\tpadding:5px;\n"
						+					"\t\t\t\tborder-top:4px solid #333333;\n"
						+ 				"\t\t\t}\n"
										// END OF H1
						+			"\t\t</style>\n"
									//// END OF CSS
						+		"\t</head>\n"
						+		"\t<body>\n"
									//// MAIN DIV
						+			"\t\t<div id=\"main\">\n"
						+				"\t\t\t" + titleHeading(state) + "\n");
		
		//=============================================================				
		
		// loop though the sorted array
		for(int i=1; i<customers.length; i++) {	
			if(!state.equals(customers[i].getState())) {					// check if state has changed
				state = customers[i].getState();							// update with current state
				htmlIndex.print(		"\t\t\t" + titleHeading(state) + "\n");	// generate HTML heading element with current state
				currentDirectory = "HTML Directory/" + state;				// update current directory
				new File(currentDirectory).mkdir();							// create new folder corresponding to current state
			}
			
			// generate HTML page of current customer and update index page
			exportToHtml(customers[i], currentDirectory);	
			htmlIndex.print(			"\t\t\t" + hyperlink(currentDirectory + "/" + customers[i].getPhone() + ".html", customers[i].getFirstName() + " " + customers[i].getLastName()) + "<br/>\n");
		}
		
		//=============================================================		
		
		htmlIndex.print(			"\t\t</div>\n"
									//// END OF MAIN DIV
						+		"\t</body>\n"
						+	"</html>");
		htmlIndex.close();
	}
	
	//=================================================================
	//=================== private helper methods ======================	
	
	// Generates an HTML page with the information of a customer
	private void exportToHtml(Customer customer, String currentDirectory) throws FileNotFoundException {
		PrintStream html = new PrintStream(new File(currentDirectory + "/" + customer.getPhone() + ".html"));
		String template = 		"<!DOCTYPE html>\n"
							+	"<html>\n"
							+		"\t<head>\n"
							+			"\t\t<title>" + customer.getFirstName() + " " + customer.getLastName() + "</title>\n"
							+			"\t\t<meta charset=\"utf-8\" />\n"
							+			"\n"
										//// CSS STYLING
							+			"\t\t<style type=\"text/css\">\n"
											// BODY
							+				"\t\t\tbody {\n"
							+					"\t\t\t\tbackground-color:#333333;\n"
							+					"\t\t\t\tfont-family:tahoma, helvetica, sans-serif;\n"
							+					"\t\t\t\tpadding:20px;\n"
							+					"\t\t\t\tmargin:0;\n"
							+ 				"\t\t\t}\n"
											// END OF BODY
							+				"\n"
											// #MAIN
							+				"\t\t\t#main {\n"
							+					"\t\t\t\tbackground-color:white;\n"
							+					"\t\t\t\tcolor:black;\n"
							+					"\t\t\t\ttext-align:center;\n"
							+					"\t\t\t\tmargin:auto;\n"
							+					"\t\t\t\twidth:30%;\n"
							+					"\t\t\t\tborder-radius:20px;\n"
							+					"\t\t\t\tpadding:20px;\n"
							+ 				"\t\t\t}\n"
											// END OF #MAIN
							+				"\n"
											// #FOOTER
							+				"\t\t\t#footer {\n"
							+					"\t\t\t\tfont-size:12px;\n"
							+					"\t\t\t\tpadding:5px;\n"
							+					"\t\t\t\tcolor:#666666;\n"
							+ 				"\t\t\t}\n"
											// END OF #FOOTER
							+			"\t\t</style>\n"
										//// END OF CSS
							+		"\t</head>\n"
							+		"\t<body>\n"
										//// MAIN DIV
							+			"\t\t<div id=\"main\">\n"
							+				"\t\t\t" + titleHeading(customer.getFirstName() + " " + customer.getLastName())+ "<br/>\n"
							+				"\t\t\t" + customer.getCompany() + "<br/>\n"
							+				"\t\t\t" + customer.getAddress() + "<br/>\n"
							+				"\t\t\t" + customer.getCity() + ", " + customer.getState() + " " + customer.getZip() + "<br/>\n"
							+				"\t\t\tPhone: " + customer.getPhone() + "<br/>\n"
							+				"\t\t\tFax: " + customer.getFax() + "<br/><br/>\n"
							+				"\t\t\t" + emailLink(customer.getEmail(), "Hello from CMPS212", customer.getEmail()) + "<br/>\n"
							+				"\t\t\t" + hyperlink(customer.getWeb(), customer.getWeb()) + "<br/><br/>\n"
							+				"\t\t\t" + cityMapLink(customer.getCity(), customer.getState(), customer.getCounty(), "Click here to get a map of " + customer.getCity() + ", " + customer.getState()) + "<br/><br/>\n"
							+				"\t\t\t" + hyperlink("../../index.html", "Go back") + "<br/><br/>\n"
							+				"\t\t\t<div id=\"footer\"><b>Created by</b> " + emailLink("jmh20@aub.edu.lb", "Wonderful work Jad!", "Jad Haidar") +"<b> | Customer Viewer version 2.5 | Last updated on 11/05/2015</b></div>\n"
							+			"\t\t</div>\n"
										//// END OF MAIN DIV
							+		"\t</body>\n"
							+	"</html>";
		html.print(template);
		html.close();
	}

	// Returns an HTML heading element
	private String titleHeading(String heading) {
		return "<h1>" + heading + "</h1><br/>";
	}

	// Returns an HTML anchor element
	private String hyperlink(String url, String linkText) {
		return "<a href=\"" + url + "\">" + linkText + "</a>";
	}
	
	// Returns an HTML anchor element (specific to mail)
	private String emailLink(String emailAddress, String subject, String linkText) {
		return "<a href=\"mailto:" + emailAddress + "?Subject=" + subject + "\">" + linkText + "</a>";
	}
	
	// Returns an HTML anchor element (specific to Google Maps)
	private String cityMapLink(String city, String state, String county, String linkText) {
		return "<a href=\"https://www.google.com/maps/place/" + city + ",+" + state + ",+" + county + "\" target=\"_blank\">" + linkText + "</a>";
	}
	
	//=================================================================
	//========================= end of code ===========================
}

