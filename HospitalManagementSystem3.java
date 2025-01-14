package trynumber2;

import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.regex.Pattern;
import java.util.Random;

public class HospitalManagementSystem3 {
    private static Connection connection;

    public static void main(String[] args) {
        connectToDatabase();

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Hospital Management System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 800);

            JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
            JButton addPatientButton = new JButton("Add Patient");
            JButton addDoctorButton = new JButton("Add Doctor");
            JButton addProcedureButton = new JButton("Add Procedure");
            JButton addDepartmentButton = new JButton("Add Department");
            JButton addMedicationButton = new JButton("Add Medication");
            JButton addInteractionButton = new JButton("Add Interaction");
            JButton addProcedureToPatientButton = new JButton("Add Procedure to Patient");
            JButton addMedicationToPatientButton = new JButton("Add Medication to Patient");
            JButton viewPatientRecordButton = new JButton("View Patient Record");
            JButton viewDepartmentProceduresButton = new JButton("View Department Procedures");
            JButton viewDoctorProceduresButton = new JButton("View Doctor Procedures");

            panel.add(addPatientButton);
            panel.add(addDoctorButton);
            panel.add(addProcedureButton);
            panel.add(addDepartmentButton);
            panel.add(addMedicationButton);
            panel.add(addInteractionButton);
            panel.add(viewPatientRecordButton);
            panel.add(viewDepartmentProceduresButton);
            panel.add(viewDoctorProceduresButton);

            // Add the new buttons
            panel.add(addProcedureToPatientButton);
            panel.add(addMedicationToPatientButton);

            frame.add(panel);
            frame.setVisible(true);

            // Existing button actions
            addPatientButton.addActionListener(e -> showAddPatientDialog(frame));
            addDoctorButton.addActionListener(e -> showAddDoctorDialog(frame));
            addProcedureButton.addActionListener(e -> showAddProcedureDialog(frame));
            addDepartmentButton.addActionListener(e -> showAddDepartmentDialog(frame));
            addMedicationButton.addActionListener(e -> showAddMedicationDialog(frame));
            addInteractionButton.addActionListener(e -> showAddInteractionDialog(frame));
            viewDepartmentProceduresButton.addActionListener(e -> showProceduresByDepartment(frame));
            addProcedureToPatientButton.addActionListener(e -> showAddProcedureToPatientDialog(frame));
            addMedicationToPatientButton.addActionListener(e -> showAddMedicationToPatientDialog(frame));
            viewDoctorProceduresButton.addActionListener(e -> showDoctorProcedures(frame));
            viewPatientRecordButton.addActionListener(e -> showPatientRecord(frame));
        });
    }

    // Database connection setup
    private static void connectToDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:oracle:thin:@cisvm-oracle.unfcsd.unf.edu:1521:orcl", "G08", "OkR3bOQJ");
            System.out.println("Connected to database.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database connection failed!", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    // Close the database connection
    private static void closeConnection() {
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    //Shows the placeholder text so people can see how it's inputed beforehand
    private static void inputPlaceholder(JTextField currentField, String currentField_placeholder) {
    	
    	//foreground = text
    	currentField.setText(currentField_placeholder); 
    	currentField.setForeground(Color.GRAY); 

    	currentField.addFocusListener(new java.awt.event.FocusAdapter() {
    		
    		
    		//When someone clicks in the space, remove the placeholder text
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (currentField.getText().equals(currentField_placeholder)) {
                	currentField.setText(""); 
                	currentField.setForeground(Color.BLACK); 
                }
            }
            
            //When someone clicks away from the space, add the placeholder text if the box is empty
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (currentField.getText().isEmpty()) {
                	currentField.setText(currentField_placeholder); 
                	currentField.setForeground(Color.GRAY); 
                }
            }
        });
    }

    
    //Dynamically checks the current input with its restrictions
    private static void inputChecker(JTextField currentField, String currentRegex, String currentInputTooltip) {
    	Pattern currentPattern = Pattern.compile(currentRegex);
    	
    	currentField.getDocument().addDocumentListener(new DocumentListener() {
    		
    		private void validateInput() {
    			String currentInput = currentField.getText();
    			
    			
    			// Matcher matcher = currentPattern.matcher(currentInput);
    			// boolean matches = matcher.matches();
    			// This if statement are both lines above into one
    			if (currentPattern.matcher(currentInput).matches()) {
    				//currentField.setBackground(Color.GREEN);
    				currentField.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
    				currentField.setToolTipText(null);
    			}														//Green if valid, red if invalid
    			else {
    				//currentField.setBackground(Color.RED);
    				currentField.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
    				currentField.setToolTipText(currentInputTooltip);
    			}
    		}
    		
    		//Events needed to check the input dynamically 
    		@Override
    		
    		public void insertUpdate(DocumentEvent e) {
    			validateInput();
    		}
    		
    		@Override
    		
    		public void removeUpdate(DocumentEvent e) {
    			validateInput();
    		}
    		
    		@Override
    		
    		public void changedUpdate(DocumentEvent e) {
    			validateInput();
    		}
    		
    		
    	});
    }
    
    
    private static boolean requiredInputsValidation(JTextField[] listOfFields) {
    	for (JTextField field : listOfFields) {
    		
    		if (field.getBorder() instanceof LineBorder) {
                // Cast the border to LineBorder to access the color
                LineBorder lineBorder = (LineBorder) field.getBorder();
                Color borderColor = lineBorder.getLineColor();
                // Print the JTextField and its border color for error testing
                System.out.println("JTextField: " + field.getText() + " | Border Color: " + borderColor);
                
                // Check if the border color is green
                if (!borderColor.equals(Color.GREEN)) {
                    return false; // return false if any invalid (non-green) required field is found
                }
    		}
    	
    	}
    	return true;
    }
    
    //True = in DB, False = not in DB, Null = error'd out
    private static Boolean checkForPatientID(String p_ID) {
    	String sql_string = "Select Count (*) as isPatient From patient where p_ID = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql_string)){
	        statement.setString(1, p_ID);
	        
	        ResultSet resultTuple = statement.executeQuery();
	        resultTuple.next();
	        
	        //Returns true if PID is in database already; false if not
	        //while (resultTuple.next())
	        //System.out.print(resultTuple.getBoolean("isPatient"));
	        Boolean isPatientID = resultTuple.getBoolean("isPatient");
	        //return resultTuple.next(); 
	        return isPatientID;
        }
        catch (SQLException e) {
        	e.printStackTrace();
        	return null;
        }
        
    }
  //True = in DB, False = not in DB, Null = error'd out
    private static Boolean checkForPatientSSN(String ssn) {
    	String sql_string = "Select Count (*) as isSSN From patient where ssn = ?";
    	try(PreparedStatement statement = connection.prepareStatement(sql_string)){
	        statement.setString(1, ssn);
	        
	        ResultSet resultTuple = statement.executeQuery();
	        resultTuple.next();
	        
	        //Returns true if SSN is in database already; false if not
	        
	        Boolean isPatientSSN = resultTuple.getBoolean("isSSN");
	        //return resultTuple.next(); 
	        return isPatientSSN;
        }
        catch (SQLException e) {
        	e.printStackTrace();
        	return null;
        }
        
    }
  //True = in DB, False = not in DB, Null = error'd out
    private static Boolean checkForDoctorSSN(String ssn) {
    	String sql_string = "Select Count (*) as isSSN From doctor where ssn = ?";
    	try(PreparedStatement statement = connection.prepareStatement(sql_string)){
	        statement.setString(1, ssn);
	        
	        ResultSet resultTuple = statement.executeQuery();
	        resultTuple.next();
	        
	        //Returns true if SSN is in database already; false if not
	        
	        Boolean isPatientSSN = resultTuple.getBoolean("isSSN");
	        //return resultTuple.next(); 
	        return isPatientSSN;
        }
        catch (SQLException e) {
        	e.printStackTrace();
        	return null;
        }
        
    }
  //True = in DB, False = not in DB, Null = error'd out
    private static Boolean checkForDoctorID(String doc_ID) {
    	//System.out.println("this is testing"+doc_ID+"no space");
    	if (doc_ID.equals(""))
    		return true;
    	String sql_string = "Select Count (*) as isDOC From doctor where doc_ID = ?";
    	try(PreparedStatement statement = connection.prepareStatement(sql_string)){
	        statement.setString(1, doc_ID);
	        
	        ResultSet resultTuple = statement.executeQuery();
	        resultTuple.next();
	        
	        //Returns true if SSN is in database already; false if not
	        
	        Boolean isDoctorID = resultTuple.getBoolean("isDOC");
	        //return resultTuple.next(); 
	        return isDoctorID;
        }
        catch (SQLException e) {
        	e.printStackTrace();
        	return null;
        }
        
    }
    
    private static Boolean checkForContactID(String contact_ID) {
    	String sql_string = "Select Count (*) as isContact From contact_info where contactID = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql_string)){
	        statement.setString(1, contact_ID);
	        
	        ResultSet resultTuple = statement.executeQuery();
	        resultTuple.next();
	        System.out.println("has been validated");
	        //Returns true if PID is in database already; false if not
	        //while (resultTuple.next())
	        //System.out.print(resultTuple.getBoolean("isPatient"));
	        Boolean isContactID = resultTuple.getBoolean("isContact");
	        //return resultTuple.next(); 
	        return isContactID;
        }
        catch (SQLException e) {
        	e.printStackTrace();
        	return null;
        }
        
        
    }
    private static Boolean checkForAddressID(String address_ID) {
    	String sql_string = "Select Count (*) as isAddress From address where addressID = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql_string)){
	        statement.setString(1, address_ID);
	        
	        ResultSet resultTuple = statement.executeQuery();
	        resultTuple.next();
	        System.out.println("has been validated");
	        //Returns true if PID is in database already; false if not
	        //while (resultTuple.next())
	        //System.out.print(resultTuple.getBoolean("isPatient"));
	        Boolean isAddressID = resultTuple.getBoolean("isAddress");
	        //return resultTuple.next(); 
	        return isAddressID;
        }
        catch (SQLException e) {
        	e.printStackTrace();
        	return null;
        }
    }
    // Add Patient Dialog
    private static void showAddPatientDialog(JFrame parent) {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        JTextField idField = new JTextField();
        JTextField ssnField = new JTextField();
        JTextField primaryDoctorField = new JTextField();
        JTextField secondaryDoctorField = new JTextField();
        JTextField contactField = new JTextField();
        //JTextField conditionField = new JTextField();
        JComboBox<String> conditionDropdown = new JComboBox<>(new String[] {
        	    "Critical",
        	    "Stable",
        	    "Fair"
        	});
        conditionDropdown.setEditable(false);
        
        
        String idField_placeholder = "\" P12345678 \"";
        String idField_requirement = "Patient ID must start with the letter 'P' followed by 8 digits";
        String idField_regex = "P\\d{8}";
        inputPlaceholder(idField,idField_placeholder);
        inputChecker(idField,idField_regex,idField_requirement);
        
        String ssnField_placeholder = "\" 123-45-6789 \"";
        String ssnField_requirement = "Must be in the format of \"XXX-XX-XXXX\"";
        String ssnField_regex = "\\d{3}-\\d{2}-\\d{4}";
        //String ssnField_regex = "\\d{9}"; //for testing
        inputPlaceholder(ssnField,ssnField_placeholder);
        inputChecker(ssnField,ssnField_regex,ssnField_requirement);
        
        String primaryDoctorField_placeholder = "\" D87654321 \"";
        String primaryDoctorField_requirement = "Doctor ID must start with the letter 'D' followed by 8 digits or be left empty";
        String primaryDoctorField_regex = "D\\d{8}";
        //String primaryDoctorField_regex = "D\\d{7}|^$"; //for testing
        //inputPlaceholder(primaryDoctorField,primaryDoctorField_placeholder);
        inputChecker(primaryDoctorField,primaryDoctorField_regex,primaryDoctorField_requirement);
        
        String secondaryDoctorField_placeholder = "\" D87654321 \"";
        String secondaryDoctorField_requirement = "Doctor ID must start with the letter 'D' followed by 8 digits or be left empty";
        //String primaryDoctorField_regex = "D\\d{8}";
        String secondaryDoctorField_regex = "D\\d{7}|^$"; //for testing
        //inputPlaceholder(secondaryDoctorField,secondaryDoctorField_placeholder);
        inputChecker(secondaryDoctorField,secondaryDoctorField_regex,secondaryDoctorField_requirement);
        
        String contactField_requirement = "Contact ID must contain up to 9 alphanumeric symbols";
        String contactField_regex = "^[a-zA-Z0-9]{0,9}$";
        inputChecker(contactField,contactField_regex,contactField_requirement);
        
        //JTextField idField = new JTextField();
        panel.add(new JLabel("Patient ID*:"));
        panel.add(idField);
        idField.setHorizontalAlignment(JTextField.CENTER);
        
        panel.add(new JLabel("SSN*:"));
        panel.add(ssnField);
        ssnField.setHorizontalAlignment(JTextField.CENTER);
        
        panel.add(new JLabel("Primary Doctor ID:"));
        panel.add(primaryDoctorField);
        primaryDoctorField.setHorizontalAlignment(JTextField.CENTER);
        
        panel.add(new JLabel("Secondary Doctor ID:"));
        panel.add(secondaryDoctorField);
        secondaryDoctorField.setHorizontalAlignment(JTextField.CENTER);
        
        panel.add(new JLabel("Contact ID:"));
        panel.add(contactField);
        contactField.setHorizontalAlignment(JTextField.CENTER);
        
        panel.add(new JLabel("Condition:"));
        //panel.add(conditionField);
        //conditionField.setHorizontalAlignment(JTextField.CENTER);
        panel.add(conditionDropdown);
        
        // Add button for Contact ID input
        JButton contactButton = new JButton("Add Contact ID");
        contactButton.addActionListener(e -> showAddContactDialog(parent, contactField)); // Trigger contact dialog

        panel.add(contactButton);
        
        
        JTextField[] requiredFields = {idField, ssnField};
        boolean queryInProgress = true;
        while (queryInProgress) {
	        int result = JOptionPane.showConfirmDialog(parent, panel, "Add Patient", JOptionPane.OK_CANCEL_OPTION);
	        
	        if (result == JOptionPane.OK_OPTION) {
	        	
	        	if ( requiredInputsValidation(requiredFields) ) {
		            
	        		Boolean patientIDExists = checkForPatientID(idField.getText());
	        		Boolean patientSSNExists = checkForPatientSSN(ssnField.getText());
	        		
	        		
	        		//Returns true if Doc id is valid OR if no ID was entered while adding patient
	        		Boolean primaryDoctorID_isValid = checkForDoctorID(primaryDoctorField.getText());	
	        		Boolean secondaryDoctorID_isValid = checkForDoctorID(secondaryDoctorField.getText());
	        		if ( patientIDExists == null || patientSSNExists == null) {
	        		    //this means there was a problem with the SQL query
	        		    JOptionPane.showMessageDialog(parent, "Error occurred while checking for Patient ID. :(", "SQL Error", JOptionPane.ERROR_MESSAGE);
	        		
	        		
	        		} else if (patientIDExists || patientSSNExists || !primaryDoctorID_isValid || !secondaryDoctorID_isValid) {
	        		    //patient ID/SSN exists already or doctor ID(s) do not exist
	        			if (patientIDExists)
	        				JOptionPane.showMessageDialog(parent, "Patient ID already exists.", "Error", JOptionPane.ERROR_MESSAGE);
	        			else if (patientSSNExists)
	        				JOptionPane.showMessageDialog(parent, "Patient SSN already exists.", "Error", JOptionPane.ERROR_MESSAGE);
	        			else if (!primaryDoctorID_isValid)
	        				JOptionPane.showMessageDialog(parent, "ID for Primary Doctor does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
	        			else if (!secondaryDoctorID_isValid)
	        				JOptionPane.showMessageDialog(parent, "ID for Secondary Doctor does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
	        		
	        		
	        		} else {
	        		   
	        		
		        		// Continue theoriginal database operation in a separate thread
			            new Thread(() -> {
			                try {
			                    String sql = "INSERT INTO patient (p_ID, ssn, pr_doctor, sec_doctor, contact, condition) VALUES (?, ?, ?, ?, ?, ?)";
			                    PreparedStatement statement = connection.prepareStatement(sql);
			                    statement.setString(1, idField.getText());
			                    statement.setString(2, ssnField.getText());
			                    statement.setString(3, primaryDoctorField.getText());
			                    statement.setString(4, secondaryDoctorField.getText());
			                    statement.setString(5, contactField.getText());
			                    //statement.setString(6, conditionField.getText())
			                    statement.setString(6, (String) conditionDropdown.getSelectedItem()); //Cast object to string
			                    statement.executeUpdate();
			
			                    // Use SwingUtilities.invokeLater to update the GUI after completion
			                    SwingUtilities.invokeLater(() -> {
			                        JOptionPane.showMessageDialog(parent, "Patient added successfully!");
			                    });
			                } catch (SQLException e) {
			                    e.printStackTrace();
			                    SwingUtilities.invokeLater(() -> {
			                        JOptionPane.showMessageDialog(parent, "Error adding patient: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			                    });
			                }
			            }).start();
			            
			            queryInProgress = false;
	        		}
	        	}
	        	else {
	        		JOptionPane.showMessageDialog(parent, "Error adding patient: One or more required fields do not have valid inputs. ","Error", JOptionPane.ERROR_MESSAGE);
	        	}
	        }
	        // Pressing cancel
	        else {
	        	queryInProgress = false;
	        }
        }
    }

    private static void showAddContactDialog(JFrame parent, JTextField contactidfield_parent) {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        JTextField contactIdField = new JTextField();
        JTextField firstNameField = new JTextField();
        JTextField middleNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField currAddressField = new JTextField();
        JTextField permAddressField = new JTextField();
        JTextField permPhoneField = new JTextField();
        JTextField currPhoneField = new JTextField();
        JTextField birthDateField = new JTextField();
        JTextField sexField = new JTextField();
        
        String birthDateField_requirement = "Birth date must be 'YYYY-MM-DD'";
        String birthDateField_regex = "\\d{4}-\\d{2}-\\d{2}";;
       
        inputChecker(birthDateField,birthDateField_regex,birthDateField_requirement);

        panel.add(new JLabel("Contact ID:"));
        panel.add(contactIdField);
        panel.add(new JLabel("First Name:"));
        panel.add(firstNameField);
        panel.add(new JLabel("Middle Name:"));
        panel.add(middleNameField);
        panel.add(new JLabel("Last Name:"));
        panel.add(lastNameField);
        panel.add(new JLabel("Permanent Phone:"));
        panel.add(permPhoneField);
        panel.add(new JLabel("Current Phone:"));
        panel.add(currPhoneField);
        panel.add(new JLabel("Birth Date (YYYY-MM-DD):"));
        panel.add(birthDateField);
        panel.add(new JLabel("Sex (M/F):"));
        panel.add(sexField);
        panel.add(new JLabel("Current AddressID:"));
        panel.add(currAddressField);
        panel.add(new JLabel("Permanent AddressID:"));
        panel.add(permAddressField);
        
        Random random = new Random();
        
        // Generate a random 9-digit number (inclusive range: 100,000,000 to 999,999,999)
        int randomNumber = random.nextInt(90_000_000) + 10_000_000;
        String randomContactID ="C" + String.valueOf(randomNumber);
        Boolean isContactIDinDB = checkForContactID(randomContactID);
        //generates new ID if one exists already
        while (isContactIDinDB) {
        	randomNumber = random.nextInt(90_000_000) + 10_000_000;
            randomContactID ="C" + String.valueOf(randomNumber);
            isContactIDinDB = checkForContactID(randomContactID);
        }
        contactidfield_parent.setText(randomContactID);
        contactIdField.setText(randomContactID);
        
        
        
        
        // Add buttons to set current and permanent addresses
        JButton currAddressButton = new JButton("Add Current Address");
        currAddressButton.addActionListener(e -> showAddressDialog(parent, "Current Address", currAddressField));

        JButton permAddressButton = new JButton("Add Permanent Address");
        permAddressButton.addActionListener(e -> showAddressDialog(parent, "Permanent Address", permAddressField));

        // Add the buttons to the panel
        panel.add(currAddressButton);
        panel.add(permAddressButton);

        int result = JOptionPane.showConfirmDialog(parent, panel, "Add Contact", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            new Thread(() -> {
                try {
                    String sql = "INSERT INTO contact_info (contactID, fname, mname, lname, curr_address, perm_address, perm_phone, curr_phone, bdate, sex) VALUES (?, ?, ?, ?, ?, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?)";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    statement.setString(1, contactIdField.getText());
                    statement.setString(2, firstNameField.getText());
                    statement.setString(3, middleNameField.getText());
                    statement.setString(4, lastNameField.getText());
                    statement.setString(5, currAddressField.getText());
                    statement.setString(6, permAddressField.getText());
                    statement.setString(7, permPhoneField.getText());
                    statement.setString(8, currPhoneField.getText());
                    statement.setString(9, birthDateField.getText());
                    statement.setString(10, sexField.getText());
                    statement.executeUpdate();

                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(parent, "Contact added successfully!");
                    });
                } catch (SQLException e) {
                    e.printStackTrace();
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(parent, "Error adding contact: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    });
                }
            }).start();
            
        }
        
    }

    private static void showAddressDialog(JFrame parent, String addressType, JTextField addressidField_parent) {
        JPanel addressPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JTextField addressIdField = new JTextField();
        JTextField addressFieldText = new JTextField();
        JTextField cityField = new JTextField();
        JTextField stateField = new JTextField();
        JTextField zipField = new JTextField();

        addressPanel.add(new JLabel("Address ID:"));
        addressPanel.add(addressIdField);
        addressPanel.add(new JLabel("Address:"));
        addressPanel.add(addressFieldText);
        addressPanel.add(new JLabel("City:"));
        addressPanel.add(cityField);
        addressPanel.add(new JLabel("State:"));
        addressPanel.add(stateField);
        addressPanel.add(new JLabel("ZIP Code:"));
        addressPanel.add(zipField);
        
        Random random = new Random();
        
        // Generate a random 9-digit number (inclusive range: 100,000,000 to 999,999,999)
        int randomNumber = random.nextInt(90_000_000) + 10_000_000;
        String randomAddressID ="A" + String.valueOf(randomNumber);
        Boolean isAddressIDinDB = checkForAddressID(randomAddressID);
        //generates new ID if one exists already
        while (isAddressIDinDB) {
        	randomNumber = random.nextInt(90_000_000) + 10_000_000;
            randomAddressID ="A" + String.valueOf(randomNumber);
            isAddressIDinDB = checkForAddressID(randomAddressID);
        }
        addressidField_parent.setText(randomAddressID);
        addressIdField.setText(randomAddressID);

        int result = JOptionPane.showConfirmDialog(parent, addressPanel, "Add " + addressType, JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            new Thread(() -> {
                try {
                    String sql = "INSERT INTO address (addressID, addr, city, state, zip) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    statement.setString(1, addressIdField.getText());
                    statement.setString(2, addressFieldText.getText());
                    statement.setString(3, cityField.getText());
                    statement.setString(4, stateField.getText());
                    statement.setString(5, zipField.getText());
                  
                    statement.executeUpdate();

                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(parent, "Contact added successfully!");
                    });
                } catch (SQLException e) {
                    e.printStackTrace();
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(parent, "Error adding contact: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    });
                }
            }).start();
            
        }
    }




    
    private static void showAddDoctorDialog(JFrame parent) {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        JTextField ssnField = new JTextField();
        JTextField departmentField = new JTextField();
        JTextField doctorIdField = new JTextField();
        JTextField contactField = new JTextField();
        
        String DoctoridField_placeholder = "\" D12345678 \"";
        String DoctoridField_requirement = "Doctor ID must start with the letter 'D' followed by 8 digits";
        String DoctoridField_regex = "D\\d{8}";
        inputPlaceholder(doctorIdField,DoctoridField_placeholder);
        inputChecker(doctorIdField,DoctoridField_regex,DoctoridField_requirement);
        
        String ssnField_placeholder = "\" 123-45-6789 \"";
        String ssnField_requirement = "Must be in the format of \"XXX-XX-XXXX\"";
        //String ssnField_regex = "\\d{3}-\\d{2}-\\d{4}";
        String ssnField_regex = "\\d{9}"; //for testing
        inputPlaceholder(ssnField,ssnField_placeholder);
        inputChecker(ssnField,ssnField_regex,ssnField_requirement);
        
        
        String departmentField_requirement = "Department code consists of up to 4 characters";
        String departmentField_regex = "^[a-zA-Z0-9]{0,4}$";
        inputChecker(departmentField,departmentField_regex,departmentField_requirement);
        
        String contactField_requirement = "Contact ID must contain up to 9 alphanumeric symbols";
        String contactField_regex = "^[a-zA-Z0-9]{0,9}$";
        inputChecker(contactField,contactField_regex,contactField_requirement);

        panel.add(new JLabel("SSN:"));
        panel.add(ssnField);
        panel.add(new JLabel("Department Code:"));
        panel.add(departmentField);
        panel.add(new JLabel("Doctor ID:"));
        panel.add(doctorIdField);
        panel.add(new JLabel("Contact ID:"));
        panel.add(contactField);

        // Add a button to open the contact dialog
        JButton addContactButton = new JButton("Add Contact");
        addContactButton.addActionListener(e -> showAddContactDialog(parent,contactField));  // Show contact dialog
        panel.add(addContactButton);
        JTextField[] requiredFields = {doctorIdField, ssnField, contactField};
        boolean queryInProgress = true;
        while (queryInProgress) {

	        int result = JOptionPane.showConfirmDialog(parent, panel, "Add Doctor", JOptionPane.OK_CANCEL_OPTION);
	        if (result == JOptionPane.OK_OPTION) {
	            // Run the database operation in a separate thread
	        	if ( requiredInputsValidation(requiredFields) ) {
		            
	        		Boolean DoctorIDExists = checkForDoctorID(doctorIdField.getText());
	        		Boolean DoctorSSNExists = checkForDoctorSSN(ssnField.getText());
	        		
	        		
	        		
	        	
	        		if ( DoctorIDExists == null || DoctorSSNExists == null) {
	        		    //this means there was a problem with the SQL query
	        		    JOptionPane.showMessageDialog(parent, "Error occurred while checking for Patient ID. :(", "SQL Error", JOptionPane.ERROR_MESSAGE);
	        		
	        		
	        		} else if (DoctorIDExists || DoctorSSNExists ) {
	        		    //patient ID/SSN exists already or doctor ID(s) do not exist
	        			if (DoctorIDExists)
	        				JOptionPane.showMessageDialog(parent, "Doctor ID already exists.", "Error", JOptionPane.ERROR_MESSAGE);
	        			else if (DoctorSSNExists)
	        				JOptionPane.showMessageDialog(parent, "Doctor SSN already exists.", "Error", JOptionPane.ERROR_MESSAGE);
	        	
	        		
	        		
	        		} else {
				            new Thread(() -> {
				                try {
				                    String sql = "INSERT INTO doctor (ssn, dptment, doc_ID, contact) VALUES (?, ?, ?, ?)";
				                    PreparedStatement statement = connection.prepareStatement(sql);
				                    statement.setString(1, ssnField.getText());
				                    statement.setString(2, departmentField.getText());
				                    statement.setString(3, doctorIdField.getText());
				                    statement.setString(4, contactField.getText());
				                    statement.executeUpdate();
				
				                    // Use SwingUtilities.invokeLater to update the GUI after completion
				                    SwingUtilities.invokeLater(() -> {
				                        JOptionPane.showMessageDialog(parent, "Doctor added successfully!");
				                    });
				                } catch (SQLException e) {
				                    e.printStackTrace();
				                    SwingUtilities.invokeLater(() -> {
				                        JOptionPane.showMessageDialog(parent, "Error adding doctor: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				                    });
				                }
				            }).start();
				            queryInProgress = false;
	        		}
	        	}
	        	
	        }else {
	        	queryInProgress = false;
	        }
        }
    }
  

    
    private static void showAddProcedureDialog(JFrame parent) {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        JTextField procNumberField = new JTextField();
        JTextField offDptmentField = new JTextField();
        JTextField pnameField = new JTextField();
        JTextField descriptionField = new JTextField();
        JTextField durationField = new JTextField();

        panel.add(new JLabel("Procedure Number:"));
        panel.add(procNumberField);
        panel.add(new JLabel("Department Code:"));
        panel.add(offDptmentField);
        panel.add(new JLabel("Procedure Name:"));
        panel.add(pnameField);
        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);
        panel.add(new JLabel("Duration (in minutes):"));
        panel.add(durationField);
        
        String departmentField_requirement = "Department code consists of up to 4 characters";
        String departmentField_regex = "^[a-zA-Z0-9]{0,4}$";
        inputChecker(offDptmentField,departmentField_regex,departmentField_requirement);
        
        String procNumberField_requirement = "Procedure code consists of up to 3 characters followed by 4 digits";
        String procNumberField_regex = "^[A-Za-z]{3}[0-9]{4}$";
        inputChecker(procNumberField,procNumberField_regex,procNumberField_requirement);

        int result = JOptionPane.showConfirmDialog(parent, panel, "Add Procedure", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            // Run the database operation in a separate thread
            new Thread(() -> {
                try {
                    String sql = "INSERT INTO procedure (proc_number, off_dptment, pname, descption, duration) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    statement.setString(1, procNumberField.getText());
                    statement.setString(2, offDptmentField.getText());
                    statement.setString(3, pnameField.getText());
                    statement.setString(4, descriptionField.getText());
                    statement.setInt(5, Integer.parseInt(durationField.getText()));  // Assuming duration is an integer
                    statement.executeUpdate();

                    
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(parent, "Procedure added successfully!");
                    });
                } catch (SQLException e) {
                    e.printStackTrace();
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(parent, "Error adding procedure: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    });
                } catch (NumberFormatException e) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(parent, "Duration must be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    });
                }
            }).start();
        }
    }	
    
    private static void showAddDepartmentDialog(JFrame parent) {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        JTextField deptCodeField = new JTextField();
        JTextField deptNameField = new JTextField();
        JTextField deptHeadField = new JTextField();
        JTextField officeNumberField = new JTextField();
        JTextField officePhoneField = new JTextField();

        panel.add(new JLabel("Department Code:"));
        panel.add(deptCodeField);
        panel.add(new JLabel("Department Name:"));
        panel.add(deptNameField);
        panel.add(new JLabel("Department Head (Doctor ID):"));
        panel.add(deptHeadField);
        panel.add(new JLabel("Office Number:"));
        panel.add(officeNumberField);
        panel.add(new JLabel("Office Phone:"));
        panel.add(officePhoneField);

        int result = JOptionPane.showConfirmDialog(parent, panel, "Add Department", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            // Run the database operation in a separate thread
            new Thread(() -> {
                try {
                    String sql = "INSERT INTO department (dpt_code, dpt_name, dpt_head, office_number, office_phone) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    statement.setString(1, deptCodeField.getText());
                    statement.setString(2, deptNameField.getText());
                    statement.setString(3, deptHeadField.getText());
                    statement.setString(4, officeNumberField.getText());
                    statement.setString(5, officePhoneField.getText());
                    statement.executeUpdate();

                    
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(parent, "Department added successfully!");
                    });
                } catch (SQLException e) {
                    e.printStackTrace();
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(parent, "Error adding department: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    });
                }
            }).start();
        }
    }

    private static void showAddMedicationDialog(JFrame parent) {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        JTextField medNameField = new JTextField();
        JTextField manufacturerField = new JTextField();
        JTextField descriptionField = new JTextField();

        panel.add(new JLabel("Medication Name:"));
        panel.add(medNameField);
        panel.add(new JLabel("Manufacturer:"));
        panel.add(manufacturerField);
        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);

        int result = JOptionPane.showConfirmDialog(parent, panel, "Add Medication", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            // Run the database operation in a separate thread
            new Thread(() -> {
                try {
                    String sql = "INSERT INTO medication (med_name, manufacturer, descrpt) VALUES (?, ?, ?)";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    statement.setString(1, medNameField.getText());
                    statement.setString(2, manufacturerField.getText());
                    statement.setString(3, descriptionField.getText());
                    statement.executeUpdate();

                    // Use SwingUtilities.invokeLater to update the GUI after completion
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(parent, "Medication added successfully!");
                    });
                } catch (SQLException e) {
                    e.printStackTrace();
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(parent, "Error adding medication: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    });
                }
            }).start();
        }
    }

    private static void showAddInteractionDialog(JFrame parent) {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        JTextField intIdField = new JTextField();
        JTextField patIdField = new JTextField();
        JTextField datetimeField = new JTextField();
        JTextField descriptionField = new JTextField();

        panel.add(new JLabel("Interaction ID:"));
        panel.add(intIdField);
        panel.add(new JLabel("Patient ID:"));
        panel.add(patIdField);
        panel.add(new JLabel("Datetime (YYYY-MM-DD HH:MM:SS):"));
        panel.add(datetimeField);
        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);

        int result = JOptionPane.showConfirmDialog(parent, panel, "Add Interaction", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            // Run the database operation in a separate thread
            new Thread(() -> {
                try {
                    String sql = "INSERT INTO interaction (int_id, pat_id, datetime, descrption) VALUES (?, ?, ?, ?)";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    statement.setString(1, intIdField.getText());
                    statement.setString(2, patIdField.getText());
                    statement.setTimestamp(3, Timestamp.valueOf(datetimeField.getText()));
                    statement.setString(4, descriptionField.getText());
                    statement.executeUpdate();

                    // Use SwingUtilities.invokeLater to update the GUI after completion
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(parent, "Interaction added successfully!");
                    });
                } catch (SQLException e) {
                    e.printStackTrace();
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(parent, "Error adding interaction: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    });
                } catch (IllegalArgumentException e) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(parent, "Invalid datetime format. Use 'YYYY-MM-DD HH:MM:SS'.", "Error", JOptionPane.ERROR_MESSAGE);
                    });
                }
            }).start();
        }
    }

    private static void showProceduresByDepartment(JFrame parent) {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        JTextField departmentField = new JTextField();

        panel.add(new JLabel("Enter Department Code or Name:"));
        panel.add(departmentField);

        int result = JOptionPane.showConfirmDialog(parent, panel, "Enter Department", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String departmentInput = departmentField.getText().trim();
            if (!departmentInput.isEmpty()) {
                // Run the database operation in a separate thread
                new Thread(() -> {
                    try {
                        String procedures = getProceduresByDepartment(departmentInput);

                        // Display the procedures in a dialog or text area
                        SwingUtilities.invokeLater(() -> {
                            JTextArea textArea = new JTextArea(20, 50);
                            textArea.setText(procedures);
                            textArea.setCaretPosition(0); // Scroll to top
                            textArea.setEditable(false);

                            JScrollPane scrollPane = new JScrollPane(textArea);
                            JOptionPane.showMessageDialog(parent, scrollPane, "Procedures Offered by Department", JOptionPane.INFORMATION_MESSAGE);
                        });

                    } catch (SQLException e) {
                        e.printStackTrace();
                        SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(parent, "Error retrieving procedures: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        });
                    }
                }).start();
            } else {
                JOptionPane.showMessageDialog(parent, "Please enter a valid department code or name.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void showAddMedicationToPatientDialog(JFrame parent) {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        JTextField doctorIdField = new JTextField();
        JTextField patientIdField = new JTextField();
        JTextField medicationNameField = new JTextField();
        JTextField dateField = new JTextField();  // Assuming user will enter date in "YYYY-MM-DD" format

        panel.add(new JLabel("Enter Doctor ID:"));
        panel.add(doctorIdField);
        panel.add(new JLabel("Enter Patient ID:"));
        panel.add(patientIdField);
        panel.add(new JLabel("Enter Medication Name:"));
        panel.add(medicationNameField);
        panel.add(new JLabel("Enter Date (YYYY-MM-DD):"));
        panel.add(dateField);

        int result = JOptionPane.showConfirmDialog(parent, panel, "Add Medication to Patient", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String doctorId = doctorIdField.getText().trim();
            String patientId = patientIdField.getText().trim();
            String medicationName = medicationNameField.getText().trim();
            String date = dateField.getText().trim();

            if (!doctorId.isEmpty() && !patientId.isEmpty() && !medicationName.isEmpty() && !date.isEmpty()) {
                // Run the database operation in a separate thread
                new Thread(() -> {
                    try {
                        // Call method to add medication to patient
                        addMedicationToPatient(doctorId, patientId, medicationName, date);
                        
                        SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(parent, "Medication added to patient successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        });
                    } catch (SQLException e) {
                        e.printStackTrace();
                        SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(parent, "Error adding medication: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        });
                    }
                }).start();
            } else {
                JOptionPane.showMessageDialog(parent, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Method to add medication to patient in the database
    private static void addMedicationToPatient(String doctorId, String patientId, String medicationName, String date) throws SQLException {
        String sql = "INSERT INTO prescription (DOC_ID, PAT_ID, MED_NAME, FECHA) VALUES (?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'))";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, doctorId);
        stmt.setString(2, patientId);
        stmt.setString(3, medicationName);
        stmt.setString(4, date);  // Assumes date is in the "YYYY-MM-DD" format
        stmt.executeUpdate();
    }
    private static void showDoctorProcedures(JFrame parent) {
        // Ask for the doctor�s ID
        String doctorId = JOptionPane.showInputDialog(parent, "Enter Doctor ID:");
        if (doctorId != null && !doctorId.trim().isEmpty()) {
            new Thread(() -> {
                try {
                    // Get the doctor procedures
                    String doctorProcedures = getDoctorProcedures(doctorId.trim());

                    // Display the procedures in a dialog
                    SwingUtilities.invokeLater(() -> {
                        JTextArea textArea = new JTextArea(20, 50);
                        textArea.setText(doctorProcedures);
                        textArea.setCaretPosition(0); // Scroll to top
                        textArea.setEditable(false);

                        JScrollPane scrollPane = new JScrollPane(textArea);
                        JOptionPane.showMessageDialog(parent, scrollPane, "Doctor's Procedures", JOptionPane.INFORMATION_MESSAGE);
                    });

                } catch (SQLException e) {
                    e.printStackTrace();
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(parent, "Error retrieving doctor procedures: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    });
                }
            }).start();
        } else {
            JOptionPane.showMessageDialog(parent, "Please enter a valid Doctor ID.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static String getDoctorProcedures(String doctorId) throws SQLException {
        StringBuilder procedures = new StringBuilder();

        // Updated query to get the procedures performed by the doctor, joining with PROC_DOC table
        String sql = "SELECT pr.P_NUMBER AS procedure_number, " +
                     "p.PNAME AS procedure_name, " +
                     "p.DESCPTION AS procedure_description, " +
                     "p.DURATION AS procedure_duration, " +
                     "pr.FECHA AS procedure_date, " +
                     "pr.HORA AS procedure_time, " +
                     "pr.NOTES AS procedure_notes, " +
                     "pr.INT_ID AS interaction_id, " +
                     "pr.REC_ID AS record_id " +
                     "FROM PROC_RECORD pr " +
                     "JOIN PROCEDURE p ON pr.P_NUMBER = p.PROC_NUMBER " +
                     "JOIN PROC_DOC pd ON pr.REC_ID = pd.REC_ID " +  // Join with PROC_DOC table
                     "WHERE pd.DOC_ID = ?";  // Filter by DOC_ID in PROC_DOC

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, doctorId);  // Set the doctor ID in the query

        ResultSet rs = stmt.executeQuery();
        procedures.append("Procedures performed by Doctor ").append(doctorId).append(":\n\n");

        while (rs.next()) {
            String procedureNumber = getStringOrDefault(rs, "procedure_number");
            String procedureName = getStringOrDefault(rs, "procedure_name");
            String procedureDescription = getStringOrDefault(rs, "procedure_description");
            String procedureDuration = getStringOrDefault(rs, "procedure_duration");
            String procedureDate = getStringOrDefault(rs, "procedure_date");
            String procedureTime = getStringOrDefault(rs, "procedure_time");
            String procedureNotes = getStringOrDefault(rs, "procedure_notes");
            String interactionId = getStringOrDefault(rs, "interaction_id");
            String recordId = getStringOrDefault(rs, "record_id");  // Fetch REC_ID

            // Display the procedure data including REC_ID
            procedures.append("Procedure Number: ").append(procedureNumber).append("\n")
                      .append("Name: ").append(procedureName).append("\n")
                      .append("Description: ").append(procedureDescription).append("\n")
                      .append("Duration: ").append(procedureDuration).append("\n")
                      .append("Date: ").append(procedureDate).append("\n")
                      .append("Time: ").append(procedureTime).append("\n")
                      .append("Notes: ").append(procedureNotes).append("\n")
                      .append("Interaction ID: ").append(interactionId).append("\n")
                      .append("Record ID: ").append(recordId).append("\n\n");  // Show REC_ID
        }

        return procedures.toString();
    }



    
    private static String getProceduresByDepartment(String departmentInput) throws SQLException {
        StringBuilder record = new StringBuilder();

        // Query to fetch procedures by department code or department name
        String proceduresSql = "SELECT pr.PNAME AS procedure_name, pr.DESCPTION AS procedure_description, "
                + "pr.DURATION AS procedure_duration "
                + "FROM procedure pr "
                + "JOIN department d ON pr.OFF_DPTMENT = d.DPT_CODE "
                + "WHERE pr.OFF_DPTMENT = ? OR d.DPT_NAME = ?";
        PreparedStatement stmt = connection.prepareStatement(proceduresSql);
        stmt.setString(1, departmentInput);
        stmt.setString(2, departmentInput);
        ResultSet proceduresResult = stmt.executeQuery();

        record.append("Procedures Offered by Department: ").append(departmentInput).append("\n\n");
        while (proceduresResult.next()) {
            String procedureName = getStringOrDefault(proceduresResult, "procedure_name");
            String procedureDescription = getStringOrDefault(proceduresResult, "procedure_description");
            int procedureDuration = proceduresResult.getInt("procedure_duration");

            record.append("Procedure: ").append(procedureName).append("\n")
                    .append("Description: ").append(procedureDescription).append("\n")
                    .append("Duration: ").append(procedureDuration).append(" minutes\n\n");
        }

        return record.toString();
    }
    private static void showAddProcedureToPatientDialog(JFrame parent) {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        JTextField procedureNumberField = new JTextField();
        JTextField interactionIdField = new JTextField();
        JTextField doctorIdField = new JTextField();  // Allow input of multiple doctor IDs, separated by commas
        JTextField dateField = new JTextField();  // Assuming user will enter date in "YYYY-MM-DD" format
        JTextField timeField = new JTextField();  // Assuming user will enter time in "HH:MM:SS" format
        JTextField notesField = new JTextField();

        panel.add(new JLabel("Enter Procedure Number (P_NUMBER):"));
        panel.add(procedureNumberField);
        panel.add(new JLabel("Enter Interaction ID (INT_ID):"));
        panel.add(interactionIdField);
        panel.add(new JLabel("Enter Doctor IDs (comma-separated):"));
        panel.add(doctorIdField);
        panel.add(new JLabel("Enter Date (YYYY-MM-DD):"));
        panel.add(dateField);
        panel.add(new JLabel("Enter Time (HH:MM:SS):"));
        panel.add(timeField);
        panel.add(new JLabel("Enter Notes (up to 30 characters):"));
        panel.add(notesField);

        int result = JOptionPane.showConfirmDialog(parent, panel, "Add Procedure to Patient", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String procedureNumber = procedureNumberField.getText().trim();
            String interactionId = interactionIdField.getText().trim();  // May be empty
            String doctorIdsInput = doctorIdField.getText().trim();  // Get the doctor IDs input as a string
            String date = dateField.getText().trim();
            String time = timeField.getText().trim();
            String notes = notesField.getText().trim();

            if (!procedureNumber.isEmpty() && !doctorIdsInput.isEmpty() && !date.isEmpty() && !time.isEmpty()) {

                // Run the database operation in a separate thread
                new Thread(() -> {
                    try {
                        // Call method to add the procedure to the patient record
                        addProcedureToPatient(procedureNumber, interactionId, doctorIdsInput, date, time, notes);

                        SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(parent, "Procedure added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        });
                    } catch (SQLException e) {
                        e.printStackTrace();
                        SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(parent, "Error adding procedure: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        });
                    }
                }).start();
            } else {
                JOptionPane.showMessageDialog(parent, "Please fill in all required fields.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Method to add the procedure to the patient's record in the database
    private static void addProcedureToPatient(String procedureNumber, String interactionId, String doctorIdsInput, 
                                               String date, String time, String notes) throws SQLException {
        
        String recId = generateRecId();  
        // Insert procedure record into PROC_RECORD table
        String sql = "INSERT INTO proc_record (P_NUMBER, INT_ID, FECHA, HORA, NOTES, REC_ID) " +
                     "VALUES (?, ?, TO_DATE(?, 'YYYY-MM-DD'), TO_DATE(?, 'HH24:MI:SS'), ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, procedureNumber);  // Insert P_NUMBER
        stmt.setString(2, interactionId.isEmpty() ? null : interactionId);  // Insert INT_ID (optional)
        stmt.setString(3, date);  // Insert Date
        stmt.setString(4, time);  // Insert Time
        stmt.setString(5, notes); // Insert Notes (can be empty)
        stmt.setString(6, recId);  // Insert REC_ID (generated)

        stmt.executeUpdate();

        // Insert doctor IDs into PROC_DOC table
        String[] doctorIds = doctorIdsInput.split(",");  // Split the input by commas
        for (String doctorId : doctorIds) {
            doctorId = doctorId.trim();  // Clean any extra spaces
            if (!doctorId.isEmpty()) {
            	
            	
            	
                String insertDocSql = "INSERT INTO proc_doc (DOC_ID, REC_ID) VALUES (?, ?)";
                PreparedStatement docStmt = connection.prepareStatement(insertDocSql);
                docStmt.setString(1, doctorId);  // Insert DOC_ID
                docStmt.setString(2, recId);  // Insert REC_ID (from PROC_RECORD)
                docStmt.executeUpdate();
            }
        }
    }

    
    private static String generateRecId() throws SQLException {
    	String sql = "SELECT COUNT(*) FROM PROC_DOC";
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            int rowCount = rs.getInt(1);  // The result of COUNT(*) is in the first column
            return("" + rowCount);
        }
        return null;
    }

    // Helper method to return a value or default text if null
    private static String getStringOrDefault(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return (value == null) ? "N/A" : value;
    }
    private static void showPatientRecord(JFrame parent) {
        // Ask for the patient's ID
    	/*JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
    	JTextField idField = new JTextField();
        panel.add(new JLabel("Patient ID*:"));
        panel.add(idField);
        idField.setHorizontalAlignment(JTextField.CENTER);*/
        String patientId = JOptionPane.showInputDialog(parent, "Enter Patient ID:");
        
        /*String idField_placeholder = "\" P12345678 \"";
        String idField_requirement = "Patient ID must start with the letter 'P' followed by 8 digits";
        String idField_regex = "P\\d{8}";
        inputPlaceholder(idField,idField_placeholder);
        inputChecker(idField,idField_regex,idField_requirement);*/

        if (patientId != null && !patientId.trim().isEmpty()) {
            new Thread(() -> {
                try {
                    // Get the patient info, procedures, interactions, and medications
                    String patientInfo = getPatientInfo(patientId.trim());
                    String allProcedures = getAllProcedures(patientId.trim());
                    String allInteractions = getAllInteractions(patientId.trim());
                    String allMedications = getAllMedications(patientId.trim());

                    // Combine all the information
                    StringBuilder patientRecord = new StringBuilder();
                    patientRecord.append("\n\n--- Info ---\n")
                    		.append(patientInfo)
                            .append("\n\n--- Procedures ---\n")
                            .append(allProcedures)
                            .append("\n\n--- Interactions ---\n")
                            .append(allInteractions)
                            .append("\n\n--- Medications ---\n")
                            .append(allMedications);

                    // Display the patient record in a dialog
                    SwingUtilities.invokeLater(() -> {
                        JTextArea textArea = new JTextArea(20, 50);
                        textArea.setText(patientRecord.toString());
                        textArea.setCaretPosition(0); // Scroll to top
                        textArea.setEditable(false);

                        JScrollPane scrollPane = new JScrollPane(textArea);
                        JOptionPane.showMessageDialog(parent, scrollPane, "Patient Health Record", JOptionPane.INFORMATION_MESSAGE);
                    });

                } catch (SQLException e) {
                    e.printStackTrace();
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(parent, "Error retrieving patient record: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    });
                }
            }).start();
        } else {
            JOptionPane.showMessageDialog(parent, "Please enter a valid Patient ID.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static String getPatientInfo(String patientId) throws SQLException {
        StringBuilder patientInfo = new StringBuilder();

        String sql = "SELECT c.fname || ' ' || c.mname || ' ' || c.lname AS Name, " +
                     "d.addr || ', ' || d.city || ', ' || d.state || '. ' || d.zip AS Address, " +
                     "c.curr_phone AS Phone, p.pr_doctor AS Doctor " +
                     "FROM patient p " +
                     "JOIN contact_info c ON p.contact = c.contactid " +
                     "JOIN address d ON c.curr_address = d.addressid " +
                     "WHERE p.p_id = ?";

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, patientId);

        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            String name = rs.getString("Name");
            String address = rs.getString("Address");
            String phone = rs.getString("Phone");
            String doctor = rs.getString("Doctor");

            patientInfo.append("Name: ").append(name).append("\n")
                        .append("Address: ").append(address).append("\n")
                        .append("Phone: ").append(phone).append("\n")
                        .append("Doctor: ").append(doctor).append("\n");
        }

        return patientInfo.toString();
    }

    private static String getAllProcedures(String patientId) throws SQLException {
        StringBuilder procedures = new StringBuilder();

        String sql = "SELECT p.pname AS \"Procedure\", pr.fecha AS \"Date\" " +
                     "FROM procedure p " +
                     "JOIN proc_record pr ON pr.p_number = p.proc_number " +
                     "JOIN interaction i ON pr.int_id = i.int_id " +
                     "WHERE i.pat_id = ?";  // Placeholder for patientId

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, patientId);  // Set the patient ID parameter

        ResultSet rs = stmt.executeQuery();
        procedures.append("Procedure(s) for patient ").append(patientId).append(":\n");

        while (rs.next()) {
            String procedureName = rs.getString("Procedure");
            String procedureDate = rs.getString("Date");
            procedures.append(procedureName).append(" - ").append(procedureDate).append("\n");
        }

        return procedures.toString();
    }


    private static String getAllInteractions(String patientId) throws SQLException {
        StringBuilder interactions = new StringBuilder();

        String sql = "SELECT int_id AS \"Code\", datetime as \"Date\", descrption as \"Description\" " +
                     "FROM interaction WHERE pat_id = ?";

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, patientId);

        ResultSet rs = stmt.executeQuery();
        interactions.append("Interaction(s) for patient ").append(patientId).append(":\n");

        while (rs.next()) {
            String code = rs.getString("Code");
            String date = rs.getString("Date");
            String description = rs.getString("Description");
            interactions.append(code).append(" - ").append(date).append(" - ").append(description).append("\n");
        }

        return interactions.toString();
    }

    private static String getAllMedications(String patientId) throws SQLException {
        StringBuilder medications = new StringBuilder();

        String sql = "SELECT doc_id AS \"Doctor\", med_name as \"Name\", fecha as \"Date\" " +
                     "FROM prescription WHERE pat_id = ?";

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, patientId);

        ResultSet rs = stmt.executeQuery();
        medications.append("Medication(s) for patient ").append(patientId).append(":\n");

        while (rs.next()) {
            String doctor = rs.getString("Doctor");
            String medicationName = rs.getString("Name");
            String date = rs.getString("Date");
            medications.append(doctor).append(" - ").append(medicationName).append(" - ").append(date).append("\n");
        }

        return medications.toString();
    }



}
