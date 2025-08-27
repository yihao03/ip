package exceptions;

/**
 * Exception thrown when a requested task index is outside the bounds
 * of the current task list.
 */
public class TaskNotFoundException extends Exception {
    /**
     * Creates a generic TaskNotFoundException without detail message.
     */
    public TaskNotFoundException() {
        super();
    }

    /**
     * Creates a TaskNotFoundException with a detailed message indicating
     * the invalid index and the current size of the list.
     *
     * @param listSize current number of tasks
     * @param input    the (1-based or raw) index provided by the user
     */
    public TaskNotFoundException(int listSize, int input) {
        super(
                String.format(
                        "Given index %d exceeds todo list size %d!",
                        input,
                        listSize));
    }
}
