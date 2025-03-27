package application;

import databasePart1.DatabaseHelper;
import java.sql.SQLException;
import java.util.List;

/**
 * Database-backed manager for Answer objects,
 * delegating to DatabaseHelper for all CRUD.
 */
public class Answers {

    private DatabaseHelper dbHelper;

    public Answers(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    // Create
    public void addAnswer(Answer answer) {
        try {
            dbHelper.createAnswer(answer);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Read
    public Answer getAnswerByID(int answerID) {
        return dbHelper.getAnswerByID(answerID);
    }

    public List<Answer> getAnswersByQuestionID(int questionID) {
        return dbHelper.getAnswersByQuestionID(questionID);
    }

    public List<Answer> getAllAnswers() {
        return dbHelper.getAllAnswers();
    }

    // Update
    public boolean updateAnswer(Answer answer) {
        try {
            dbHelper.updateAnswer(answer);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete
    public boolean deleteAnswer(int answerID) {
        try {
            dbHelper.deleteAnswer(answerID);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
