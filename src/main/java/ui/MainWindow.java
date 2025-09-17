package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import utilities.EventBus;
import utilities.EventListener;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane implements EventListener {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Image userImage = new Image(
                    this.getClass().getResourceAsStream("/images/DaUser.png"));
    private Image dumpyImage = new Image(
                    this.getClass().getResourceAsStream("/images/Dumpy.jpg"));

    /**
     * Handles incoming messages from the event bus and displays them in the
     * chat interface. This method is called when a message event is published
     * through the EventBus.
     *
     * @param message the message content to display
     * @param isUser true if the message is from the user, false if from Dumpy
     */
    @Override
    public void onMessage(String message, boolean isUser) {
        // Check if this is an error message (you can customize this logic)
        if (!isUser && (message.toLowerCase().contains("error")
                        || message.toLowerCase().contains("invalid")
                        || message.toLowerCase().contains("unknown"))) {
            addErrorBubble(message);
        } else {
            addBubble(message, isUser);
        }
    }

    @Override
    public void onInput(String input) {
        // Not used in this controller
    }

    /**
     * Initializes the MainWindow controller after FXML loading. Sets up the
     * scroll pane to automatically scroll to bottom when new content is added,
     * subscribes this controller to the EventBus for message notifications, and
     * applies CSS styling.
     */
    @FXML
    public void initialize() {
        // Apply style classes
        dialogContainer.getStyleClass().add("dialog-container");
        userInput.getStyleClass().add("user-input");
        sendButton.getStyleClass().add("send-button");

        // Style the input area
        AnchorPane inputArea = (AnchorPane) userInput.getParent();
        inputArea.getStyleClass().add("input-area");

        // Set up scroll behavior
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());

        // Set up input field behavior
        userInput.setPromptText("Type your message here...");

        // Subscribe to event bus
        EventBus.subscribe(this);

        // Add welcome message
        addWelcomeBubble("Hello! I'm Dumpy. How can I assist you today?",
                        dumpyImage);
    }

    /**
     * Handles user input from the text field when the send button is clicked or
     * Enter is pressed. Validates that the input is not empty, adds it to the
     * EventBus input queue for processing, and clears the input field for the
     * next message.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (!input.trim().isEmpty()) {
            addBubble(input, true);
            EventBus.addInput(input); // Add to blocking queue
            userInput.clear();
        }
    }

    /**
     * Creates and adds a dialog box to the chat interface. Generates either a
     * user dialog or Dumpy dialog based on the message source and appends it to
     * the dialog container for display.
     *
     * @param message the message content to display in the dialog box
     * @param isUser true to create a user dialog, false to create a Dumpy
     *            dialog
     */
    private void addBubble(String message, boolean isUser) {
        if (isUser) {
            dialogContainer.getChildren()
                            .add(DialogBox.getUserDialog(message, userImage));
        } else {
            dialogContainer.getChildren()
                            .add(DialogBox.getDumpyDialog(message, dumpyImage));
        }
    }

    /**
     * Adds an error dialog box with special styling to highlight errors.
     *
     * @param errorMessage the error message to display
     */
    private void addErrorBubble(String errorMessage) {
        dialogContainer.getChildren()
                        .add(DialogBox.getErrorDialog(errorMessage));
    }

    /**
     * Adds a welcome dialog box with special styling for the initial greeting.
     *
     * @param welcomeMessage the welcome message to display
     */
    private void addWelcomeBubble(String welcomeMessage, Image img) {
        dialogContainer.getChildren()
                        .add(DialogBox.getWelcomeDialog(welcomeMessage, img));
    }
}
