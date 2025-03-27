package application;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * UserHomePage class represents the user interface for a regular user.
 * Displays a simple welcome message and provides a button to manage questions.
 */
public class UserHomePage {

    /**
     * Displays the user page in the provided primary stage.
     * @param primaryStage The primary stage where the scene will be displayed.
     * @param user The logged-in user with role "user"
     */
    public void show(Stage primaryStage, User user) {
        VBox layout = new VBox();
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        // Label to display Hello user (includes user's name)
        Label userLabel = new Label("Hello, " + user.getUserName() + "!");
        userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Button to open the questions manager
        Button manageQuestionsBtn = new Button("Questions Page");
        manageQuestionsBtn.setOnAction(e -> {
            Questions questions = StartCSE360.getQuestions();
            Answers answers = StartCSE360.getAnswers();
            new QuestionsPage(questions, answers, user).show(primaryStage);
        });
        

        layout.getChildren().addAll(userLabel, manageQuestionsBtn);
        Scene userScene = new Scene(layout, 800, 400);

        primaryStage.setScene(userScene);
        primaryStage.setTitle("HomePage");
    }
}
