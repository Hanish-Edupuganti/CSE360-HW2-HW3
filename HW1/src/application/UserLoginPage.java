package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.SQLException;

public class UserLoginPage {

    private final DatabaseHelper databaseHelper;

    public UserLoginPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        Button loginButton = new Button("Login");

        loginButton.setOnAction(a -> {
            String userName = userNameField.getText().trim();
            String password = passwordField.getText().trim();

            if (userName.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Username and password are required!");
                return;
            }

            try {
                if (databaseHelper.login(userName, password)) {
                    System.out.println("Login successful: " + userName);
                    String role = databaseHelper.getUserRole(userName);

                    if (role != null) {
                        if (role.equals("admin")) {
                            new AdminHomePage().show(primaryStage);
                        } else {
                            new QuestionPage(databaseHelper).show(primaryStage);
                        }
                    } else {
                        errorLabel.setText("Error retrieving user role.");
                    }
                } else {
                    errorLabel.setText("Invalid username or password.");
                }
            } catch (SQLException e) {
                errorLabel.setText("Database error: " + e.getMessage());
                System.err.println("Database error: " + e.getMessage());
                e.printStackTrace();
            }
        });

        layout.getChildren().addAll(userNameField, passwordField, loginButton, errorLabel);
        Scene loginScene = new Scene(layout, 400, 400);
        primaryStage.setScene(loginScene);
        primaryStage.setTitle("User Login");
    }
}
