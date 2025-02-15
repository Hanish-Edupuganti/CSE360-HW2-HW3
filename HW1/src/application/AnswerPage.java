package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AnswerPage {
    private final DatabaseHelper databaseHelper;
    private final String questionText;

    public AnswerPage(DatabaseHelper databaseHelper, String questionText) {
        this.databaseHelper = databaseHelper;
        this.questionText = questionText;
    }

    public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label questionLabel = new Label("Question: " + questionText);
        questionLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        TextArea answerField = new TextArea();
        answerField.setPromptText("Enter your answer");

        Button submitButton = new Button("Submit Answer");
        Label statusLabel = new Label();

        submitButton.setOnAction(e -> {
            String answerText = answerField.getText().trim();
            if (!answerText.isEmpty()) {
                try {
                    databaseHelper.saveAnswer(questionText, answerText);
                    statusLabel.setText("Answer saved successfully!");
                    answerField.clear();
                } catch (Exception ex) {
                    statusLabel.setText("Error saving answer: " + ex.getMessage());
                }
            } else {
                statusLabel.setText("Answer field cannot be empty");
            }
        });

        layout.getChildren().addAll(questionLabel, answerField, submitButton, statusLabel);
        primaryStage.setScene(new Scene(layout, 500, 400));
        primaryStage.setTitle("Add Answer");
        primaryStage.show();
    }
}
