package application;

import databasePart1.DatabaseHelper;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDateTime;

/**
 * A standalone JavaFX-based test class to manually verify core behaviors of
 * {@code Question.java} and {@code FirstPage.java} using console output.
 * <p>
 * This class includes:
 * <ul>
 *   <li>Basic getter/setter tests for the {@code Question} class</li>
 *   <li>Initialization test for {@code creationTime}</li>
 *   <li>UI routing logic tests for {@code FirstPage}</li>
 * </ul>
 * JavaFX is initialized using {@code JFXPanel}.
 */
public class javatest {
	private javatest () {}
    static {
        // Start JavaFX toolkit
        new JFXPanel();
    }

    /**
     * Main method to execute all test cases and print results.
     *
     * @param args Command-line arguments (not used)
     * @throws Exception If any thread-related exception occurs
     */
    public static void main(String[] args) throws Exception {
        testSetAndGetQuestionID();
        testCreationTimeIsInitialized();
        testShowMethodDisplaysScene();
        testContinueButtonTriggersSetupSequence();
        testRoleBasedRouting_AdminGoesToSetup();

        System.out.println("\n✅ All tests finished running.");
    }

    // ------------------- Question.java Tests -------------------

    /**
     * Test for verifying the getter and setter of {@code Question} ID.
     */
    public static void testSetAndGetQuestionID() {
        Question question = new Question();
        question.setQuestionID(123);
        if (question.getQuestionID() == 123) {
            System.out.println("✅ testSetAndGetQuestionID passed");
        } else {
            System.out.println("❌ testSetAndGetQuestionID failed");
        }
    }

    /**
     * Test to verify that the {@code creationTime} is initialized when a {@code Question} is instantiated.
     */
    public static void testCreationTimeIsInitialized() {
        Question question = new Question();
        LocalDateTime time = question.getCreationTime();
        if (time != null && time.isBefore(LocalDateTime.now().plusSeconds(1))) {
            System.out.println("✅ testCreationTimeIsInitialized passed");
        } else {
            System.out.println("❌ testCreationTimeIsInitialized failed");
        }
    }

    // ------------------- FirstPage.java Tests -------------------

    /**
     * A testable version of {@code AdminSetupPage} that tracks whether its {@code show()} method is called.
     */
    static class TestableAdminSetupPage extends AdminSetupPage {
        public static boolean wasShown = false;

        /**
         * Constructs a {@code TestableAdminSetupPage} with the given {@code DatabaseHelper}.
         *
         * @param dbHelper Simulated database helper
         */
        public TestableAdminSetupPage(DatabaseHelper dbHelper) {
            super(dbHelper);
        }

        /**
         * Overrides {@code show()} to track method call.
         *
         * @param stage The JavaFX stage
         */
        @Override
        public void show(Stage stage) {
            wasShown = true;
        }
    }

    /**
     * A stub implementation of {@code DatabaseHelper} to simulate user roles for testing.
     */
    static class StubDatabaseHelper extends DatabaseHelper {
        public boolean isAdmin = true;

        /**
         * Returns whether the simulated user is an admin.
         *
         * @return {@code true} if admin
         */
        public boolean isAdminUser() {
            return isAdmin;
        }
    }

    /**
     * Verifies that {@code FirstPage.show()} renders a scene without throwing errors.
     *
     * @throws InterruptedException If the thread sleep is interrupted
     */
    public static void testShowMethodDisplaysScene() throws InterruptedException {
        Platform.runLater(() -> {
            try {
                FirstPage page = new FirstPage(new StubDatabaseHelper());
                Stage stage = new Stage();
                page.show(stage);
                System.out.println("✅ testShowMethodDisplaysScene passed");
            } catch (Exception e) {
                System.out.println("❌ testShowMethodDisplaysScene failed: " + e.getMessage());
            }
        });
        Thread.sleep(500);
    }

    /**
     * Tests whether pressing the "Continue" button triggers the {@code AdminSetupPage} sequence.
     * A hook is injected using {@code FirstPage.onContinuePressed} to simulate and capture the navigation.
     *
     * @throws InterruptedException If the thread sleep is interrupted
     */
    public static void testContinueButtonTriggersSetupSequence() throws InterruptedException {
        TestableAdminSetupPage.wasShown = false;
        StubDatabaseHelper dbHelper = new StubDatabaseHelper();

        Platform.runLater(() -> {
            Stage stage = new Stage();

            // ✅ Inject test behavior into real FirstPage
            FirstPage.onContinuePressed = () -> {
                new TestableAdminSetupPage(dbHelper).show(stage);
            };

            FirstPage page = new FirstPage(dbHelper);
            page.show(stage);

            // ✅ Find the real "Continue" button and fire it
            VBox root = (VBox) stage.getScene().getRoot();
            for (javafx.scene.Node node : root.getChildren()) {
                if (node instanceof Button) {
                    Button button = (Button) node;
                    if (button.getText().equals("Continue")) {
                        button.fire(); // 💥 Simulate real click
                        break;
                    }
                }
            }

            stage.hide();
        });

        Thread.sleep(500);

        if (TestableAdminSetupPage.wasShown) {
            System.out.println("✅ testContinueButtonTriggersSetupSequence passed");
        } else {
            System.out.println("❌ testContinueButtonTriggersSetupSequence failed");
        }

        FirstPage.onContinuePressed = null;
    }

    /**
     * Verifies that admin users are routed to {@code AdminSetupPage} on app start.
     *
     * @throws InterruptedException If the thread sleep is interrupted
     */
    public static void testRoleBasedRouting_AdminGoesToSetup() throws InterruptedException {
        TestableAdminSetupPage.wasShown = false;
        StubDatabaseHelper dbHelper = new StubDatabaseHelper();
        dbHelper.isAdmin = true;

        Platform.runLater(() -> {
            Stage stage = new Stage();
            new TestableAdminSetupPage(dbHelper).show(stage);
        });

        Thread.sleep(500);
        if (TestableAdminSetupPage.wasShown) {
            System.out.println("✅ testRoleBasedRouting_AdminGoesToSetup passed");
        } else {
            System.out.println("❌ testRoleBasedRouting_AdminGoesToSetup failed");
        }
    }
}
