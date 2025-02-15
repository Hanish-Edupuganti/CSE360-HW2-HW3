package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.SQLException;

public class AdminSetupPage {

    private final DatabaseHelper databaseHelper;

    public AdminSetupPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter Admin Username");
        userNameField.setMaxWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setMaxWidth(250);

        Button setupButton = new Button("Setup");
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        setupButton.setOnAction(a -> {
            String username = userNameField.getText().trim();
            String password = passwordField.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Both fields are required!");
                return;
            }

            try {
                databaseHelper.registerUser(username, password, "admin");
                System.out.println("Admin setup completed.");

                // Navigate to Welcome Login Page
                new WelcomeLoginPage(databaseHelper).show(primaryStage);
                
            } catch (SQLException e) {
                errorLabel.setText("Database error: " + e.getMessage());
                System.err.println("Database error: " + e.getMessage());
                e.printStackTrace();
            }
        });

        layout.getChildren().addAll(userNameField, passwordField, setupButton, errorLabel);
        primaryStage.setScene(new Scene(layout, 400, 400));
        primaryStage.setTitle("Administrator Setup");
        primaryStage.show();
    }
}
