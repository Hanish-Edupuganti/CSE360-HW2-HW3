package application;

/**
 * The Answer class represents an answer to a question.
 * Each answer has an ID, question ID, text content, and author.
 */
public class Answer {
    private int id;
    private int questionId;
    private String answerText;
    private String author;

    // Constructor
    public Answer(int id, int questionId, String answerText, String author) {
        this.id = id;
        this.questionId = questionId;
        this.answerText = answerText;
        this.author = author;
    }

    // Getters and Setters
    public int getId() { return id; }
    public int getQuestionId() { return questionId; }
    public String getAnswerText() { return answerText; }
    public void setAnswerText(String answerText) { this.answerText = answerText; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
}
