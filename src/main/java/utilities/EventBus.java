package utilities;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

/**
 * A centralized event bus for managing communication between different
 * components of the application. Provides publish-subscribe messaging for
 * events and asynchronous/synchronous input handling mechanisms.
 *
 * <p>
 * This class supports:
 * <ul>
 * <li>Event listener subscription and notification</li>
 * <li>Asynchronous input handling with callbacks</li>
 * <li>Blocking and non-blocking input retrieval</li>
 * <li>Thread-safe operations for concurrent access</li>
 * </ul>
 *
 * <p>
 * All methods are static as this is designed as a singleton event bus.
 */
public class EventBus {
    /** List of registered event listeners that receive published messages */
    private static final List<EventListener> listeners = new ArrayList<>();

    /** Thread-safe queue for storing user input until it can be processed */
    private static final BlockingQueue<String> inputs = new LinkedBlockingQueue<>();

    /** Queue of callback functions waiting to receive input asynchronously */
    private static final Queue<Consumer<String>> inputCallbacks = new LinkedList<>();

    /** Synchronization lock for thread-safe access to input callbacks */
    private static final Object callbackLock = new Object();

    /**
     * Subscribes an event listener to receive notifications from the event bus.
     * The listener will be notified of all published messages.
     *
     * @param listener the event listener to subscribe
     */
    public static void subscribe(EventListener listener) {
        listeners.add(listener);
    }

    /**
     * Publishes a message to all subscribed event listeners. All registered
     * listeners will receive the message via their onMessage callback.
     *
     * @param message the message content to publish
     * @param isUser true if the message originates from a user, false if from
     *            the system
     */
    public static void publish(String message, boolean isUser) {
        for (EventListener listener : listeners) {
            listener.onMessage(message, isUser);
        }
    }

    /**
     * Unsubscribes an event listener from receiving notifications. The listener
     * will no longer receive published messages.
     *
     * @param listener the event listener to unsubscribe
     */
    public static void unsubscribe(EventListener listener) {
        listeners.remove(listener);
    }

    /**
     * Adds user input to the event bus. If there are callbacks waiting for
     * input, the input is immediately delivered to the first callback.
     * Otherwise, the input is queued for later retrieval.
     *
     * @param input the input string to add
     */
    public static void addInput(String input) {
        synchronized (callbackLock) {
            // Check if there are any callbacks waiting for input
            if (!inputCallbacks.isEmpty()) {
                Consumer<String> callback = inputCallbacks.poll();
                if (callback != null) {
                    callback.accept(input);
                    return;
                }
            }
        }

        for (EventListener listener : listeners) {
            listener.onInput(input);
        }
    }

    /**
     * Asynchronously retrieves user input without blocking the calling thread.
     * If input is already available in the queue, the callback is invoked
     * immediately. Otherwise, the callback is queued and will be invoked when
     * input becomes available.
     *
     * @param callback the consumer function to handle the input when available
     */
    public static void getInputAsync(Consumer<String> callback) {
        synchronized (callbackLock) {
            // Check if input is already available in the queue
            String existingInput = inputs.poll();
            if (existingInput != null) {
                callback.accept(existingInput);
                return;
            }

            // Queue the callback for when input arrives
            inputCallbacks.offer(callback);
        }
    }
}
