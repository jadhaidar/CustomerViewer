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

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class UserInterface extends JFrame {
	protected JPanel contentPane;
	protected JMenuBar menuBar;
	protected JMenu mnFile;
	protected JMenu mnHelp;
	protected JMenuItem mntmOpen;
	protected JMenuItem mntmReset;
	protected JMenuItem mntmClear;
	protected JMenuItem mntmExit;
	protected JMenuItem mntmAbout;
	protected JMenuItem mntmHTML;
	protected JButton buttonSearch;
	protected JButton buttonFirst;
	protected JButton buttonPrevious;
	protected JButton buttonNext;
	protected JButton buttonLast;
	protected JButton buttonGoTo;
	protected JLabel customerNotFound;
	protected JLabel lblSort;
	protected JComboBox sortComboBox;
	protected JCheckBox chckbxDescending;
	protected JTextField textFieldSearch;
	protected JTextField textFieldFirstName;
	protected JTextField textFieldLastName;
	protected JTextField textFieldCompany;
	protected JTextField textFieldAddress;
	protected JTextField textFieldCity;
	protected JTextField textFieldCounty;
	protected JTextField textFieldState;
	protected JTextField textFieldZip;
	protected JTextField textFieldPhone;
	protected JTextField textFieldFax;
	protected JTextField textFieldEmail;
	protected JTextField textFieldWeb;
	protected JTextField txtRecordOf;
	protected JTextField textFieldGoToRecord;
	protected String [] sortLabel = {"None", "First Name", "Last Name", "Company", "Address", "City", "County", "State", "ZIP", "Phone", "Fax", "Email", "Web"};
	
	//=================================================================
	//==================== overloaded constructor =====================
		
	public UserInterface() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setBounds(100, 100, 590, 590);
		setContentPane(contentPane);
		setLocationRelativeTo(null);
		setResizable(false);
		setTitle("Customer Viewer 2.5");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//======================== menu items =========================
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnFile = new JMenu("File");
		menuBar.add(mnFile);

		mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		mntmOpen = new JMenuItem("Open File");
		mnFile.add(mntmOpen);
		
		mntmHTML = new JMenuItem("Generate HTML Directory");
		mnFile.add(mntmHTML);
		
		mntmClear = new JMenuItem("Clear");
		mnFile.add(mntmClear);
		
		mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
		
		mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);
		
		//====================== search and sort ======================
		
		// Search input area
		textFieldSearch = new JTextField();
		textFieldSearch.setBounds(20, 15, 185, 23);
		contentPane.add(textFieldSearch);
		textFieldSearch.setColumns(10);
		
		// Hidden "Customer not found" error message
		customerNotFound = new JLabel("");
		customerNotFound.setBounds(21, 40, 140, 14);
		customerNotFound.setFont(new Font("Tahoma", Font.ITALIC, 11));
		customerNotFound.setForeground(Color.RED);
		contentPane.add(customerNotFound);
		
		// Search button
		buttonSearch = new JButton("Search");
		buttonSearch.setToolTipText("Displays the first customer with matching input data");
		buttonSearch.setBounds(210, 15, 75, 22);
		contentPane.add(buttonSearch);
		
		// Sort label
		lblSort = new JLabel("Sort");
		lblSort.setBounds(308, 15, 30, 23);
		contentPane.add(lblSort);
		
		// Sort options
		sortComboBox = new JComboBox(sortLabel);
		sortComboBox.setBounds(340, 15, 120, 23);
		contentPane.add(sortComboBox);
		
		// "Descending" checkbox
		chckbxDescending = new JCheckBox("Descending");
		chckbxDescending.setBounds(467, 15, 95, 23);
		contentPane.add(chckbxDescending);
		
		//====================== customer fields ======================
		
		//First name
		JLabel lblFirstName = new JLabel("First Name");
		lblFirstName.setBounds(20, 60, 95, 14);
		contentPane.add(lblFirstName);
		textFieldFirstName = new JTextField();
		textFieldFirstName.setColumns(10);
		textFieldFirstName.setBounds(20, 80, 267, 20);
		contentPane.add(textFieldFirstName);
		
		// Last name
		JLabel lblLastName = new JLabel("Last Name");
		lblLastName.setBounds(302, 60, 95, 14);
		contentPane.add(lblLastName);
		textFieldLastName = new JTextField();
		textFieldLastName.setColumns(10);
		textFieldLastName.setBounds(302, 80, 240, 20);
		contentPane.add(textFieldLastName);
		
		// Company
		JLabel lblCompany = new JLabel("Company");
		lblCompany.setBounds(20, 125, 72, 14);
		contentPane.add(lblCompany);
		textFieldCompany = new JTextField();
		textFieldCompany.setColumns(10);
		textFieldCompany.setBounds(20, 145, 267, 20);
		contentPane.add(textFieldCompany);

		// Address
		JLabel lblAddress = new JLabel("Address");
		lblAddress.setBounds(302, 125, 72, 14);
		contentPane.add(lblAddress);
		textFieldAddress = new JTextField();
		textFieldAddress.setColumns(10);
		textFieldAddress.setBounds(302, 145, 240, 20);
		contentPane.add(textFieldAddress);

		// City
		JLabel lblCity = new JLabel("City");
		lblCity.setBounds(20, 190, 72, 14);
		contentPane.add(lblCity);
		textFieldCity = new JTextField();
		textFieldCity.setColumns(10);
		textFieldCity.setBounds(20, 210, 267, 20);
		contentPane.add(textFieldCity);

		// County
		JLabel lblCountry = new JLabel("County");
		lblCountry.setBounds(302, 190, 80, 14);
		contentPane.add(lblCountry);
		textFieldCounty = new JTextField();
		textFieldCounty.setColumns(10);
		textFieldCounty.setBounds(302, 210, 240, 20);
		contentPane.add(textFieldCounty);

		// State
		JLabel lblState = new JLabel("State");
		lblState.setBounds(20, 255, 72, 14);
		contentPane.add(lblState);
		textFieldState = new JTextField();
		textFieldState.setColumns(10);
		textFieldState.setBounds(20, 275, 267, 20);
		contentPane.add(textFieldState);

		// ZIP
		JLabel lblZip = new JLabel("ZIP");
		lblZip.setBounds(302, 255, 95, 14);
		contentPane.add(lblZip);
		textFieldZip = new JTextField();
		textFieldZip.setColumns(10);
		textFieldZip.setBounds(302, 275, 240, 20);
		contentPane.add(textFieldZip);

		// Phone
		JLabel lblPhone = new JLabel("Phone");
		lblPhone.setBounds(20, 320, 72, 14);
		contentPane.add(lblPhone);
		textFieldPhone = new JTextField();
		textFieldPhone.setColumns(10);
		textFieldPhone.setBounds(20, 340, 267, 20);
		contentPane.add(textFieldPhone);

		// Fax
		JLabel lblFax = new JLabel("Fax");
		lblFax.setBounds(302, 320, 95, 14);
		contentPane.add(lblFax);
		textFieldFax = new JTextField();
		textFieldFax.setColumns(10);
		textFieldFax.setBounds(302, 340, 240, 20);
		contentPane.add(textFieldFax);

		// Email
		JLabel lblEmail = new JLabel("Email");
		lblEmail.setBounds(20, 385, 72, 14);
		contentPane.add(lblEmail);
		textFieldEmail = new JTextField();
		textFieldEmail.setColumns(10);
		textFieldEmail.setBounds(20, 405, 267, 20);
		contentPane.add(textFieldEmail);

		// Web
		JLabel lblWeb = new JLabel("Web");
		lblWeb.setBounds(302, 385, 95, 14);
		contentPane.add(lblWeb);
		textFieldWeb = new JTextField();
		textFieldWeb.setColumns(10);
		textFieldWeb.setBounds(302, 405, 240, 20);
		contentPane.add(textFieldWeb);
		
		//==================== customer navigation ====================
		
		// Jump to first customer button
		buttonFirst = new JButton("<<");
		buttonFirst.setToolTipText("First Customer");
		buttonFirst.setBounds(117, 450, 49, 23);
		contentPane.add(buttonFirst);
		
		// Jump to previous customer button
		buttonPrevious = new JButton("<");
		buttonPrevious.setToolTipText("Previous Customer");
		buttonPrevious.setBounds(166, 450, 49, 23);
		contentPane.add(buttonPrevious);
		
		// Jump to next customer button
		buttonNext = new JButton(">");
		buttonNext.setToolTipText("Next Customer");
		buttonNext.setBounds(215, 450, 48, 23);
		contentPane.add(buttonNext);
		
		// Jump to last customer button
		buttonLast = new JButton(">>");
		buttonLast.setToolTipText("Last Customer");
		buttonLast.setBounds(263, 450, 48, 23);
		contentPane.add(buttonLast);
		
		// Jump to specific customer input area
		textFieldGoToRecord = new JTextField();
		textFieldGoToRecord.setHorizontalAlignment(SwingConstants.CENTER);
		textFieldGoToRecord.setColumns(10);
		textFieldGoToRecord.setBounds(314, 450, 70, 24);
		contentPane.add(textFieldGoToRecord);
		
		// Jump to specific customer button
		buttonGoTo = new JButton("Go To");
		buttonGoTo.setBounds(386, 450, 70, 24);
		contentPane.add(buttonGoTo);
		
		// Customer record number
		txtRecordOf = new JTextField();
		txtRecordOf.setEditable(false);
		txtRecordOf.setHorizontalAlignment(SwingConstants.CENTER);
		txtRecordOf.setFont(new Font("Tahoma", Font.BOLD, 11));
		txtRecordOf.setColumns(10);
		txtRecordOf.setBounds(117, 481, 339, 25);
		contentPane.add(txtRecordOf);
	}
	
	//=================================================================
	//========================= end of code ===========================
}

