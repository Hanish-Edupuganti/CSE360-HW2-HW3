package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WelcomeLoginPage {

    private final DatabaseHelper databaseHelper;

    public WelcomeLoginPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label welcomeLabel = new Label("Welcome!");
        welcomeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Button continueButton = new Button("Continue to Login");

        continueButton.setOnAction(e -> {
            new UserLoginPage(databaseHelper).show(primaryStage);
        });

        layout.getChildren().addAll(welcomeLabel, continueButton);
        primaryStage.setScene(new Scene(layout, 400, 300));
        primaryStage.setTitle("Welcome Page");
        primaryStage.show();
    }
}
