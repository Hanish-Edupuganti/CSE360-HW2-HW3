package application;

/**
 * Represents a Question object with an ID and text.
 */
public class Question {
    private int id;
    private String questionText;

    public Question(int id, String questionText) {  
        this.id = id;
        this.questionText = questionText;
    }

    public int getId() { return id; }
    
    public String getQuestionText() { return questionText; } 
    
    public void setQuestionText(String text) { this.questionText = text; } 

    @Override
    public String toString() {
        return questionText; // Ensures proper display in ListView
    }
}
