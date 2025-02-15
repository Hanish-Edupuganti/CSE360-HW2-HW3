package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class QuestionPage {
    private final DatabaseHelper databaseHelper;

    public QuestionPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        TextField questionField = new TextField();
        questionField.setPromptText("Enter your question");

        Button submitButton = new Button("Submit Question");
        Label statusLabel = new Label();

        ListView<String> questionListView = new ListView<>();
        refreshQuestionList(questionListView);

        Button addAnswerButton = new Button("Add Answer");
        addAnswerButton.setDisable(true);  // Initially disabled

        // Submit Question
        submitButton.setOnAction(e -> {
            String questionText = questionField.getText().trim();
            if (!questionText.isEmpty()) {
                try {
                    databaseHelper.saveQuestion(questionText);
                    statusLabel.setText("Question added successfully!");
                    questionField.clear();
                    refreshQuestionList(questionListView);
                } catch (Exception ex) {
                    statusLabel.setText("Error adding question: " + ex.getMessage());
                }
            } else {
                statusLabel.setText("Question field cannot be empty");
            }
        });

        // Enable "Add Answer" button when a question is selected
        questionListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            addAnswerButton.setDisable(newValue == null);
        });

        // Open AnswerPage when clicking "Add Answer"
        addAnswerButton.setOnAction(e -> {
            String selectedQuestion = questionListView.getSelectionModel().getSelectedItem();
            if (selectedQuestion != null) {
                new AnswerPage(databaseHelper, selectedQuestion).show(primaryStage);
            }
        });

        layout.getChildren().addAll(questionField, submitButton, questionListView, addAnswerButton, statusLabel);
        primaryStage.setScene(new Scene(layout, 500, 500));
        primaryStage.setTitle("Manage Questions");
        primaryStage.show();
    }

    private void refreshQuestionList(ListView<String> listView) {
        List<String> questions = databaseHelper.getAllQuestions();
        listView.getItems().setAll(questions);
    }
}
