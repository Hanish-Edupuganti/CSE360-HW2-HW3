package databasePart1;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
    // Database connection details
    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:./FoundationDatabase";
    private static final String USER = "sa";
    private static final String PASS = "";

    private Connection connection;
    private Statement statement;

    // Constructor: Establish database connection
    public DatabaseHelper() {
        try {
            Class.forName(JDBC_DRIVER); // Load H2 driver
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            statement = connection.createStatement();

            // Create necessary tables if they don't exist
            createTables();
            createDefaultAdmin();
        } catch (Exception e) {
            System.err.println("Database connection error: " + e.getMessage());
        }
    }

    // **Creates tables for users, questions, and answers**
    private void createTables() throws SQLException {
        String userTable = "CREATE TABLE IF NOT EXISTS Users (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "userName VARCHAR(255) UNIQUE NOT NULL, " +
                "password VARCHAR(255) NOT NULL, " +
                "role VARCHAR(50) NOT NULL)";
        statement.execute(userTable);

        String questionTable = "CREATE TABLE IF NOT EXISTS Questions (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "questionText VARCHAR(500) UNIQUE NOT NULL)";
        statement.execute(questionTable);

        String answerTable = "CREATE TABLE IF NOT EXISTS Answers (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "questionText VARCHAR(500) NOT NULL, " +
                "answerText VARCHAR(1000) NOT NULL)";
        statement.execute(answerTable);
    }

    // **Creates a default admin account if the database is empty**
    private void createDefaultAdmin() throws SQLException {
        if (isDatabaseEmpty()) {
            String insertAdmin = "INSERT INTO Users (userName, password, role) VALUES (?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(insertAdmin);
            pstmt.setString(1, "admin");
            pstmt.setString(2, "admin123"); // Default admin password
            pstmt.setString(3, "admin");
            pstmt.executeUpdate();
            System.out.println("Default Admin user created: admin/admin123");
        }
    }

    // **Checks if database is empty**
    public boolean isDatabaseEmpty() throws SQLException {
        String query = "SELECT COUNT(*) FROM Users";
        ResultSet rs = statement.executeQuery(query);
        return rs.next() && rs.getInt(1) == 0;
    }

    // **Registers a new user**
    public void registerUser(String userName, String password, String role) throws SQLException {
        String query = "INSERT INTO Users (userName, password, role) VALUES (?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, userName);
        pstmt.setString(2, password);
        pstmt.setString(3, role);
        pstmt.executeUpdate();
    }

    // **Checks if a user exists**
    public boolean login(String userName, String password) throws SQLException {
        String query = "SELECT * FROM Users WHERE userName = ? AND password = ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, userName);
        pstmt.setString(2, password);
        ResultSet rs = pstmt.executeQuery();
        return rs.next();
    }

    // **Retrieves user role**
    public String getUserRole(String userName) throws SQLException {
        String query = "SELECT role FROM Users WHERE userName = ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, userName);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getString("role");
        }
        return null;
    }

    // **Saves a new question**
    public void saveQuestion(String question) throws SQLException {
        String query = "INSERT INTO Questions (questionText) VALUES (?)";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, question);
        pstmt.executeUpdate();
    }

    // **Retrieves all questions**
    public List<String> getAllQuestions() {
        List<String> questions = new ArrayList<>();
        try {
            ResultSet rs = statement.executeQuery("SELECT questionText FROM Questions");
            while (rs.next()) {
                questions.add(rs.getString("questionText"));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching questions: " + e.getMessage());
        }
        return questions;
    }

    // **Saves an answer for a specific question**
    public void saveAnswer(String question, String answer) throws SQLException {
        String query = "INSERT INTO Answers (questionText, answerText) VALUES (?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, question);
        pstmt.setString(2, answer);
        pstmt.executeUpdate();
    }

    // **Retrieves all answers for a given question**
    public List<String> getAnswersForQuestion(String question) {
        List<String> answers = new ArrayList<>();
        try {
            String query = "SELECT answerText FROM Answers WHERE questionText = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, question);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                answers.add(rs.getString("answerText"));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching answers: " + e.getMessage());
        }
        return answers;
    }

    // **Closes the database connection**
    public void closeConnection() {
        try {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        } catch (SQLException e) {
            System.err.println("Error closing database: " + e.getMessage());
        }
    }

	public void connectToDatabase() {
		// TODO Auto-generated method stub
		
	}
}
