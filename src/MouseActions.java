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

import java.util.Arrays;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class MouseActions extends UserInterface{
	private Customer[] customers;
	private Customer[] unsortedCustomers;
	private String selectedField;
	private File csvFile;
	private int index;
	
	//=================================================================
	//==================== overloaded constructor =====================
	
	public MouseActions() {
			openFileAction();
			clearAction();
			exitAction();
			aboutAction();
			htmlExport();
			buttonSearchAction();
			mouseWheelAction();
			buttonFirstAction();
			buttonPreviousAction();
			buttonNextAction();
			buttonLastAction();
			buttonGoToAction();
			sortAction();
			checkBoxSortAction();
	}
	
	//=================================================================
	//=================== actionListener methods ======================
	
	// Open file
	private void openFileAction() {
		mntmOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));	// CTRL O key-bind
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0){
				JFileChooser jFileChooser = new JFileChooser(System.getProperty("user.dir"));
				jFileChooser.setFileFilter(new FileNameExtensionFilter("CSV files (.csv)", "csv"));
				jFileChooser.setAcceptAllFileFilterUsed(false);
				jFileChooser.setDialogTitle("Open File");
					
				if(jFileChooser.showOpenDialog(contentPane) == JFileChooser.APPROVE_OPTION){ 
					try {
						csvFile = jFileChooser.getSelectedFile();
						FileParser f = new FileParser(csvFile);
						customers = f.getCustomers();
						unsortedCustomers = customers.clone();	// create a copy of the original array
					} catch(Exception e) {
						showErrorDialog();	// error message in case the csv file is not properly formated
					}
					index = 0;
					displayCustomer(customers[index]);
				}
			}
		});
	}
	
	// Clear customers
	private void clearAction() {
		mntmClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0){
				customers = null;
				clearCustomer();
			}
		});
	}
	
	// Exit
	private void exitAction() {
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0){
				 System.exit(0);
			}
		});
	}
	
	// About
	private void aboutAction() {
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0){
				showAboutMessage();
			}
		});
	}
	
	// Generate HTML directory
	private void htmlExport() {
		mntmHTML.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(customers == null){} // do nothing
				else try {
					if(new File("HTML Directory").exists()) {			// checks if the directory already exists
						if(showDuplicateDirectoryFoundMessage() == 1){	// prompts user for override
							HtmlExport htmlExport = new HtmlExport();
							htmlExport.generateDirectory(csvFile);		// generates directory if confirmed
							showSuccessMessage();						// directory was successfully generated
						}
					}
					else {	// directory doesn't already exist
						HtmlExport htmlExport = new HtmlExport();
						htmlExport.generateDirectory(csvFile);			// generates directory
						showSuccessMessage();							// directory was successfully generated
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	//=================================================================
	
	// Search button
	private void buttonSearchAction() {
		buttonSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(customers == null){} // do nothing
				else {
					if(textFieldSearch.getText().isEmpty()){} // do nothing
					else {
						int previousIndex = index;										// catches position within the customers array
						String inputSearch = textFieldSearch.getText().toLowerCase();	// get the input string from textFieldSearch
						String[] tokens = inputSearch.split(" ");						// splits user input in case first and last names are entered
						selectedField = (String) sortComboBox.getSelectedItem();		// get the selected field from JComboBox
						index = -1;
						
						/*
						 * I broke the search code once I updated the class with the ability
						 * to revert back to the unsorted array. If I tried searching using 
						 * any of the 12 customers fields, after having sorted the array and 
						 * then reverting back to its unsorted state, the displayed customer
						 * would be completely incorrect (having none of the fields matching
						 * the user input). While not very elegant, I solved the problem with
						 * the following if statement until I have more time to figure it out
						 * without the pressure of the deadline.
						 */
						
						if(selectedField == "None"){
							try {
								FileParser f = new FileParser(csvFile);
								customers = f.getCustomers();
							} catch(Exception e) {
								showErrorDialog();	// error message
							}
						}
						
						for(int i=0; i<customers.length; i++){
							if(inputSearch.equals(customers[i].getFirstName().toLowerCase()) 			// first name
									|| inputSearch.equals(customers[i].getLastName().toLowerCase()) 	// last name
									|| inputSearch.equals(customers[i].getCompany().toLowerCase())		// company
									|| inputSearch.equals(customers[i].getAddress().toLowerCase()) 		// address
									|| inputSearch.equals(customers[i].getCity().toLowerCase()) 		// city
									|| inputSearch.equals(customers[i].getCounty().toLowerCase())		// county
									|| inputSearch.equals(customers[i].getState().toLowerCase()) 		// state
									|| inputSearch.equals(customers[i].getZip().toLowerCase())			// zip 
									|| inputSearch.equals(customers[i].getPhone().toLowerCase())		// phone
									|| inputSearch.equals(customers[i].getFax().toLowerCase()) 			// fax
									|| inputSearch.equals(customers[i].getEmail().toLowerCase()) 		// email
									|| inputSearch.equals(customers[i].getWeb().toLowerCase())			// web
									|| (tokens[0].equals(customers[i].getFirstName().toLowerCase()) && tokens[1].equals(customers[i].getLastName().toLowerCase()))){	// first and last name
									index = i;
									break;
							}
						}
						
						if(index == -1){
							index = previousIndex;
							customerNotFound.setText("Customer not found!");
						}
						else {
							displayCustomer(customers[index]);
						}
					}
				}
			}
		});	
	}
	
	// Jump to next or previous customers using scroll wheel
	private void mouseWheelAction() {
		contentPane.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				if(customers == null){} // do nothing
				else {
					int wheelRotation = e.getWheelRotation();
					if(wheelRotation > 0) {	// previous customer
						if(index <= 0) index = 0;
						else index--;
						displayCustomer(customers[index]);
					}
					
					if(wheelRotation < 0) {	// next customer
						if(index >= customers.length-1) index = customers.length-1;
						else index++;
						displayCustomer(customers[index]);
					}
				}
			}
		});
	}
	
	// Jump to first customer button
	private void buttonFirstAction() {
		buttonFirst.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(customers == null){} // do nothing
				else {
					index = 0;
					displayCustomer(customers[index]);
				}
			}
		});
	}
	
	// Jump to previous customer button
	private void buttonPreviousAction() {
		buttonPrevious.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(customers == null){} // do nothing
				else {
					if(index <= 0) index = 0;
					else index--;
					displayCustomer(customers[index]);
				}
			}
		});
	}
	
	// Jump to next customer button
	private void buttonNextAction() {
		buttonNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(customers == null){} // do nothing
				else {
					if(index >= customers.length-1) index = customers.length-1;
					else index++;
					displayCustomer(customers[index]);
				}
			}
		});
	}

	// Jump to last customer button
	private void buttonLastAction() {
		buttonLast.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(customers == null){} // do nothing
				else {
					index = customers.length-1;
					displayCustomer(customers[index]);
				}
			}
		});
	}

	// Jump to specific customer button
	private void buttonGoToAction() {
		buttonGoTo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(customers == null){} // do nothing
				else {
					if(textFieldGoToRecord.getText().isEmpty()){} // do nothing
					else if(textFieldGoToRecord.getText().matches("\\d+")) {	// checks if the input is an integer
						int inputGoToRecord = Integer.parseInt(textFieldGoToRecord.getText());
						
						if(inputGoToRecord <= 0 || inputGoToRecord > customers.length) txtRecordOf.setText("Invalid Input");	// checks if input is not within bounds
						else {
							index = inputGoToRecord-1;
							displayCustomer(customers[index]);
						}
					}
					else txtRecordOf.setText("Invalid Input");	// invalid input for all values differant than an integer
				}
			}
		});
	}
	
	//=================================================================
	
	// Sort customers
	private void sortAction() {
		sortComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedField = (String) sortComboBox.getSelectedItem(); //get the selected field from JComboBox
				index = 0;
				
				if(customers == null){} // do nothing
				else {
					switch (selectedField) {
					case "None":		// displays the unsorted array of customers
						displayCustomer(customers[index]);
						break;
					case "First Name":	// sort by First Name
						if(chckbxDescending.isSelected()) Arrays.sort(customers, new FirstNameComparator(SortOrder.descending));
						else Arrays.sort(customers, new FirstNameComparator(SortOrder.ascending));
						displayCustomer(customers[index]);
						break;
					case "Last Name":	// sort by Last Name
						if(chckbxDescending.isSelected()) Arrays.sort(customers, new LastNameComparator(SortOrder.descending));
						else Arrays.sort(customers, new LastNameComparator(SortOrder.ascending));
						displayCustomer(customers[index]);
						break;
					case "Company":		// sort by Company
						if(chckbxDescending.isSelected()) Arrays.sort(customers, new CompanyComparator(SortOrder.descending));
						else Arrays.sort(customers, new CompanyComparator(SortOrder.ascending));
						displayCustomer(customers[index]);
						break;
					case "Address":		// sort by Address
						if(chckbxDescending.isSelected()) Arrays.sort(customers, new AddressComparator(SortOrder.descending));
						else Arrays.sort(customers, new AddressComparator(SortOrder.ascending));
						displayCustomer(customers[index]);
						break;
					case "City":		// sort by City
						if(chckbxDescending.isSelected()) Arrays.sort(customers, new CityComparator(SortOrder.descending));
						else Arrays.sort(customers, new CityComparator(SortOrder.ascending));
						displayCustomer(customers[index]);
						break;
					case "County":		// sort by County
						if(chckbxDescending.isSelected()) Arrays.sort(customers, new CountyComparator(SortOrder.descending));
						else Arrays.sort(customers, new CountyComparator(SortOrder.ascending));
						displayCustomer(customers[index]);
						break;
					case "State":		// sort by State
						if(chckbxDescending.isSelected()) Arrays.sort(customers, new StateComparator(SortOrder.descending));
						else Arrays.sort(customers, new StateComparator(SortOrder.ascending));
						displayCustomer(customers[index]);
						break;
					case "ZIP":			// sort by ZIP
						if(chckbxDescending.isSelected()) Arrays.sort(customers, new ZipComparator(SortOrder.descending));
						else Arrays.sort(customers, new ZipComparator(SortOrder.ascending));
						displayCustomer(customers[index]);
						break;
					case "Phone":		// sort by Phone
						if(chckbxDescending.isSelected()) Arrays.sort(customers, new PhoneComparator(SortOrder.descending));
						else Arrays.sort(customers, new PhoneComparator(SortOrder.ascending));
						displayCustomer(customers[index]);
						break;
					case "Fax":			// sort by Fax
						if(chckbxDescending.isSelected()) Arrays.sort(customers, new FaxComparator(SortOrder.descending));
						else Arrays.sort(customers, new FaxComparator(SortOrder.ascending));
						displayCustomer(customers[index]);
						break;
					case "Email":		// sort by Email
						if(chckbxDescending.isSelected()) Arrays.sort(customers, new EmailComparator(SortOrder.descending));
						else Arrays.sort(customers, new EmailComparator(SortOrder.ascending));
						displayCustomer(customers[index]);
						break;
					case "Web":			// sort by Web
						if(chckbxDescending.isSelected()) Arrays.sort(customers, new WebComparator(SortOrder.descending));
						else Arrays.sort(customers, new WebComparator(SortOrder.ascending));
						displayCustomer(customers[index]);
						break;
					}
				}
			}
		});
	}
	
	// Descending sort
	private void checkBoxSortAction() {
		chckbxDescending.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedField = (String) sortComboBox.getSelectedItem(); //get the selected field from JComboBox
				index = 0;
				
				if(customers == null){} // do nothing
				else {
					switch (selectedField) {
					case "None":		// displays the unsorted array of customers
						displayCustomer(customers[index]);
						break;
					case "First Name":	// sort by First Name
						if(chckbxDescending.isSelected()) Arrays.sort(customers, new FirstNameComparator(SortOrder.descending));
						else Arrays.sort(customers, new FirstNameComparator(SortOrder.ascending));
						displayCustomer(customers[index]);
						break;
					case "Last Name":	// sort by Last Name
						if(chckbxDescending.isSelected()) Arrays.sort(customers, new LastNameComparator(SortOrder.descending));
						else Arrays.sort(customers, new LastNameComparator(SortOrder.ascending));
						displayCustomer(customers[index]);
						break;
					case "Company":		// sort by Company
						if(chckbxDescending.isSelected()) Arrays.sort(customers, new CompanyComparator(SortOrder.descending));
						else Arrays.sort(customers, new CompanyComparator(SortOrder.ascending));
						displayCustomer(customers[index]);
						break;
					case "Address":		// sort by Address
						if(chckbxDescending.isSelected()) Arrays.sort(customers, new AddressComparator(SortOrder.descending));
						else Arrays.sort(customers, new AddressComparator(SortOrder.ascending));
						displayCustomer(customers[index]);
						break;
					case "City":		// sort by City
						if(chckbxDescending.isSelected()) Arrays.sort(customers, new CityComparator(SortOrder.descending));
						else Arrays.sort(customers, new CityComparator(SortOrder.ascending));
						displayCustomer(customers[index]);
						break;
					case "County":		// sort by County
						if(chckbxDescending.isSelected()) Arrays.sort(customers, new CountyComparator(SortOrder.descending));
						else Arrays.sort(customers, new CountyComparator(SortOrder.ascending));
						displayCustomer(customers[index]);
						break;
					case "State":		// sort by State
						if(chckbxDescending.isSelected()) Arrays.sort(customers, new StateComparator(SortOrder.descending));
						else Arrays.sort(customers, new StateComparator(SortOrder.ascending));
						displayCustomer(customers[index]);
						break;
					case "ZIP":			// sort by ZIP
						if(chckbxDescending.isSelected()) Arrays.sort(customers, new ZipComparator(SortOrder.descending));
						else Arrays.sort(customers, new ZipComparator(SortOrder.ascending));
						displayCustomer(customers[index]);
						break;
					case "Phone":		// sort by Phone
						if(chckbxDescending.isSelected()) Arrays.sort(customers, new PhoneComparator(SortOrder.descending));
						else Arrays.sort(customers, new PhoneComparator(SortOrder.ascending));
						displayCustomer(customers[index]);
						break;
					case "Fax":			// sort by Fax
						if(chckbxDescending.isSelected()) Arrays.sort(customers, new FaxComparator(SortOrder.descending));
						else Arrays.sort(customers, new FaxComparator(SortOrder.ascending));
						displayCustomer(customers[index]);
						break;
					case "Email":		// sort by Email
						if(chckbxDescending.isSelected()) Arrays.sort(customers, new EmailComparator(SortOrder.descending));
						else Arrays.sort(customers, new EmailComparator(SortOrder.ascending));
						displayCustomer(customers[index]);
						break;
					case "Web":			// sort by Web
						if(chckbxDescending.isSelected()) Arrays.sort(customers, new WebComparator(SortOrder.descending));
						else Arrays.sort(customers, new WebComparator(SortOrder.ascending));
						displayCustomer(customers[index]);
						break;
					}
				}
			}
		});
	}
	
	
	//=================================================================
	//=================== private helper methods ======================
	
	// Decides whether to display a customer from the unsorted array
	private void displayCustomer(Customer c) {
		if(selectedField == "None") displayCustomerHelper(unsortedCustomers[index]);
		else displayCustomerHelper(customers[index]);
	}
	
	// Displays customer information in the corresponding text fields
	private void displayCustomerHelper(Customer c) {
		textFieldFirstName.setText(c.getFirstName());
		textFieldLastName.setText(c.getLastName());
		textFieldCompany.setText(c.getCompany());
		textFieldAddress.setText(c.getAddress());
		textFieldCity.setText(c.getCity());
		textFieldCounty.setText(c.getCounty());
		textFieldState.setText(c.getState());
		textFieldZip.setText(c.getZip());
		textFieldPhone.setText(c.getPhone());
		textFieldFax.setText(c.getFax());
		textFieldEmail.setText(c.getEmail());
		textFieldWeb.setText(c.getWeb());
		int displayedRecord = index + 1;
		txtRecordOf.setText("Record " + displayedRecord + " of " + customers.length);	// Updates txtRecordOf
		clearUserInput();
	}
	
	// Clears the customer text fields
	private void clearCustomer() {
			textFieldFirstName.setText("");
			textFieldLastName.setText("");
			textFieldCompany.setText("");
			textFieldAddress.setText("");
			textFieldCity.setText("");
			textFieldCounty.setText("");
			textFieldState.setText("");
			textFieldZip.setText("");
			textFieldPhone.setText("");
			textFieldFax.setText("");
			textFieldEmail.setText("");
			textFieldWeb.setText("");
			txtRecordOf.setText("");
	}
	
	// Clears all possible user input
	private void clearUserInput() {
		textFieldGoToRecord.setText("");
		textFieldSearch.setText("");
		customerNotFound.setText("");
	}
	
	// Error dialog (in case the user has opted to select a non-valid file)
	// UPDATE: not very useful now that the "All Files" option is disabled.
	// Kept it in case the .csv file is not properly formatted or is empty
	private void showErrorDialog() {
		String errorMessage = "An error has occured while trying to read specified the file.\nPlease make sure you are using a valid .csv file and try again.";			
		JOptionPane.showMessageDialog(contentPane, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	// Developer information dialog
	private void showAboutMessage() {
		String aboutMessage = "Developed by Jad Haidar\nVersion 2.5\nLast updated on 11/05/2015";
		JOptionPane.showMessageDialog(contentPane, aboutMessage, "About", JOptionPane.INFORMATION_MESSAGE);
	}
	
	// Displays confirmation message for Export HTML Directory and launches the index.html file
	private void showSuccessMessage() throws IOException {
		String successMage = "HTML directory has been successfully created.";
		JOptionPane.showMessageDialog(contentPane, successMage, "Success", JOptionPane.INFORMATION_MESSAGE);
		Desktop.getDesktop().browse(new File("index.html").toURI());
	}
	
	// Displays a confirmation prompt and returns which option was chosen (YES/NO)
	private int showDuplicateDirectoryFoundMessage() {
		String duplicatedDirectoryFound = "HTML directory already exists, do you want to override?";
		if(JOptionPane.showConfirmDialog(contentPane, duplicatedDirectoryFound, "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) return 1;
		else return 0;
		}

	//=================================================================
	//========================= end of code ===========================
}

