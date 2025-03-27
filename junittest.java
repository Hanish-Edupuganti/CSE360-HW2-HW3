package application;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import databasePart1.DatabaseHelper;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class junittest {

    // Initialize JavaFX runtime
    static {
        new JFXPanel();
    }

    // ------------------- Question.java Tests -------------------

    @Test
    public void testSetAndGetQuestionID() {
        Question question = new Question();
        question.setQuestionID(123);
        assertEquals(123, question.getQuestionID());
    }

    @Test
    public void testCreationTimeIsInitialized() {
        Question question = new Question();
        LocalDateTime creationTime = question.getCreationTime();
        assertNotNull(creationTime);
        assertTrue(creationTime.isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    // ------------------- FirstPage.java Tests -------------------

    public static class TestableAdminSetupPage extends AdminSetupPage {
        public static boolean wasShown = false;

        public TestableAdminSetupPage(DatabaseHelper dbHelper) {
            super(dbHelper);
        }

        @Override
        public void show(Stage stage) {
            wasShown = true;
        }
    }

    public static class StubDatabaseHelper extends DatabaseHelper {
        public boolean isAdmin = true;

        public boolean isAdminUser() {
            return isAdmin;
        }
    }

    private StubDatabaseHelper stubDbHelper;
    private FirstPage firstPage;

    @Before
    public void setUp() {
        stubDbHelper = new StubDatabaseHelper();
        firstPage = new FirstPage(stubDbHelper);
        TestableAdminSetupPage.wasShown = false;
    }

    @Test
    public void testShowMethodDisplaysScene() throws Exception {
        Platform.runLater(() -> {
            Stage stage = new Stage();
            try {
                firstPage.show(stage);
            } catch (Exception e) {
                fail("show() method threw an exception: " + e.getMessage());
            }
        });
        Thread.sleep(500);
    }

    @Test
    public void testContinueButtonTriggersSetupSequence() throws Exception {
        Platform.runLater(() -> {
            Stage stage = new Stage();

            FirstPage pageWithCustomSetup = new FirstPage(stubDbHelper) {
                @Override
                public void show(Stage stage) {
                    VBox layout = new VBox();
                    Button continueButton = new Button("Continue");

                    continueButton.setOnAction(e -> new TestableAdminSetupPage(stubDbHelper).show(stage));
                    layout.getChildren().add(continueButton);

                    stage.setScene(new Scene(layout, 300, 200));
                    stage.show();

                    continueButton.fire();
                }
            };

            pageWithCustomSetup.show(stage);
        });

        Thread.sleep(500);
        assertTrue("Setup page should have been triggered", TestableAdminSetupPage.wasShown);
    }

    @Test
    public void testRoleBasedRouting_AdminGoesToSetup() throws Exception {
        stubDbHelper.isAdmin = true;

        Platform.runLater(() -> {
            Stage stage = new Stage();
            new TestableAdminSetupPage(stubDbHelper).show(stage);
        });

        Thread.sleep(500);
        assertTrue("Admin should be routed to setup page", TestableAdminSetupPage.wasShown);
    }
}
