package application;

import java.time.LocalDateTime;

/**
 * Represents a single Question stored in the database.
 */
public class Question {
    
    private int questionID;
    private String author;
    private String questionTitle;
    private String questionText;
    private LocalDateTime creationTime;
    private boolean solved = false;
    private int acceptedAnswerID = 0;

    public Question() {
        this.creationTime = LocalDateTime.now();
    }

    public Question(String author, String title, String text) {
        this();
        setAuthor(author);
        setQuestionTitle(title);
        setQuestionText(text);
    }

    public int getQuestionID() {
        return questionID;
    }
    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        if (author == null || author.isBlank()) {
            throw new IllegalArgumentException("Author cannot be null or blank.");
        }
        this.author = author;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }
    public void setQuestionTitle(String questionTitle) {
        if (questionTitle == null || questionTitle.isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or blank.");
        }
        this.questionTitle = questionTitle;
    }

    public String getQuestionText() {
        return questionText;
    }
    public void setQuestionText(String questionText) {
        if (questionText == null || questionText.isBlank()) {
            throw new IllegalArgumentException("Text cannot be null or blank.");
        }
        this.questionText = questionText;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }
    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public boolean isSolved() {
        return solved;
    }
    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public int getAcceptedAnswerID() {
        return acceptedAnswerID;
    }
    public void setAcceptedAnswerID(int acceptedAnswerID) {
        this.acceptedAnswerID = acceptedAnswerID;
    }

    @Override
    public String toString() {
        // e.g. "My Title (Answered)" if solved is true
        if (solved) {
            return questionTitle + " (Answered)";
        }
        return questionTitle;
    }
}
