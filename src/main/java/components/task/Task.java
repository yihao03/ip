package components.task;

import utilities.Data;
import utilities.IO;

/**
 * Base representation of a task with a textual description and completion
 * status. Acts as the parent for specialized task types (e.g., deadlines,
 * events). Provides: - Toggling of completion state - Basic encoding/decoding
 * for persistence (description + done flag) - Interactive creation of a plain
 * task via console - A default "not due soon" implementation (overridden by
 * subclasses that track time)
 */
public class Task {
    /** Human-readable description of the task. */
    private String description;
    /** Completion flag: true if the task has been marked done. */
    private boolean isDone;

    /**
     * Constructs a new (undone) Task with the given description.
     *
     * @param description textual description
     */
    protected Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Constructs a Task with explicit completion status (used mainly by
     * decoding).
     *
     * @param description textual description
     * @param isDone initial completion flag
     */
    protected Task(String description, boolean isDone) {
        this.description = description;
        this.isDone = isDone;
    }

    /**
     * Toggles the current completion status.
     *
     * @return new status after toggling (true if now done)
     */
    public boolean toggleDone() {
        this.isDone = !this.isDone;
        return this.isDone;
    }

    /**
     * Returns the textual description.
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Indicates whether the task is completed.
     *
     * @return true if done
     */
    public boolean isDone() {
        return this.isDone;
    }

    /**
     * Interactively creates a basic Task by prompting the user for a
     * description.
     *
     * @return new Task instance
     */
    public static Task createTask() {
        System.out.println("Please provide the task name");
        String description = IO.readLine();
        return new Task(description);
    }

    /**
     * Creates a placeholder task for corrupt data scenarios.
     *
     * @return placeholder Task
     */
    public static Task createCorruptTask() {
        return new Task("Corrupt Task");
    }

    /**
     * Decodes a plain Task from tokenized data. Expected format length: 3 ->
     * [TaskType, description, statusFlag] statusFlag: "1" = done, "0" = not
     * done.
     *
     * @param data token array
     * @return decoded Task
     * @throws IllegalArgumentException if token length is invalid
     */
    public static Task decodeData(String[] data) throws IllegalArgumentException {
        if (data.length != 3) {
            throw new IllegalArgumentException();
        }
        return new Task(data[1], data[2].equals("1"));
    }

    /**
     * Encodes this Task including its type tag for persistence. Format:
     * TODO|&lt;description&gt;|&lt;statusFlag&gt;
     *
     * @return encoded string
     */
    public String encodeData() {
        return String.join(Data.DELIMITER, TaskType.TODO.toString(), this.encodeBasic());
    }

    /**
     * Encodes only the description and status flag. Used by subclasses when
     * prepending their own type and additional fields. Format: Format:
     * &lt;description&gt;|&lt;statusFlag&gt; where statusFlag is 1 (done) or 0
     * (not
     *
     * @return encoded core fields
     */
    public String encodeBasic() {
        String status = this.isDone ? "1" : "0";
        return String.join(Data.DELIMITER, this.description, status);
    }

    /**
     * Indicates if the task is due within a week. Base implementation returns
     * false; subclasses with temporal fields override.
     *
     * @return false (no deadline associated)
     */
    public boolean isDueSoon() {
        return false;
    }

    /**
     * Returns a human-readable representation with status and description.
     *
     * @return display string
     */
    @Override
    public String toString() {
        String statusIcon = (isDone ? "[✅] " : "[  ] ");
        return String.format("%s %s", statusIcon, description);
    }
}
