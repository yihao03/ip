package utilities;

/**
 * Interface for handling events in the application. Provides callbacks for
 * message display and user input events.
 */
public interface EventListener {
    /**
     * Called when a message needs to be displayed.
     *
     * @param message the message content to display
     * @param isUser true if the message is from the user, false if from the
     *            system
     */
    void onMessage(String message, boolean isUser);

    /**
     * Called when user input is received.
     *
     * @param input the user input string
     */
    void onInput(String input);
}
