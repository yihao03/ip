package components.task;

import java.time.LocalDateTime;

import utilities.Data;
import utilities.DateTime;
import utilities.IO;

/**
 * A Task that has an associated deadline (date and time). Provides support for
 * interactive creation, persistence encoding/decoding, and logic to determine
 * whether it is due soon (within one week).
 */
public class DeadlineTask extends Task {
    private LocalDateTime deadline;

    /**
     * Constructs a new DeadlineTask with an undone status by default.
     *
     * @param description textual description of the task
     * @param deadline date-time by which the task is due
     */
    private DeadlineTask(String description, LocalDateTime deadline) {
        super(description);
        this.deadline = deadline;
    }

    /**
     * Constructs a new DeadlineTask with an explicit completion status.
     *
     * @param description textual description of the task
     * @param status true if the task is already completed
     * @param deadline date-time by which the task is due
     */
    private DeadlineTask(String description, Boolean status, LocalDateTime deadline) {
        super(description, status);
        this.deadline = deadline;
    }

    /**
     * Takes over standard input to interactively create a DeadlineTask. Prompts
     * the user for a description and a deadline in the accepted format.
     *
     * @return the newly created DeadlineTask
     */
    public static Task createTask() {
        System.out.println("Please provide the task name");
        String description = IO.readLine();

        System.out.printf("Please provide the deadline (%s)\n", DateTime.INPUT_DATE_FORMAT);
        LocalDateTime deadline = DateTime.getDateTime();

        return new DeadlineTask(description, deadline);
    }

    /**
     * Decodes a previously encoded DeadlineTask. Expected format: [TaskType,
     * description, statusFlag, deadlineString] where statusFlag is "1" for
     * done, "0" for not done.
     *
     * @param data tokenized data fields
     * @return reconstructed DeadlineTask
     * @throws IllegalArgumentException if the data length is invalid
     */
    public static Task decodeData(String[] data) throws IllegalArgumentException {
        if (data.length != 4) {
            throw new IllegalArgumentException();
        }
        return new DeadlineTask(data[1], data[2].equals("1"), DateTime.parseDateTime(data[3]));
    }

    /**
     * Encodes this DeadlineTask into a delimited string suitable for
     * persistence. Format: DEADLINE|<description>|<statusFlag>|<deadline>
     *
     * @return encoded string representation
     */
    @Override
    public String encodeData() {
        return String.join(Data.DELIMITER, TaskType.DEADLINE.toString(), super.encodeBasic(),
                                        DateTime.formatDateTime(this.deadline));
    }

    /**
     * Indicates if the deadline is due within a week from now (inclusive of
     * now, exclusive of +1 week), and only if the task is not already
     * completed.
     *
     * @return true if due within one week and not done
     */
    @Override
    public boolean isDueSoon() {
        LocalDateTime now = LocalDateTime.now();
        System.out.println(super.isDone());
        return !super.isDone() && !deadline.isBefore(now) // deadline >= now
                                        && deadline.isBefore(now.plusWeeks(1));
    }

    /**
     * Returns a human-friendly string including the formatted deadline.
     *
     * @return display string
     */
    @Override
    public String toString() {
        return super.toString() + " (by: " + DateTime.printDateTime(deadline) + ")";
    }
}
