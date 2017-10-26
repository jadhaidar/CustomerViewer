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

public class Customer {
	private String firstName;
	private String lastName;
	private String company;
	private String address;
	private String city;
	private String county;
	private String state;
	private String zip;
	private String phone;
	private String fax;
	private String email;
	private String web;
	
	//=================================================================
	//==================== overloaded constructor =====================
	
	public Customer(String firstName, String lastName, String company, String address, String city, String county, String state, String zip, String phone, String fax, String email, String web) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.company = company;
		this.address = address;
		this.city = city;
		this.county = county;
		this.state = state;
		this.zip = zip;
		this.phone = phone;
		this.fax = fax;
		this.email = email;
		this.web = web;
	}
	
	//=================================================================
	//========================= getter methods ========================

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getCompany() {
		return company;
	}

	public String getAddress() {
		return address;
	}

	public String getCity() {
		return city;
	}

	public String getCounty() {
		return county;
	}

	public String getState() {
		return state;
	}

	public String getZip() {
		return zip;
	}

	public String getPhone() {
		return phone;
	}

	public String getFax() {
		return fax;
	}

	public String getEmail() {
		return email;
	}

	public String getWeb() {
		return web;
	}
	
	//=================================================================
	//========================= end of code ===========================
}

