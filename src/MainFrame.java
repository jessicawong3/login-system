
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import javax.swing.JTextField;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Login System
 * @author Jessica Wong
 */
public class MainFrame extends javax.swing.JFrame {

    // create printwriter, scanner, and file with user info
    File userDataFile = new File("userdata.txt");
    Scanner s;
    PrintWriter pw;
    
    // scanner and file for bad password file
    File badPass = new File("dictbadpass.txt");
    Scanner s2; 
    
    // create delimiter to use when scanning and writing to files
    String delim = ",";
    
    // the minimum length for good passwords
    int minPassLength = 7;
    
    /**
     * Creates new form MainFrame
     */
    public MainFrame(){        
        initComponents();
        // set extra info/warning labels to invisible
        noUserExistsLabel.setVisible(false);
        badPassLabel.setVisible(false);
        takenUserLabel.setVisible(false);
        invalidPassLabel.setVisible(false);
        invalidEmailLabel.setVisible(false);
    }
    
    /**
     * Checks if there are empty fields in the chosen screen, and changes labels based on the result
     * @param screen The screen to work with (registration vs login)
     * @return Returns if there are empty fields or not
     */
    public boolean emptyFields(char screen) {
        // check for empty fields at default false
        boolean isEmpty = false;
        
        // for the registration screen
        if (screen == 'R'){    
            // check if username field is empty
            if (usernameRField.getText().equals("")){
                usernameLabel.setText("*Username:");
                isEmpty = true;
            } else { 
                usernameLabel.setText("Username:");
            }
            // check if password field is empty
            if (passwordRField.getText().equals("")){
                passwordLabel.setText("*Password:");
                isEmpty = true;
            } else {
                passwordLabel.setText("Password:");
            }
            // check if first name field is empty
            if (fNameRField.getText().equals("")){
                firstNameLabel.setText("*First Name:");
                isEmpty = true;
            } else {
                firstNameLabel.setText("First Name:");
            }
            // check if last name field is empty
            if (lNameRField.getText().equals("")){
                lastNameLabel.setText("*Last Name:");
                isEmpty = true;
            } else {
                lastNameLabel.setText("Last Name:");
            }
            // check if email field is empty
            if (emailRField.getText().equals("")){
                emailLabel.setText("*Email Address:");
                isEmpty = true;
            } else {
                emailLabel.setText("Email Address:");
            }
            
        // for the login screen
        } else if (screen == 'L') {
            // check if username field is empty
            if (usernameLField.getText().equals("")){
                usernameLoginLabel.setText("*Username:");
                isEmpty = true;
            } else {
                usernameLoginLabel.setText("Username:");
            }
            // check if password field is empty
            if (passwordLField.getText().equals("")){
                passwordLoginLabel.setText("*Password:");
                isEmpty = true;
            } else {
                passwordLoginLabel.setText("Password:");
            }
        }
        return isEmpty;
    }
        
    /**
     * Checks if the password is valid, changes password label accordingly
     * @return Returns if the password is valid or not
     */
    public boolean invalidPass() {             
        // check for invalid password at default false
        boolean isInvalid = false;        
        // checks for password requirements at default false
        boolean hasLower = false;
        boolean hasUpper = false;
        boolean hasNum = false;
        boolean goodLength = false;        
        // variable given value of inputed password
        String password = passwordRField.getText();
        
        // check each character for lowercase, uppercase, and a number
        for (int i = 0; i < password.length(); i++) {
            if (password.charAt(i) >= '0' && password.charAt(i) <= '9') {
                hasNum = true;
            } else if (password.charAt(i) == password.toLowerCase().charAt(i)) {
                hasLower = true;
            } else if (password.charAt(i) == password.toUpperCase().charAt(i)) {
                hasUpper = true;
            }
        }
        // check if password length is good
        if (password.length() >= minPassLength) {
            goodLength = true;
        }        
        // if password does not meet requirements, password is invalid
        if (!hasLower || !hasUpper || !goodLength || !hasNum){
            invalidPassLabel.setVisible(true);
            isInvalid = true;
        }
        
        // create scanner for bad passwords file
        try {
            s2 = new Scanner(badPass);
        } catch (IOException ex) {
            System.out.println("The file 'badPass' does not exist.");
        }        
        
        // for every bad password in the file:
        while(s2.hasNextLine()){
            // check if the password field contains the bad password
            if (passwordRField.getText().equals(s2.nextLine())){
                invalidPassLabel.setVisible(true);
                isInvalid = true;
                break;
            }
        }
        
        s2.close();
        return isInvalid;
    }
    
    /**
     * Checks if the email address is already taken or valid, changes email label accordingly
     * @return Returns if the email is a valid email address (meets requirements and is not taken)
     */
    public boolean invalidEmail() {
        // check for taken and invalid email at default false
        boolean isInvalid = false;
        // check for '@' symbol
        boolean hasAt = false;
        // give string value of the email field
        String email = emailRField.getText();
        
        // for each character of the email address
        for (int i = 0; i < email.length(); i++) {
            // check for the '@' symbol
            if (email.charAt(i) == '@') {
                hasAt = true;
            }
        }
        
        // if there is no '@', the email is invalid
        if (!hasAt) {
            isInvalid = true;
            invalidEmailLabel.setText("*Invalid email address");
            invalidEmailLabel.setVisible(true);
        }
        
        // create scanner for user info file
        try {
            s = new Scanner(userDataFile);
        } catch (IOException ex) {
            System.out.println("The file 'userDataFile' does not exist.");
        }
        
        // for each user:
        while(s.hasNextLine()) {
            // create string and array for user info based on file
            String userDataS = s.nextLine();
            String[] userData = userDataS.split(delim);
            
            // check if the emaill field contains the existing email
            if (emailRField.getText().equals(userData[4])){
                invalidEmailLabel.setText("*Email already used");
                invalidEmailLabel.setVisible(true);
                isInvalid = true;
            }
        }
        
        s.close();
        return isInvalid;
    }
    
    /**
     * Checks for a match between the specified username text field and the information in the user data file
     * @param usernameField The text field to work with
     * @return Returns whether or not the text field matches a username in the user data file
     */
    public boolean userMatch(JTextField usernameField) {
        // check for a matching username set at default false
        boolean isMatch = false;
        
        // create scanner for user info file
        try {
            s = new Scanner(userDataFile);
        } catch (IOException ex) {
            System.out.println("The file 'userDataFile' does not exist.");
        }
        
        // for each user:
        while(s.hasNextLine()) {
            // create string and array for user info based on file
            String userDataS = s.nextLine();
            String[] userData = userDataS.split(delim);
            
            // check if the username field contains the existing username
            if (usernameField.getText().equals(userData[0])){
                isMatch = true;
                break;
            }
        }
            
        s.close();
        return isMatch;
    }
    
    /**
     * Checks for a match between the password from the text field and the information in the user data file
     * @return Returns whether or not the password from the text field matches a password in the user data file
     */
    public boolean passMatch() {
        // check for a matching password set at default false
        boolean isMatch = false;
        
        // create scanner for user info file
        try {
            s = new Scanner(userDataFile);
        } catch (IOException ex) {
            System.out.println("The file 'userDataFile' does not exist.");
        }
        
        // for each user:
        while(s.hasNextLine()) {
            // create string and array for user info based on file
            String userDataS = s.nextLine();
            String[] userData = userDataS.split(delim);
                       
            // check if the password and username fields match an existing user profile
            if (encrypt(passwordLField.getText()).equals(userData[1]) && usernameLField.getText().equals(userData[0])){
                isMatch = true;
                break;
            }
        }
            
        s.close();
        return isMatch;
    }
    
    /**
     * Takes in a string and provides the encoded version
     * @param password The password to be encrypted
     * @return Returns the encoded version of the original String
     */
    public String encrypt(String password) {
        String toEncrypt = password;
 
	//java helper class to perform encryption
    	MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("The algorithm 'MD5' does not exist.");
        }
	//give the helper function the password
   	md.update(toEncrypt.getBytes());
	//perform the encryption
	byte byteData[] = md.digest(); 
 
        //To express the byte data as a hexadecimal number (the normal way)
	String sb1 = "";
        for (int i = 0; i < byteData.length; ++i) {
            sb1 += (Integer.toHexString((byteData[i] & 0xFF) |  0x100).substring(1,3));
        }
        return sb1;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        registrationLabel = new javax.swing.JLabel();
        loginLabel = new javax.swing.JLabel();
        usernameLabel = new javax.swing.JLabel();
        passwordLabel = new javax.swing.JLabel();
        firstNameLabel = new javax.swing.JLabel();
        lastNameLabel = new javax.swing.JLabel();
        emailLabel = new javax.swing.JLabel();
        usernameLoginLabel = new javax.swing.JLabel();
        passwordLoginLabel = new javax.swing.JLabel();
        registerButton = new javax.swing.JButton();
        loginButton = new javax.swing.JButton();
        usernameRField = new javax.swing.JTextField();
        passwordRField = new javax.swing.JTextField();
        fNameRField = new javax.swing.JTextField();
        lNameRField = new javax.swing.JTextField();
        emailRField = new javax.swing.JTextField();
        usernameLField = new javax.swing.JTextField();
        passwordLField = new javax.swing.JPasswordField();
        jSeparator1 = new javax.swing.JSeparator();
        noUserExistsLabel = new javax.swing.JLabel();
        badPassLabel = new javax.swing.JLabel();
        takenUserLabel = new javax.swing.JLabel();
        invalidPassLabel = new javax.swing.JLabel();
        invalidEmailLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        registrationLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        registrationLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        registrationLabel.setText("Register");
        registrationLabel.setAutoscrolls(true);

        loginLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        loginLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        loginLabel.setText("Login");

        usernameLabel.setText("Username:");
        usernameLabel.setAutoscrolls(true);

        passwordLabel.setText("Password:");
        passwordLabel.setAutoscrolls(true);

        firstNameLabel.setText("First Name:");
        firstNameLabel.setAutoscrolls(true);

        lastNameLabel.setText("Last Name:");
        lastNameLabel.setAutoscrolls(true);

        emailLabel.setText("Email Address:");
        emailLabel.setAutoscrolls(true);

        usernameLoginLabel.setText("Username:");

        passwordLoginLabel.setText("Password");

        registerButton.setText("Register");
        registerButton.setAutoscrolls(true);
        registerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registerButtonActionPerformed(evt);
            }
        });

        loginButton.setText("Login");
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        noUserExistsLabel.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        noUserExistsLabel.setForeground(new java.awt.Color(255, 0, 0));
        noUserExistsLabel.setText("*Username does not exist");

        badPassLabel.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        badPassLabel.setForeground(new java.awt.Color(255, 0, 0));
        badPassLabel.setText("*Incorrect Password");

        takenUserLabel.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        takenUserLabel.setForeground(new java.awt.Color(255, 0, 0));
        takenUserLabel.setText("*Username already taken");

        invalidPassLabel.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        invalidPassLabel.setForeground(new java.awt.Color(255, 0, 0));
        invalidPassLabel.setText("*Must be at least 7 characters with uppercase, lowercase, and number");

        invalidEmailLabel.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        invalidEmailLabel.setForeground(new java.awt.Color(255, 0, 0));
        invalidEmailLabel.setText("*Invalid email address");
        invalidEmailLabel.setAutoscrolls(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(registrationLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(passwordLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(usernameLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(firstNameLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lastNameLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(emailLabel, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(invalidEmailLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(passwordRField, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(usernameRField, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fNameRField, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lNameRField, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(emailRField, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(takenUserLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(invalidPassLabel)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addComponent(registerButton)))
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(61, 61, 61)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(passwordLoginLabel)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGap(82, 82, 82)
                                            .addComponent(passwordLField, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(usernameLoginLabel)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGap(82, 82, 82)
                                            .addComponent(usernameLField, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(noUserExistsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(badPassLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(57, 57, 57))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(loginLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addContainerGap())
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(loginButton)
                                        .addGap(128, 128, 128))))))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(88, 88, 88)
                        .addComponent(loginLabel)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(usernameLoginLabel)
                            .addComponent(usernameLField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(5, 5, 5)
                        .addComponent(noUserExistsLabel)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(passwordLoginLabel)
                            .addComponent(passwordLField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(7, 7, 7)
                        .addComponent(badPassLabel)
                        .addGap(18, 18, 18)
                        .addComponent(loginButton))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(registrationLabel)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(usernameLabel)
                                .addGap(37, 37, 37)
                                .addComponent(passwordLabel)
                                .addGap(40, 40, 40)
                                .addComponent(firstNameLabel)
                                .addGap(27, 27, 27)
                                .addComponent(lastNameLabel)
                                .addGap(34, 34, 34)
                                .addComponent(emailLabel))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(usernameRField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(takenUserLabel)
                                .addGap(15, 15, 15)
                                .addComponent(passwordRField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(invalidPassLabel)
                                .addGap(18, 18, 18)
                                .addComponent(fNameRField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(21, 21, 21)
                                .addComponent(lNameRField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28)
                                .addComponent(emailRField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(invalidEmailLabel)))))
                .addGap(18, 18, 18)
                .addComponent(registerButton)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void registerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registerButtonActionPerformed
        // when user presses register button:   
        // reset info labels to invisible
        takenUserLabel.setVisible(false);
        invalidPassLabel.setVisible(false);
        invalidEmailLabel.setVisible(false); 
        
        // check if there are empty fields (if a field is empty, indicate empty field)
        emptyFields('R');        
        // check if username is taken (indicate taken username)
        if (userMatch(usernameRField)) {
            takenUserLabel.setVisible(true);
        }
        // check if password does not meet requirements/matches file of bad passwords (indicate invalid password)
        invalidPass();        
        // check if email address is not a valid address (no @ or already used), indicate invalid email
        invalidEmail();
        
        //if everything is valid, encrypt password and use printwriter to write info into file (create new user profile)
        if (!emptyFields('R') && !userMatch(usernameRField) && !invalidPass() && !invalidEmail()) {
            // create printwriter
            try {
                pw = new PrintWriter(new FileWriter(userDataFile,true));
            } catch (IOException ex) {
                System.out.println("The file 'userDataFile' does not exist.");
            }
            // write user profile into user file
            pw.println(usernameRField.getText() + delim + encrypt(passwordRField.getText()) + delim + fNameRField.getText() + delim + lNameRField.getText() + delim + emailRField.getText());
            pw.close();
            registerButton.setText("User Registered");
        }
    }//GEN-LAST:event_registerButtonActionPerformed

    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginButtonActionPerformed
        // when user presses login button:
        // if a field is empty, indicate empty field
        emptyFields('L');
        // if username does not match data on file, indicate wrong info
        if (!userMatch(usernameLField) && !emptyFields('L')) {
            noUserExistsLabel.setVisible(true);
        } else {
            noUserExistsLabel.setVisible(false);
        }
        // if password does not match data on file, indicate wrong info
        if (!passMatch() && !emptyFields('L')) {
            badPassLabel.setVisible(true);
        } else {
            badPassLabel.setVisible(false);
        }
        // else successful login
        if (!emptyFields('L') && passMatch()) {
            loginButton.setText("Successful Login");
        }
    }//GEN-LAST:event_loginButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel badPassLabel;
    private javax.swing.JLabel emailLabel;
    private javax.swing.JTextField emailRField;
    private javax.swing.JTextField fNameRField;
    private javax.swing.JLabel firstNameLabel;
    private javax.swing.JLabel invalidEmailLabel;
    private javax.swing.JLabel invalidPassLabel;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField lNameRField;
    private javax.swing.JLabel lastNameLabel;
    private javax.swing.JButton loginButton;
    private javax.swing.JLabel loginLabel;
    private javax.swing.JLabel noUserExistsLabel;
    private javax.swing.JPasswordField passwordLField;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JLabel passwordLoginLabel;
    private javax.swing.JTextField passwordRField;
    private javax.swing.JButton registerButton;
    private javax.swing.JLabel registrationLabel;
    private javax.swing.JLabel takenUserLabel;
    private javax.swing.JTextField usernameLField;
    private javax.swing.JLabel usernameLabel;
    private javax.swing.JLabel usernameLoginLabel;
    private javax.swing.JTextField usernameRField;
    // End of variables declaration//GEN-END:variables
}
