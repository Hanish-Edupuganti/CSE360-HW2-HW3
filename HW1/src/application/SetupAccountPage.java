package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.SQLException;

public class SetupAccountPage {

    private final DatabaseHelper databaseHelper;

    public SetupAccountPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter Username");
        userNameField.setMaxWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setMaxWidth(250);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        Button setupButton = new Button("Setup");

        setupButton.setOnAction(e -> {
            String username = userNameField.getText().trim();
            String password = passwordField.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Username and password are required!");
                return;
            }

            try {
                databaseHelper.registerUser(username, password, "user");
                System.out.println("User registered successfully!");

                // Navigate to WelcomeLoginPage
                new WelcomeLoginPage(databaseHelper).show(primaryStage);
            } catch (SQLException ex) {
                errorLabel.setText("Database error: " + ex.getMessage());
                System.err.println("Database error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        layout.getChildren().addAll(userNameField, passwordField, setupButton, errorLabel);
        primaryStage.setScene(new Scene(layout, 400, 400));
        primaryStage.setTitle("Create an Account");
        primaryStage.show();
    }
}
