/**
 *	UI Design and Mobile App Development
 * 	Vasu Irneni
 *	Date of Start: 10th October 2014
 *	Made Open Source
 *	Provides basic functionalities of a Contact Manager.
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.*;

import java.awt.event.*;
import java.util.HashMap;

import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/**	ContactManager is the default or main class
 *	This has all the default functions
 *	Labels, Buttons, and their actions
 */
public class ContactManager extends JFrame{ 
	
    int selectedRow;
    boolean zeroErrors = true;
    boolean  editMode = false, fname=false,lname=false,gname=false,aname=false,cname=false,sname=false,
    zname=false,pname=false,ename=false,coname=false;
    int progressCount=0;

    /*
     * HashMap to get the contacts from data file.
     * */
    HashMap contactsHashMap = new HashMap();
    ContactUtility contactSelected = new ContactUtility();
    String errorMessage ="";
    JPanel mainPanel = new JPanel();    

/* 	labels are used to display names
	text fields are used to add and edit text
	labels panels - This is like a container
	panels: add, edit, delete
*/
    JLabel errorLabelDef = new JLabel("Status:");
	JLabel progressLabel = new JLabel("Progress");
	
	JProgressBar progressBar = new JProgressBar();
    JTextArea errorTextArea = new JTextArea("");

/*
 *  All buttons used in this program
 */
    JButton editButton = new JButton("Edit");
    JButton newButton = new JButton("New");
    JButton deleteButton = new JButton("Delete");
    JButton saveContact = new JButton("Save");
    JButton cancelEditContact = new JButton("Cancel");
    
/*
 *  Text fields used in the program are declared here.
 */
    JTextField fieldFName = new JTextField(20);
    JTextField fieldLName = new JTextField(20);
    JTextField fieldMName = new JTextField(1);
    JTextField fieldAddL1 = new JTextField(35);
    JTextField fieldAddL2 = new JTextField(35);
    JTextField fieldCity = new JTextField(25);
    JTextField fieldState = new JTextField(2);
    JTextField fieldPhNum = new JTextField(21);
    JTextField fieldGender = new JTextField(1);
    JTextField fieldZip = new JTextField(9);
    JTextField fieldEmail = new JTextField(100);
    JTextField fieldCountry = new JTextField(30);

/*
 * I used a default PINK to show the field entered is not in the correct format.
 * Although RED is preferred it is not matching with the UI design
 * Similarly White for default background
 */
    Color errorColor	= Color.PINK;
    Color defaultColor	= Color.WHITE;    

    
/*  Commented for testing purposes
 * 
 *  JLabel labelName = new JLabel("Name");
    JLabel labelAddL1 = new JLabel();
    JLabel labelAddL2 = new JLabel();
    JLabel labelCity = new JLabel("City");
    JLabel labelState = new JLabel("State");
    JLabel labelZipcode = new JLabel("Zip Code");
    JLabel labelPhoneNumber = new JLabel("Phone number");
    JLabel labelGender = new JLabel("Gender"); 
*/   

    
    /*
     * New object is called.
     */
    public static void main(String[] args) {
        new Assignment2();   
    }
    
    /*
     * Constructor 
     * implements layouts 
     * and some basic functionalities.
     */
    public Assignment2(){
        
    	/*
    	 * Initial function to be called to show the already present contacts in file. 
    	 */
        FileUtility.readFromFile(contactsHashMap);
        contactSelected = ((ContactUtility)contactsHashMap.values().toArray()[0]);

//        this.setBackground(defaultColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0,10,10,10));
        this.setSize(650,450);
        this.setMinimumSize(new Dimension(650, 450)); 
		
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setTitle("MY Contacts Manager");         

/* 
 * 		This below commented code sets the screen default from top to bottom.
 * 	
 *      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double height = screenSize.getHeight();
        int height1 = (int) height;
        this.setSize(650, height1);
*/        
        this.setLocationRelativeTo(null); 
        this.setLayout(new GridLayout(1, 1)); 
        
        try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        errorTextArea.setEditable(false);
        errorTextArea.setBackground(defaultColor);
        
        String columnNames[] = { "Name", "Phone Number"};
        final JTable tableObject = new JTable() {  
        	//Creating new table object.
            @Override
            public boolean isCellEditable(int row, int column) {               
                    return false;               
            };
        };
        
        final DefaultTableModel dtm = new DefaultTableModel(0, 0); 
        
        dtm.setColumnIdentifiers(columnNames);
        tableObject.setModel(dtm);  
        /*
         * Updates data from hashmap
         */
        for(Object contact: contactsHashMap.values())
        {
            dtm.addRow(new Object[]{ ((ContactUtility)contact).getFullName(), ((ContactUtility)contact).getPhoneNumber()});
        }
        
        tableObject.setSelectionMode(0);

        JScrollPane scrollPane = new JScrollPane(tableObject);
        this.add(scrollPane);
        
        /*
         * Adding mouse listener for table object
         * which onclicks highlights the code.
         */
        tableObject.addMouseListener(new MouseAdapter() { 
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                  deleteButton.setEnabled(true);
                  editMode = true;
                  progressCount=100;
                  progressBar.setValue(progressCount);
                  makeAllTrue();
                  JTable target = (JTable)e.getSource();
                  selectedRow = target.getSelectedRow();
                  String name = ((String)target.getValueAt(target.getSelectedRow(), 0)).toLowerCase();             
                  name = name.replaceAll("\\s","");
                  contactSelected = (ContactUtility)contactsHashMap.get(name);
                  updatePanel(); 
                  errorMessage = "";
                  errorTextArea.setText(errorMessage);
                  }
            }  
          });
               
        /*
         * This is fired on selecting Save button
         * To save the contact to file.
         * */
        saveContact.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String identifier = contactSelected.getIdentifier();
                
                String newIdentifier = fieldFName.getText().trim() + fieldMName.getText().trim() + fieldLName.getText().trim(); 
                
                /*
                 *	This is where the dataValidation function called 
                 */
                dataValidation();
                
                if(zeroErrors) 
                {
                	progressCount=0;
                	makeAllFalse();
                if(editMode){
                	errorMessage="";
                    editMode=false;
                   }
                else{
                        contactSelected = null;
                        identifier = null;
                	errorMessage = "Contact Saved Succesfully.";
                }
                errorTextArea.setText(errorMessage);
               	contactSelected = new ContactUtility(fieldFName.getText().trim(),
                fieldLName.getText().trim(),
                fieldMName.getText().trim(),
                fieldAddL1.getText().trim(),
                fieldAddL2.getText().trim(),
                fieldCity.getText().trim(),
                fieldState.getText().trim(),
                fieldZip.getText().trim(),
                fieldPhNum.getText().trim(),
                fieldGender.getText().trim(),
                fieldEmail.getText().trim(),
                fieldCountry.getText().trim());
                
                if(identifier!=null)
                    identifier = identifier.toLowerCase();
                newIdentifier = newIdentifier.toLowerCase();
                
                if(identifier!=null && newIdentifier!=null && !identifier.equals(newIdentifier))
                {
                    identifier = identifier.toLowerCase();
                    contactsHashMap.remove(identifier);
                    dtm.removeRow(selectedRow);
                    identifier = newIdentifier;
                    contactSelected.setIdentifier(identifier);
                    contactsHashMap.put(identifier, contactSelected);
                    dtm.addRow(new Object[]{ ((ContactUtility)contactsHashMap.get(identifier)).getFullName(), ((ContactUtility)contactsHashMap.get(identifier)).getPhoneNumber()});
                
                }
                else if(identifier==null)
                {
                    
                identifier = newIdentifier;
                contactSelected.setIdentifier(identifier);
                contactsHashMap.put(identifier, contactSelected);
                dtm.addRow(new Object[]{ ((ContactUtility)contactsHashMap.get(identifier)).getFullName(), ((ContactUtility)contactsHashMap.get(identifier)).getPhoneNumber()});
                }
                else
                {
                    contactsHashMap.remove(identifier);
                    dtm.setValueAt(fieldPhNum.getText().trim(), selectedRow, 1);
                    contactsHashMap.put(newIdentifier, contactSelected);
                    tableObject.clearSelection();
                }
                clearField();
            	editMode = false;
                updatePanel();  

                /*
                 * Once the hashmap is updated, it has to be written to file
                 */
                FileUtility.writeToFile(contactsHashMap);  
                
                }
            }
        });
        
        /*
         * This is fired when you wish to cancel the present edit action
         * And so the action is to camcel the txt fields accordingly.
         * */
        cancelEditContact.addActionListener(new ActionListener() { 

            @Override
            public void actionPerformed(ActionEvent e) {     
            	progressCount=0;
            	progressBar.setValue(progressCount);
            	makeAllFalse();
            	editMode = false;
            	clearField();
            	errorMessage="";
            	errorTextArea.setText("");
              }
        });
        
        
        /*
         *         
         * This is fired when new action is being performed.
         * This takes to the right most pane and a new window is set to finish the action
         * */
        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {   
            	progressCount=0;
            	progressBar.setValue(progressCount);
            	makeAllFalse();
            	clearField();
            	editMode = false;
            	deleteButton.setEnabled(false);
            	errorTextArea.setText("");
                contactSelected = new ContactUtility();
                updateEditPanel();
            }
        });
        
        /* 
         * Delete button is fired when user press delete
         * This deletes the contact from file and updates the left panel
         */
        deleteButton.addActionListener(new ActionListener() { 

            @Override
            public void actionPerformed(ActionEvent e) {
            	progressCount=0;
            	progressBar.setValue(progressCount);
            	makeAllFalse();
            	editMode = false;
            	contactsHashMap.remove(contactSelected.getIdentifier());
                contactSelected = ((ContactUtility)contactsHashMap.values().toArray()[0]);
                dtm.setRowCount(0);
                for(Object contact: contactsHashMap.values())
                {
                    dtm.addRow(new Object[]{ ((ContactUtility)contact).getFullName(), ((ContactUtility)contact).getPhoneNumber()});
                }
                updatePanel();  
                FileUtility.writeToFile(contactsHashMap); 
            }
        });
        mainPanel.setLayout(new GridBagLayout());
        
        /*
         * 	For Progrress bar validation and listener
         */
        fieldFName.addFocusListener(new FocusListener() {

            @Override
            public void focusLost(FocusEvent arg0) {
            	String val=fieldFName.getSelectedText();
            	if(fieldFName.getText().trim() == null || !fieldFName.getText().trim().matches("[a-zA-z]+"))
                {
            		if(fname){
            	    	progressCount-=10;
            	    	progressBar.setValue(progressCount);
                        fname=false;            	   
            	    }
            		fieldFName.setBackground(errorColor);
            	    errorTextArea.setText("First Name Is either Blank or Invalid.");
            	    
                    
                }else
                {
                	if(fname==false){
                		progressCount+=10;
                
                    	progressBar.setValue(progressCount);
                    	fname=true;
                	}                	
                	fieldFName.setBackground(defaultColor);
                }

            }
            @Override
            public void focusGained(FocusEvent arg0) {
            //	firstnameField.setText("");
            }
        });

/*
 *  validation for last name field        
 */
        fieldLName.addFocusListener(new FocusListener() {

            @Override
            public void focusLost(FocusEvent arg0) {
            	String val=fieldLName.getSelectedText();
            	if(fieldLName.getText().trim() == null || !fieldLName.getText().trim().matches("[a-zA-z]+"))
                {

            	   if(lname){
            	    	progressCount-=10;
            	    	progressBar.setValue(progressCount);
                        lname=false;            	   
            	    }
            		fieldLName.setBackground(errorColor);
            	    errorTextArea.setText("Last Name Is either Blank or invalid");
            	    
                }else
                {
                	if(lname==false){
                		progressCount+=10;
                		fieldLName.setBackground(defaultColor);
                    	progressBar.setValue(progressCount);
                    	lname=true;
                	}                	
                }

            }
            @Override
            public void focusGained(FocusEvent arg0) {
            //	firstnameField.setText("");
            }
        });


        /*
         * Address 1 field data validation
         */
        fieldAddL1.addFocusListener(new FocusListener() {

            @Override
            public void focusLost(FocusEvent arg0) {
            	String val=fieldAddL1.getSelectedText();
            	if(fieldAddL1.getText().trim() == null ||  fieldAddL1.getText().trim().isEmpty())
                {
            		if(aname){
            	    	progressCount-=10;
            	    	progressBar.setValue(progressCount);
                        aname=false;            	   
            	    }
            		fieldAddL1.setBackground(errorColor);
            	    errorTextArea.setText("Addressline 1 is either Blank or Invalid.");
                }else
                {
                	if(aname==false){
                		progressCount+=10;
                		fieldAddL1.setBackground(defaultColor);
                    	progressBar.setValue(progressCount);
                    	aname=true;
                	}                	
                }

            }
            @Override
            public void focusGained(FocusEvent arg0) {
                // TODO Auto-generated method stub
            //	firstnameField.setText("");
            }
        });
        
/*
 * Validation for Gender field
 */
        fieldGender.addFocusListener(new FocusListener() {

            @Override
            public void focusLost(FocusEvent arg0) {
            	String val=fieldGender.getSelectedText();
            	if(fieldGender.getText().trim()== null || !fieldGender.getText().trim().matches("[MmFf]")|| fieldGender.getText().trim().isEmpty() )
                {
            		if(gname){
            	    	progressCount-=10;
            	    	progressBar.setValue(progressCount);
                        gname=false;            	   
            	    }
            		fieldGender.setBackground(errorColor);
            	    errorTextArea.setText("Gender field should be either M or F. \n Cannot be empty!");
                          }else
                {
                	if(gname==false){
                		progressCount+=10;
                		fieldGender.setBackground(defaultColor);
                    	progressBar.setValue(progressCount);
                    	gname=true;
                	}                	
                }

            }
            @Override
            public void focusGained(FocusEvent arg0) {
            //	firstnameField.setText("");
            }
        });

        /*
         * City data validation
         */
        fieldCity.addFocusListener(new FocusListener() {

            @Override
            public void focusLost(FocusEvent arg0) {
                // TODO Auto-generated method stub
            	String val=fieldCity.getSelectedText();
            	if(fieldCity.getText().trim()== null || !fieldCity.getText().trim().matches("[a-zA-Z]+") || fieldCity.getText().trim().isEmpty() )
                {
            		if(cname){
            	    	progressCount-=10;
            	    	progressBar.setValue(progressCount);
                        cname=false;            	   
            	    }
            		fieldCity.setBackground(errorColor);
            	    errorTextArea.setText("City field is either Empty or Invalid.");            	    
                }else
                {
                	if(cname==false){
                		progressCount+=10;
                		fieldCity.setBackground(defaultColor);
                    	progressBar.setValue(progressCount);
                    	cname=true;
                	}                	
                }

            }
            @Override
            public void focusGained(FocusEvent arg0) {
            //	firstnameField.setText("");
            }
        });
        
        fieldState.addFocusListener(new FocusListener() {

            @Override
            public void focusLost(FocusEvent arg0) {
                // TODO Auto-generated method stub
            	String val=fieldState.getSelectedText();
            	if(fieldState.getText().trim()== null || !fieldState.getText().trim().matches("[a-zA-Z]|[a-zA-Z][a-zA-Z]") || fieldState.getText().trim().isEmpty())
                {
            		if(sname){
            	    	progressCount-=10;
            	    	progressBar.setValue(progressCount);
                        sname=false;            	   
            	    }
            		fieldState.setBackground(errorColor);
            	    errorTextArea.setText("State should be 2 characters only. \n Cannot be empty!");
            	    
                }else
                {
                	if(sname==false){
                		progressCount+=10;
                		fieldState.setBackground(defaultColor);
                    	progressBar.setValue(progressCount);
                    	sname=true;
                	}                	
                }

            }
            @Override
            public void focusGained(FocusEvent arg0) {
            //	firstnameField.setText("");
            }
        });
/*
 *  Zip field data validation       
 */
        fieldZip.addFocusListener(new FocusListener() {

            @Override
            public void focusLost(FocusEvent arg0) {
                // TODO Auto-generated method stub
            	String val=fieldZip.getSelectedText();
            	if(fieldZip.getText().trim()== null || !fieldZip.getText().trim().matches("[0-9]+")|| fieldZip.getText().trim().isEmpty() )
                {
                    
            		if(zname){
            	    	progressCount-=10;
            	    	progressBar.setValue(progressCount);
                        zname=false;            	   
            	    }
            		fieldZip.setBackground(errorColor);
            	    errorTextArea.setText("Zipcode should be numerical only");
            	    
                              }else
                {
                	if(zname==false){
                		progressCount+=10;
                		fieldZip.setBackground(defaultColor);
                    	progressBar.setValue(progressCount);
                    	zname=true;
                	}                	
                }

            }
            @Override
            public void focusGained(FocusEvent arg0) {
                // TODO Auto-generated method stub
            //	firstnameField.setText("");
            }
        });
        /*
         * Phone number data vaidation
         */
        fieldPhNum.addFocusListener(new FocusListener() {

            @Override
            public void focusLost(FocusEvent arg0) {
                // TODO Auto-generated method stub
               	String val=fieldPhNum.getSelectedText();
           
            	if(fieldPhNum.getText().length()==0||fieldPhNum.getText().trim()== null || !fieldPhNum.getText().trim().matches("[0-9]+") || fieldPhNum.getText().trim().isEmpty() )
                {
            		if(pname){
            	    	progressCount-=10;
            	    	progressBar.setValue(progressCount);
                        pname=false;            	   
            	    }
            		fieldPhNum.setBackground(errorColor);
            	    errorTextArea.setText("Phone number should be numerical only");
            	    
                }else
                {
                	if(pname==false){
                		progressCount+=10;
                		fieldPhNum.setBackground(defaultColor);
                    	progressBar.setValue(progressCount);
                    	pname=true;
                	}                	
                }

            }
            @Override
            public void focusGained(FocusEvent arg0) {
                // TODO Auto-generated method stub
            //	firstnameField.setText("");
            }
        });

        /*
         * Email validation
         * of the form a@a.com
         */
        fieldEmail.addFocusListener(new FocusListener() {

            @Override
            public void focusLost(FocusEvent arg0) {
                // TODO Auto-generated method stub
            	String val=fieldEmail.getSelectedText();
            	if(fieldEmail.getText().trim()== null || !fieldEmail.getText().trim().matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")|| fieldEmail.getText().trim().isEmpty() )
                {
                    //error
            		if(ename){
            	    	progressCount-=10;
            	    	progressBar.setValue(progressCount);
                        ename=false;            	   
            	    }
            		fieldEmail.setBackground(errorColor);
            	    errorTextArea.setText("Email is either empty or invalid.");
            	    
                }else
                {
                	if(ename==false){
                		progressCount+=10;
                		fieldEmail.setBackground(defaultColor);
                    	progressBar.setValue(progressCount);
                    	ename=true;
                	}                	
                }

            }
            @Override
            public void focusGained(FocusEvent arg0) {
            //	firstnameField.setText("");
            }
        });

        /*
         *  Country Data Validation
         */
        fieldCountry.addFocusListener(new FocusListener() {

            @Override
            public void focusLost(FocusEvent arg0) {
                // TODO Auto-generated method stub
            	String val=fieldCountry.getSelectedText();
            	if(fieldCountry.getText().trim()== null || !fieldCountry.getText().trim().matches("[a-zA-Z]+")|| fieldCountry.getText().trim().isEmpty())
                {
                    //error
            		if(coname){
            	    	progressCount-=10;
            	    	progressBar.setValue(progressCount);
                        coname=false;            	   
            	    }
            		fieldCountry.setBackground(errorColor);
            	    errorTextArea.setText("Country is either empty or invalid.");
            	    
                }else
                {
                	if(coname==false){
                		progressCount+=10;
                		fieldCountry.setBackground(defaultColor);
                    	progressBar.setValue(progressCount);
                    	coname=true;
                	}                	
                }

            }
            @Override
            public void focusGained(FocusEvent arg0) {
                // TODO Auto-generated method stub
            //	firstnameField.setText("");
            }
        });
            
        
//        setUpPanelFirstRead();
        setUpEditPanel();
        this.add(mainPanel);
       this.setVisible(true);
	}
    
/*    
 * Commented for testing purposes
    private void setUpPanelFirstRead() {
    	labelName.setText(contactSelected.getFirstName() + " "+ contactSelected.getMiddleInitial() + " " + contactSelected.getLastName());
    	labelAddL1.setText(contactSelected.getAddressLine1());
    	labelAddL2.setText(contactSelected.getAddressLine2());
    	labelCity.setText(contactSelected.getCity());
    	labelState.setText(contactSelected.getState());
    	labelZipcode.setText(contactSelected.getZipcode());
    	labelPhoneNumber.setText(contactSelected.getPhoneNumber());
    	labelGender.setText(contactSelected.getGender());
        
    }*/

    
    /*
     * This is to clear all the fields.
     *  Can be fired on cancel button or mouse change mode
     */
    private void clearField(){

    	fieldFName.setText("");
    	fieldFName.setBackground(defaultColor);
    	 fieldLName.setText("");
    	 fieldLName.setBackground(defaultColor);
         fieldMName.setText("");
         fieldMName.setBackground(defaultColor);
         fieldAddL1.setText("");
         fieldAddL1.setBackground(defaultColor);
         fieldAddL2.setText("");
         fieldAddL2.setBackground(defaultColor);
         fieldCity.setText("");
         fieldCity.setBackground(defaultColor);
         fieldState.setText("");
         fieldState.setBackground(defaultColor);
         fieldZip.setText("");
         fieldZip.setBackground(defaultColor);
         fieldPhNum.setText("");
         fieldPhNum.setBackground(defaultColor);
         fieldGender.setText("");
         fieldGender.setBackground(defaultColor);
         fieldCountry.setText("");
         fieldCountry.setBackground(defaultColor);
         fieldEmail.setText("");
         fieldEmail.setBackground(defaultColor);
    }
    
    /*
     * Updates the panel with selected row from table.
     */
    private void updatePanel(){ 
    	fieldFName.setText(contactSelected.getFirstName());
        fieldLName.setText(contactSelected.getLastName());
        fieldMName.setText(contactSelected.getMiddleInitial());
        fieldAddL1.setText(contactSelected.getAddressLine1());
        fieldAddL2.setText(contactSelected.getAddressLine2());
        fieldCity.setText(contactSelected.getCity());
        fieldState.setText(contactSelected.getState());
        fieldZip.setText(contactSelected.getZipcode());
        fieldPhNum.setText(contactSelected.getPhoneNumber());
        fieldGender.setText(contactSelected.getGender());
        fieldCountry.setText(contactSelected.getCountry());
        fieldEmail.setText(contactSelected.getEmail());
        
    }

    /*
     * To extract all the information of selected row and 
     * gives default edit functionality.
     */
    private void setUpEditPanel() {  
    	fieldFName.setText(contactSelected.getFirstName());
        fieldLName.setText(contactSelected.getLastName());
        fieldMName.setText(contactSelected.getMiddleInitial());
        fieldAddL1.setText(contactSelected.getAddressLine1());
        fieldAddL2.setText(contactSelected.getAddressLine2());
        fieldCity.setText(contactSelected.getCity());
        fieldZip.setText(contactSelected.getZipcode());
        fieldPhNum.setText(contactSelected.getPhoneNumber());
        fieldState.setText(contactSelected.getState());
        fieldGender.setText(contactSelected.getGender());
        
        //set the field limits from Form Utility
    	fieldFName.setDocument(new FormUtility(20));
		fieldLName.setDocument(new FormUtility(20));
		fieldMName.setDocument(new FormUtility(1));
		fieldCity.setDocument(new FormUtility(25));
		fieldAddL1.setDocument(new FormUtility(35));
		fieldAddL2.setDocument(new FormUtility(35));
		fieldState.setDocument(new FormUtility(2));
		fieldCountry.setDocument(new FormUtility(30));
		fieldEmail.setDocument(new FormUtility(100));
		fieldZip.setDocument(new FormUtility(9));
		fieldPhNum.setDocument(new FormUtility(21));
		fieldGender.setDocument(new FormUtility(1));
	
        // form used from here
        JPanel form = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(form, BorderLayout.NORTH);

        form.setLayout(new GridBagLayout());
        FormUtility formUtility = new FormUtility();

        //progress bar and error text area
        formUtility.addLabel(progressLabel, form);
        formUtility.addLastField(progressBar, form);

        formUtility.addLabel(errorLabelDef, form);
        formUtility.addLastField(errorTextArea, form);
        
        formUtility.addLastField(new JPanel(), form);

        formUtility.addLabel("First Name: ", form);
        formUtility.addLastField(fieldFName, form);

        formUtility.addLabel("Last Name: ", form);
        formUtility.addLastField(fieldLName, form);
        
        // gender and middle initial
        formUtility.addLabel("Middle Initial: ", form);
        formUtility.addLastField(fieldMName, form);
        
        formUtility.addLabel("Gender: ", form);
        formUtility.addLastField(fieldGender, form);
        
        
        formUtility.addLabel("Address Line1: ", form);
        formUtility.addLastField(fieldAddL1, form);

        formUtility.addLabel("Address Line2", form);
        formUtility.addLastField(fieldAddL2, form);

        formUtility.addLabel("City: ", form);
          
        Dimension citySize = fieldCity.getPreferredSize();
        citySize.width = 100;
        fieldCity.setPreferredSize(citySize);
        JPanel cityPanel = new JPanel();
        cityPanel.setLayout(new BorderLayout());
        cityPanel.add(fieldCity, BorderLayout.WEST);
        formUtility.addMiddleField(cityPanel, form);
        
        formUtility.addLastField(new JPanel(), form);

        formUtility.addLabel("State: ", form);
      
        Dimension stateSize = fieldState.getPreferredSize();
        stateSize.width = 100;
        fieldCity.setPreferredSize(stateSize);
        JPanel statePanel = new JPanel();
        statePanel.setLayout(new BorderLayout());
        statePanel.add(fieldState, BorderLayout.WEST);
        formUtility.addMiddleField(statePanel, form);
  
        formUtility.addLastField(new JPanel(), form);	//this gets to new line
        
        formUtility.addLabel("Zip: ", form);
        Dimension zipSize = fieldZip.getPreferredSize();
        zipSize.width = 100;
        fieldZip.setPreferredSize(zipSize);
        JPanel zipPanel = new JPanel();
        zipPanel.setLayout(new BorderLayout());
        zipPanel.add(fieldZip, BorderLayout.WEST);
        formUtility.addMiddleField(zipPanel, form);
        
        formUtility.addLastField(new JPanel(), form);

        formUtility.addLabel("Phone: ", form);
        Dimension phoneSize = fieldPhNum.getPreferredSize();
        phoneSize.width = 200;
        fieldPhNum.setPreferredSize(phoneSize);
        JPanel phonePanel = new JPanel();
        phonePanel.setLayout(new BorderLayout());
        phonePanel.add(fieldPhNum, BorderLayout.WEST);
        formUtility.addLastField(phonePanel, form);

        formUtility.addLabel("Email: ", form);
        Dimension emailSize = fieldEmail.getPreferredSize();
        emailSize.width = 200;
        fieldEmail.setPreferredSize(emailSize);
        JPanel emailPanel = new JPanel();
        emailPanel.setLayout(new BorderLayout());
        emailPanel.add(fieldEmail, BorderLayout.WEST);
        formUtility.addLastField(emailPanel, form);
        
        formUtility.addLabel("Country: ", form);
        formUtility.addLastField(fieldCountry, form);

        formUtility.addLastField(new JPanel(), form);
        
 /*       
  * 	Tried different layout for buttons
  * 
  * 
  * 	Dimension buttonSize = saveContact.getPreferredSize();
        buttonSize.width = 90;
        saveContact.setPreferredSize(buttonSize);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.add(saveContact, BorderLayout.WEST);

        Dimension buttonSize1 = cancelEditContact.getPreferredSize();
        buttonSize1.width = 90;
        cancelEditContact.setPreferredSize(buttonSize1);
        JPanel buttonPanel2 = new JPanel();
        buttonPanel2.setLayout(new BorderLayout());
        buttonPanel2.add(cancelEditContact, BorderLayout.WEST);


        buttonPanel.add(newButton, BorderLayout.WEST);
        buttonPanel.add(deleteButton, BorderLayout.EAST);
*/
        
//        formUtility.addButton(buttonPanel, form);
        formUtility.addButton(saveContact, form);
        formUtility.addButton(cancelEditContact, form);
        
        deleteButton.setBackground(Color.PINK);

        formUtility.addButton(deleteButton, form);
        formUtility.addButton(newButton, form);
        
        formUtility.addLastField(new JPanel(), form);

        		
        // Add an little padding around the form
        form.setBorder(new EmptyBorder(2, 2, 2, 2));
        
        mainPanel.setBorder(BorderFactory.createEtchedBorder());
        
    }
    
    /*
     * This function updates the edit panel
     */
    private void updateEditPanel()  
    {
    	fieldFName.setText(contactSelected.getFirstName());
        fieldLName.setText(contactSelected.getLastName());
        fieldMName.setText(contactSelected.getMiddleInitial());
        fieldAddL1.setText(contactSelected.getAddressLine1());
        fieldAddL2.setText(contactSelected.getAddressLine2());
        fieldCity.setText(contactSelected.getCity());
        fieldZip.setText(contactSelected.getZipcode());
        fieldPhNum.setText(contactSelected.getPhoneNumber());
        fieldState.setText(contactSelected.getState());
        fieldGender.setText(contactSelected.getGender());
        fieldEmail.setText(contactSelected.getEmail());
        fieldCountry.setText(contactSelected.getCountry());
    }
    
    /*
     * this is the function which does the data validation
     */
    private void dataValidation()
    {
        String newIdentifier = fieldFName.getText().trim() + fieldMName.getText().trim() + fieldLName.getText().trim(); //Grabing full name from contact.
        
        newIdentifier = newIdentifier.toLowerCase();
        errorMessage="";
        boolean toaddnull = false, same=false;
        int count=0;
        
        zeroErrors= true;
        for(Object contact: contactsHashMap.values())
        {
        	ContactUtility id = (ContactUtility)contact;
            if(id.getFirstName().equals(fieldFName.getText()) && id.getMiddleInitial().equals(fieldMName.getText())&& id.getLastName().equals(fieldLName.getText()))
            {

                errorMessage = "* A person with this name is already in your contacts";
                count++;
                if(editMode)
                {                    
                	if(contactSelected.getFirstName().equals(fieldFName.getText()) && contactSelected.getLastName().equals(fieldLName.getText())&& contactSelected.getMiddleInitial().equals(fieldMName.getText()))
                    {
                		count++;
                        //zeroErrors= true;
                    }
                }
            }
         
        }
        if(editMode){ //When it is in editing mode.
            if(count==2 || count==0)   //above loop will increment count either twice(count=2) one for if identifier matches and second for matching the selected entry
                                       //                                  or edited detail is completely different.(count=0)
                {
            	zeroErrors=true;
                                }
            else{
            	zeroErrors=false;
               
            }
        }else{
            if(count>0)zeroErrors= false; //for new entery case, counter will be incremented only once.
        }
        
        
        if(fieldGender.getText().trim()== null || !fieldGender.getText().trim().matches("[MmFf]")|| fieldGender.getText().trim().isEmpty() )
        {
           zeroErrors = false;
           toaddnull = true;
           fieldGender.setBackground(errorColor);
           errorMessage += "Gender, ";
        }
        
        
        
        if(fieldZip.getText().trim()== null || !fieldZip.getText().trim().matches("[0-9]+")|| fieldZip.getText().trim().isEmpty() )
        {
           zeroErrors = false;
           toaddnull = true;
           fieldZip.setBackground(errorColor);
           errorMessage += "Zipcode, ";
        }
        if(fieldPhNum.getText().trim()== null || !fieldPhNum.getText().trim().matches("[0-9]+") || fieldPhNum.getText().trim().isEmpty() )
        {
           zeroErrors = false;
           toaddnull = true;
           fieldPhNum.setBackground(errorColor);
           errorMessage += "Phone number, "+"\n"; 
        }
        
        if(fieldEmail.getText().trim()== null || !fieldEmail.getText().trim().matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")|| fieldEmail.getText().trim().isEmpty() )
        {
        	zeroErrors = false;
        	toaddnull = true;
            fieldEmail.setBackground(errorColor);
        	errorMessage += "Email id, ";
        	
        }
        if(fieldCity.getText().trim()== null || !fieldCity.getText().trim().matches("[a-zA-Z]+") || fieldCity.getText().trim().isEmpty() )
        {
            zeroErrors = false;
            toaddnull = true;
            fieldCity.setBackground(errorColor);
            errorMessage += "City, ";
        }
        if(fieldFName.getText().trim() == null || !fieldFName.getText().trim().matches("[a-zA-z]+"))
        {
            zeroErrors = false;
            toaddnull = true;
            fieldFName.setBackground(errorColor);
            errorMessage += "Firstname, "+"\n";
        }
        
        if(fieldLName.getText().trim() == null ||  !fieldLName.getText().trim().matches("[a-zA-z]+")|| fieldLName.getText().trim().isEmpty())
        {
            zeroErrors = false;
            toaddnull = true;
            fieldLName.setBackground(errorColor);
            errorMessage += "Last Name, "; 
        }
        if(fieldMName.getText().trim().length() > 1  || fieldMName.getText().trim() == null)
        {
           zeroErrors = false;
           toaddnull = true;
           
           errorMessage += "Middle Initial, ";
        }
        if(fieldAddL1.getText().trim() == null ||  fieldAddL1.getText().trim().isEmpty())
        {
           zeroErrors = false;
           toaddnull = true;
           fieldAddL1.setBackground(errorColor);
           errorMessage += "Address Line1, "+"\n";
        }

        if(fieldState.getText().trim()== null || !fieldState.getText().trim().matches("[a-zA-Z]|[a-zA-Z][a-zA-Z]") || fieldState.getText().trim().isEmpty())
        {
           zeroErrors = false;
           toaddnull = true;
           fieldState.setBackground(errorColor);
           errorMessage += "State, ";
        }
        
        
        
        if(fieldCountry.getText().trim()== null || !fieldCountry.getText().trim().matches("[a-zA-Z]+")|| fieldCountry.getText().trim().isEmpty() )
        {
        	zeroErrors = false;
        	toaddnull = true;
        	fieldCountry.setBackground(errorColor);
             errorMessage +="Country, ";
        }
        if (errorMessage.length() > 3&& toaddnull) { 
        	  errorMessage += "are empty or invalid";
        }
        errorTextArea.setText(errorMessage);
    }
    
    /*
     * Function used in between the code to make all names true boolean
     */
    private void makeAllTrue(){
    	aname= true;
    	fname=true;
    	lname=true;
    	gname=true;
    	aname=true;
    	cname=true;
    	sname=true;
    	zname=true;
    	coname=true;
    	pname=true;
    }

    /*
     * Function used in between the code to make all names false boolean
     */
    private void makeAllFalse(){
    	aname= false;
    	fname= false;
    	lname= false;
    	gname=false;
    	aname=false;
    	cname=false;
    	sname=false;
    	zname=false;
    	coname=false;
    	pname=false;
    }
}


