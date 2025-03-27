package application;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * AdminHomePage class represents the user interface for the admin user.
 * Displays a simple welcome message and provides a button to manage questions.
 */
public class AdminHomePage {

    /**
     * Displays the admin page in the provided primary stage.
     * @param primaryStage The primary stage where the scene will be displayed.
     * @param user The admin user (with role "admin")
     */
    public void show(Stage primaryStage, User user) {
        VBox layout = new VBox();
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        // Label to display the welcome message for the admin (includes user's name)
        Label adminLabel = new Label("Hello, " + user.getUserName() + "!");
        adminLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Button to take the admin to the questions management page
        Button manageQuestionsBtn = new Button("Manage Questions");
        manageQuestionsBtn.setOnAction(e -> {
            // Access shared in-memory questions and answers
            Questions questions = StartCSE360.getQuestions();
            Answers answers = StartCSE360.getAnswers();

            // Display the QuestionsPage, passing the current admin user
            new QuestionsPage(questions, answers, user).show(primaryStage);
        });

        layout.getChildren().addAll(adminLabel, manageQuestionsBtn);
        Scene adminScene = new Scene(layout, 800, 400);

        primaryStage.setScene(adminScene);
        primaryStage.setTitle("Admin HomePage");
    }
}
