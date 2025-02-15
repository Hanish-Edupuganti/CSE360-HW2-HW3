package application;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminHomePage {

    public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label welcomeLabel = new Label("Welcome Admin!");
        layout.getChildren().add(welcomeLabel);

        Scene adminScene = new Scene(layout, 600, 400);
        primaryStage.setScene(adminScene);
        primaryStage.setTitle("Admin Dashboard");
    }
}
