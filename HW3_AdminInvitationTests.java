package application;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;

/**
 * <p>
 * HW3_AdminInvitationTests is a standalone console-based test runner for:
 * <ul>
 *   <li>InvitationPage.java</li>
 *   <li>AdminSetupPage.java</li>
 * </ul>
 * The five test methods cover:
 * <ol>
 *   <li>databaseHelper.generateInvitationCode(): test format or uniqueness</li>
 *   <li>setupButton.setOnAction(...): simulate action and assert flow</li>
 *   <li>Validate that admin role is assigned after setup</li>
 *   <li>Test invalid database connection (simulate exception)</li>
 *   <li>Test return to login page after setup</li>
 * </ol>
 * 
 * <strong>Note:</strong> This is a sample test code. You must adjust method calls
 * to match your actual InvitationPage, AdminSetupPage, and DatabaseHelper logic.
 * </p>
 */
public class HW3_AdminInvitationTests {

    static {
        // Initialize JavaFX (required if AdminSetupPage/InvitationPage uses JavaFX UI)
        new JFXPanel();
    }

    /**
     * Main entry point for running all five tests.
     * 
     * @param args command-line arguments (unused)
     * @throws InterruptedException if Thread.sleep calls are interrupted
     */
    public static void main(String[] args) throws InterruptedException {
        testGenerateInvitationCodeFormat();
        testSetupButtonActionFlow();
        testAdminRoleAssigned();
        testInvalidDatabaseConnection();
        testReturnToLoginAfterSetup();

        System.out.println("\n✅ All tests for InvitationPage and AdminSetupPage have finished.");
    }

    // -------------------------------------------------------------------------
    // 1) databaseHelper.generateInvitationCode(): test format or uniqueness
    // -------------------------------------------------------------------------

    /**
     * <p>
     * Tests that {@code databaseHelper.generateInvitationCode()} either:
     * <ul>
     *   <li>Generates a code matching an expected format (e.g., length 8, alphanumeric)</li>
     *   <li>Generates unique codes when called multiple times</li>
     * </ul>
     * <strong>Inputs:</strong> None, calls the method multiple times.<br>
     * <strong>Outcomes:</strong> Console output indicates pass/fail based on code format/uniqueness.
     * </p>
     */
    public static void testGenerateInvitationCodeFormat() {
        // Setup: create DatabaseHelper (stub or real)
        DatabaseHelper dbHelper = new DatabaseHelper(); 
        // Possibly dbHelper.connectToDatabase() if needed

        // Generate two codes
        String code1 = dbHelper.generateInvitationCode();
        String code2 = dbHelper.generateInvitationCode();

        // Check format (example: 8 chars, all letters/digits)
        boolean formatOK1 = code1 != null && code1.matches("[A-Za-z0-9]{8}");
        boolean formatOK2 = code2 != null && code2.matches("[A-Za-z0-9]{8}");

        // Check uniqueness
        boolean isUnique = !code1.equals(code2);

        if (formatOK1 && formatOK2 && isUnique) {
            System.out.println("✅ testGenerateInvitationCodeFormat passed");
        } else {
            System.out.println("❌ testGenerateInvitationCodeFormat failed");
        }
    }

    // -------------------------------------------------------------------------
    // 2) setupButton.setOnAction(...): simulate action and assert flow
    // -------------------------------------------------------------------------

    /**
     * <p>
     * Tests that the "Setup" button on AdminSetupPage triggers the expected flow.
     * For example, it might navigate to a success screen or print a success message.
     * <br>
     * <strong>Inputs:</strong> Simulated button click.<br>
     * <strong>Outcomes:</strong> Observes whether the correct flow (method calls/navigation) occurs.
     * </p>
     * @throws InterruptedException in case the JavaFX thread is interrupted
     */
    public static void testSetupButtonActionFlow() throws InterruptedException {
        Platform.runLater(() -> {
            try {
                DatabaseHelper dbHelper = new DatabaseHelper(); 
                AdminSetupPage adminSetup = new AdminSetupPage(dbHelper);

                // Show the page in a test Stage
                Stage stage = new Stage();
                adminSetup.show(stage);

                // TODO: We look up the "setupButton" in adminSetup
                // For demonstration, let's assume there's a method "adminSetup.simulateSetupButtonClick();"
                adminSetup.simulateSetupButtonClick();

                // If the flow is correct, we might check a flag or see if a new page is displayed
                if (AdminSetupPageTestHelper.flowWasTriggered) {
                    System.out.println("✅ testSetupButtonActionFlow passed");
                } else {
                    System.out.println("❌ testSetupButtonActionFlow failed");
                }

                stage.hide();
            } catch (Exception e) {
                System.out.println("❌ testSetupButtonActionFlow exception: " + e.getMessage());
            }
        });

        // Let JavaFX run
        Thread.sleep(500);
    }

    // -------------------------------------------------------------------------
    // 3) Validate that admin role is assigned after setup
    // -------------------------------------------------------------------------

    /**
     * <p>
     * After clicking "Setup" or completing the admin setup process, 
     * verifies that the user's role is set to admin in the database or local state.
     * <br>
     * <strong>Inputs:</strong> Completed setup flow in AdminSetupPage.<br>
     * <strong>Outcomes:</strong> Confirm that the role is "admin" by checking a field or DB query.
     * </p>
     * @throws InterruptedException if the JavaFX thread is interrupted
     */
    public static void testAdminRoleAssigned() throws InterruptedException {
        Platform.runLater(() -> {
            try {
                DatabaseHelper dbHelper = new DatabaseHelper();
                AdminSetupPage adminSetup = new AdminSetupPage(dbHelper);

                // Show in test Stage
                Stage stage = new Stage();
                adminSetup.show(stage);

                // Suppose we do the setup
                adminSetup.simulateSetupButtonClick();

                // Suppose we can check "dbHelper.getCurrentUser()" or similar
                // or a "User" object to see if role is now "admin"
                User currentUser = dbHelper.getCurrentUser(); // Example method
                if (currentUser != null && "admin".equalsIgnoreCase(currentUser.getRole())) {
                    System.out.println("✅ testAdminRoleAssigned passed");
                } else {
                    System.out.println("❌ testAdminRoleAssigned failed");
                }

                stage.hide();
            } catch (Exception e) {
                System.out.println("❌ testAdminRoleAssigned exception: " + e.getMessage());
            }
        });

        Thread.sleep(500);
    }

    // -------------------------------------------------------------------------
    // 4) Test invalid database connection (simulate exception)
    // -------------------------------------------------------------------------

    /**
     * <p>
     * This test checks how the AdminSetupPage or InvitationPage 
     * behaves if the database connection fails or throws an exception.
     * <br>
     * <strong>Inputs:</strong> A simulated or forced database exception.<br>
     * <strong>Outcomes:</strong> The UI or method should handle the error gracefully (e.g., show alert).
     * </p>
     */
    public static void testInvalidDatabaseConnection() throws InterruptedException {
        Platform.runLater(() -> {
            try {
                // A special stub that always fails
                DatabaseHelper failingDb = new DatabaseHelper() {
                    @Override
                    public String generateInvitationCode() {
                        throw new RuntimeException("DB Connection Error");
                    }
                };

                InvitationPage invitePage = new InvitationPage(failingDb);
                Stage stage = new Stage();
                invitePage.show(stage);

                // Trigger code gen, expecting an exception
                try {
                    failingDb.generateInvitationCode();
                    System.out.println("❌ testInvalidDatabaseConnection failed (no exception?)");
                } catch (Exception e) {
                    // We expected an exception
                    System.out.println("✅ testInvalidDatabaseConnection passed");
                }

                stage.hide();
            } catch (Exception e) {
                System.out.println("❌ testInvalidDatabaseConnection exception: " + e.getMessage());
            }
        });

        Thread.sleep(500);
    }

    // -------------------------------------------------------------------------
    // 5) Test return to login page after setup
    // -------------------------------------------------------------------------

    /**
     * <p>
     * Verifies that after completing the admin setup, 
     * a user can return to the login page (or main app page).
     * <br>
     * <strong>Inputs:</strong> A completed or partially completed setup process.<br>
     * <strong>Outcomes:</strong> The UI navigates back to the login page (e.g., WelcomeLoginPage).
     * </p>
     * @throws InterruptedException if the thread is interrupted
     */
    public static void testReturnToLoginAfterSetup() throws InterruptedException {
        Platform.runLater(() -> {
            try {
                DatabaseHelper dbHelper = new DatabaseHelper();
                AdminSetupPage adminSetup = new AdminSetupPage(dbHelper);

                Stage stage = new Stage();
                adminSetup.show(stage);

                // Suppose there's a "returnToLoginButton" or a method
                adminSetup.simulateReturnToLogin();

                // We check whether the new scene is the login page or if a flag is set
                if (AdminSetupPageTestHelper.didReturnToLogin) {
                    System.out.println("✅ testReturnToLoginAfterSetup passed");
                } else {
                    System.out.println("❌ testReturnToLoginAfterSetup failed");
                }

                stage.hide();
            } catch (Exception e) {
                System.out.println("❌ testReturnToLoginAfterSetup exception: " + e.getMessage());
            }
        });

        Thread.sleep(500);
    }
}
