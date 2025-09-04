package ui;

import components.Dumpy;
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

    private Image userImage = new Image(this.getClass()
                                    .getResourceAsStream("/images/DaUser.png"));
    private Image dumpyImage = new Image(this.getClass()
                                    .getResourceAsStream("/images/DaDuke.png"));

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
        addBubble(message, isUser);
    }

    @Override
    public void onInput(String input) {
        // Not used in this controller
    }

    /**
     * Initializes the MainWindow controller after FXML loading. Sets up the
     * scroll pane to automatically scroll to bottom when new content is added
     * and subscribes this controller to the EventBus for message notifications.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        EventBus.subscribe(this);
        addBubble("Hello! I'm Dumpy. How can I assist you today?", false);
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
            dialogContainer.getChildren().add(DialogBox.getUserDialog(message,
                                            userImage));
        } else {
            dialogContainer.getChildren().add(DialogBox.getDumpyDialog(message,
                                            dumpyImage));
        }
    }
}
