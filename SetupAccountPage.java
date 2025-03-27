package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.SQLException;

import databasePart1.*;

/**
 * SetupAccountPage class handles the account setup process for new users.
 * Users provide their userName, password, and a valid invitation code to register.
 */
public class SetupAccountPage {
	
    private final DatabaseHelper databaseHelper;
    // DatabaseHelper to handle database operations.
    public SetupAccountPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    /**
     * Displays the Setup Account page in the provided stage.
     * @param primaryStage The primary stage where the scene will be displayed.
     */
    public void show(Stage primaryStage) {
    	// Input fields for userName, password, and invitation code
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter userName");
        userNameField.setMaxWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setMaxWidth(250);
        
        TextField inviteCodeField = new TextField();
        inviteCodeField.setPromptText("Enter InvitationCode");
        inviteCodeField.setMaxWidth(250);
        
        // Labels for the validation feedback
        Label labelRequirements = new Label("Password Requirements:");
        Label labelUpperCase = new Label("At least one uppercase letter");
        Label labelLowerCase = new Label("At least one lowercase letter");
        Label labelNumeric = new Label("At least one numeric digit");
        Label labelSpecial = new Label("At least one special character");
        Label labelLength = new Label("At least eight characters");
        
        // Label to display error messages for invalid input or registration issues
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        

        Button setupButton = new Button("Setup");
        
        // I added a listener to the passwordField for real time feedback to the users input
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            updatePasswordFeedback(newValue, labelUpperCase, labelLowerCase, labelNumeric, labelSpecial, labelLength);
        });
        
        setupButton.setOnAction(a -> {
        	// Retrieve user input
            String userName = userNameField.getText();
            String password = passwordField.getText();
            String code = inviteCodeField.getText();
            
            String passwordValidationResult = PasswordEvaluator.evaluatePassword(password);
            
            // If user still tries to set up account with an invalid password,
            // we will push an errorLabel mentioning that it does not meet requirements. 
            if (!passwordValidationResult.isEmpty()) {
            	errorLabel.setText("Password does not meet the requirements!!!");
                return;
            }
            
            try {
            	// Check if the user already exists
            	if(!databaseHelper.doesUserExist(userName)) {
            		
            		// Checks whether user name is valid by cross checking if the error
            		// output is empty
            		if (UserNameRecognizer.checkForValidUserName(userName) == "") {
            		
	            		// Validate the invitation code
	            		if(databaseHelper.validateInvitationCode(code)) {
	            			
	            			// Create a new user and register them in the database
			            	User user=new User(userName, password, "user");
			                databaseHelper.register(user);
			                
			             // Navigate to the Welcome Login Page
			                new WelcomeLoginPage(databaseHelper).show(primaryStage,user);
	            		}
	            		else {
	            			errorLabel.setText("Please enter a valid invitation code");
	            		}
            		} 
            		else { // Else it will print the error message for invalid user name
            			errorLabel.setText(UserNameRecognizer.checkForValidUserName(userName));
            		}
            	}
            	else {
            		errorLabel.setText("This useruserName is taken!!.. Please use another to setup an account");
            	}
            	
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
                e.printStackTrace();
            }
        });

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.getChildren().addAll(userNameField, passwordField,inviteCodeField, labelRequirements, labelUpperCase, labelLowerCase, labelNumeric, labelSpecial, labelLength, setupButton, errorLabel);

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("Account Setup");
        primaryStage.show();
    }
    
    // This updates the feedback labels for the password requirements in real time as the user types
    private void updatePasswordFeedback(String password, Label labelUpperCase, Label labelLowerCase, Label labelNumeric, Label labelSpecial, Label labelLength) {
		PasswordEvaluator.evaluatePassword(password);
		
		labelUpperCase.setTextFill(PasswordEvaluator.foundUpperCase ? Color.GREEN : Color.RED);
		labelLowerCase.setTextFill(PasswordEvaluator.foundLowerCase ? Color.GREEN : Color.RED);
		labelNumeric.setTextFill(PasswordEvaluator.foundNumericDigit ? Color.GREEN : Color.RED);
		labelSpecial.setTextFill(PasswordEvaluator.foundSpecialChar ? Color.GREEN : Color.RED);
		labelLength.setTextFill(PasswordEvaluator.foundLongEnough ? Color.GREEN : Color.RED);
	}

}