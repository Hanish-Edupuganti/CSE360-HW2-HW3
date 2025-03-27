package application;

import databasePart1.DatabaseHelper;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Database-backed manager for Question objects,
 * delegating to DatabaseHelper for all CRUD.
 */
public class Questions {
    
    private DatabaseHelper dbHelper;

    public Questions(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    // Create
    public void addQuestion(Question question) {
        try {
            dbHelper.createQuestion(question);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Read (all)
    public List<Question> getAllQuestions() {
        return dbHelper.getAllQuestions();
    }

    // Read (by ID)
    public Question getQuestionByID(int questionID) {
        return dbHelper.getQuestionByID(questionID);
    }

    // Update
    public boolean updateQuestion(Question question) {
        try {
            dbHelper.updateQuestion(question);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete
    public boolean deleteQuestion(int questionID) {
        try {
            dbHelper.deleteQuestion(questionID);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Search by keyword in questionText
    public List<Question> searchByKeyword(String keyword) {
        List<Question> all = dbHelper.getAllQuestions();
        if (keyword == null || keyword.isBlank()) {
            return all;
        }
        return all.stream()
            .filter(q -> q.getQuestionText().toLowerCase().contains(keyword.toLowerCase()))
            .collect(Collectors.toList());
    }
}
