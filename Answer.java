package application;

import java.time.LocalDateTime;

/**
 * Represents an Answer in the database. 
 * Upvote/downvote counts are stored in columns upvotes/downvotes.
 */
public class Answer {
    private int answerID;
    private int questionID;
    private String author;
    private String answerText;
    private LocalDateTime creationTime;
    private int upvotes;
    private int downvotes;
    private boolean accepted = false;

    public Answer() {
        this.creationTime = LocalDateTime.now();
    }

    public Answer(int answerID, int questionID, String author, String answerText) {
        this();
        setAnswerID(answerID);
        setQuestionID(questionID);
        setAuthor(author);
        setAnswerText(answerText);
    }

    public int getAnswerID() {
        return answerID;
    }
    public void setAnswerID(int answerID) {
        this.answerID = answerID;
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

    public String getAnswerText() {
        return answerText;
    }
    public void setAnswerText(String answerText) {
        if (answerText == null || answerText.isBlank()) {
            throw new IllegalArgumentException("Answer text cannot be null or blank.");
        }
        this.answerText = answerText;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }
    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public int getUpvotes() {
        return upvotes;
    }
    public int getDownvotes() {
        return downvotes;
    }

    public void upvote() {
        this.upvotes++;
    }
    public void downvote() {
        this.downvotes++;
    }

    public void setUpvotesCount(int upvotes) {
        this.upvotes = upvotes;
    }
    public void setDownvotesCount(int downvotes) {
        this.downvotes = downvotes;
    }

    public boolean isAccepted() {
        return accepted;
    }
    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    @Override
    public String toString() {
        // e.g. "Alice (Accepted): My answer text"
        StringBuilder sb = new StringBuilder();
        sb.append(author);
        if (accepted) {
            sb.append(" (Accepted)");
        }
        sb.append(": ").append(answerText);
        return sb.toString();
    }
}
