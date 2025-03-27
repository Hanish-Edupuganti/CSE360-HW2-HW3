package application;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A UI page that displays all questions from the DB,
 * combined searching (questions + answers), adding, and deleting.
 * Only an admin or the question's author can delete.
 */
public class QuestionsPage {

    private final Questions questions;  // DB-based
    private final Answers answers;      // DB-based
    private final User currentUser;     // admin or user

    public QuestionsPage(Questions questions, Answers answers, User currentUser) {
        this.questions = questions;
        this.answers = answers;
        this.currentUser = currentUser;
    }

    public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        Label lblTitle = new Label("Manage Questions");
        TextField txtSearch = new TextField();
        txtSearch.setPromptText("Enter keyword to search in questions OR answers...");

        Button btnSearch = new Button("Search");
        Button btnRefresh = new Button("Show All");

        ListView<Question> listView = new ListView<>();
        listView.getItems().addAll(questions.getAllQuestions());

        // Fields to create a new question
        TextField txtQTitle = new TextField();
        txtQTitle.setPromptText("Question Title");
        TextField txtQText = new TextField();
        txtQText.setPromptText("Question Text");
        Button btnAdd = new Button("Add Question");

        Button btnDelete = new Button("Delete Selected Question");

        Button btnBack = new Button("Back to Home Page");
        btnBack.setOnAction(e -> {
            if ("admin".equalsIgnoreCase(currentUser.getRole())) {
                new AdminHomePage().show(primaryStage, currentUser);
            } else {
                new UserHomePage().show(primaryStage, currentUser);
            }
        });

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");

        // Searching Q + A
        btnSearch.setOnAction(e -> {
            String keyword = txtSearch.getText();
            listView.getItems().clear();
            if (keyword != null && !keyword.isBlank()) {
                combinedSearch(keyword, listView);
            }
            errorLabel.setText("");
        });

        btnRefresh.setOnAction(e -> {
            listView.getItems().clear();
            listView.getItems().addAll(questions.getAllQuestions());
            errorLabel.setText("");
        });

        // Add question
        btnAdd.setOnAction(e -> {
            try {
                String qTitle = txtQTitle.getText();
                String qText = txtQText.getText();

                Question newQ = new Question();
                newQ.setAuthor(currentUser.getUserName());
                newQ.setQuestionTitle(qTitle);
                newQ.setQuestionText(qText);

                questions.addQuestion(newQ);

                txtQTitle.clear();
                txtQText.clear();

                // Refresh from DB
                listView.getItems().clear();
                listView.getItems().addAll(questions.getAllQuestions());
                errorLabel.setText("");
            } catch (Exception ex) {
                System.out.println("Error adding question: " + ex.getMessage());
            }
        });

        // Double-click -> question detail
        listView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Question selected = listView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    new QuestionDetailPage(selected, questions, answers, currentUser).show(primaryStage);
                }
                errorLabel.setText("");
            }
        });

        // Delete question
        btnDelete.setOnAction(e -> {
            Question selected = listView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                errorLabel.setText("No question selected!");
                return;
            }

            boolean isAdmin = "admin".equalsIgnoreCase(currentUser.getRole());
            boolean isAuthor = selected.getAuthor().equals(currentUser.getUserName());

            if (isAdmin || isAuthor) {
                errorLabel.setText("");
                questions.deleteQuestion(selected.getQuestionID());
                listView.getItems().clear();
                listView.getItems().addAll(questions.getAllQuestions());
                System.out.println("Question and related answers deleted successfully.");
            } else {
                errorLabel.setText("You do not have permission to delete this question!");
            }
        });

        layout.getChildren().addAll(
            lblTitle,
            txtSearch, btnSearch, btnRefresh,
            listView,
            new Label("Create a New Question:"),
            txtQTitle, txtQText, btnAdd,
            btnDelete,
            btnBack,
            errorLabel
        );

        Scene scene = new Scene(layout, 600, 550);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Questions Management (DB-Backed)");
        primaryStage.show();
    }

    /**
     * Combined search of questions + answers in memory.
     * If you want to do it purely in SQL, you'd write a join-based query.
     */
    private void combinedSearch(String keyword, ListView<Question> listView) {
        // 1) Search questions by text
        List<Question> questionsFound = questions.searchByKeyword(keyword);

        // 2) Search answers for the keyword
        List<Answer> allAnswers = answers.getAllAnswers();
        List<Answer> answersFound = allAnswers.stream()
                .filter(a -> a.getAnswerText().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());

        // 3) Get question IDs from those answers
        Set<Integer> questionIDs = answersFound.stream()
                .map(Answer::getQuestionID)
                .collect(Collectors.toSet());

        // 4) For each questionID, retrieve that question
        for (int qID : questionIDs) {
            Question q = questions.getQuestionByID(qID);
            if (q != null) {
                questionsFound.add(q);
            }
        }

        // 5) Remove duplicates
        List<Question> uniqueResults = questionsFound.stream()
                .distinct()
                .collect(Collectors.toList());

        listView.getItems().addAll(uniqueResults);
    }
}
