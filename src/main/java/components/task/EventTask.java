package components.task;

import java.time.LocalDateTime;

import utilities.Data;
import utilities.DateTime;
import utilities.IO;

/**
 * A Task that represents an event spanning a start and end date-time. Supports
 * interactive creation, persistence encoding/decoding, and logic for
 * determining if the event starts within the next week.
 */
public class EventTask extends Task {
    /** Inclusive start date-time of the event. */
    private LocalDateTime startTime;
    /** Exclusive/nominal end date-time of the event (must be after start). */
    private LocalDateTime endTime;

    /**
     * Full constructor allowing explicit completion status.
     *
     * @param description textual description of the event
     * @param status true if already marked done
     * @param startTime event start date-time
     * @param endTime event end date-time (must not be before startTime)
     */
    public EventTask(String description, Boolean status, LocalDateTime startTime, LocalDateTime endTime) {
        super(description, status);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Convenience constructor creating an undone EventTask.
     *
     * @param description textual description
     * @param startTime event start date-time
     * @param endTime event end date-time
     */
    public EventTask(String description, LocalDateTime startTime, LocalDateTime endTime) {
        this(description, false, startTime, endTime);
    }

    /**
     * Interactively creates an EventTask by prompting the user over standard
     * input. Ensures the start time precedes the end time (re-prompts
     * otherwise).
     *
     * @return newly created EventTask
     */
    public static Task createTask() {
        System.out.println("Please provide the task name");
        String description = IO.readLine();
        while (true) {
            System.out.printf("Please provide the event start time (e.g., %s) \n", DateTime.INPUT_DATE_FORMAT);
            LocalDateTime startTime = DateTime.getDateTime();

            System.out.printf("Please provide the event end time (e.g., %s) \n", DateTime.INPUT_DATE_FORMAT);
            LocalDateTime endTime = DateTime.getDateTime();

            if (startTime.isAfter(endTime)) {
                System.out.println("Start time must be before end time. Please try again.");
            } else {
                return new EventTask(description, startTime, endTime);
            }
        }
    }

    /**
     * Indicates if the event starts within the next week (now &lt;= start &lt;
     * now + 1 week) and the task is not yet marked done.
     *
     * @return true if the event is due soon
     */
    @Override
    public boolean isDueSoon() {
        LocalDateTime now = LocalDateTime.now();
        return !super.isDone() && !startTime.isBefore(now) // start &gt;= now
                                        && startTime.isBefore(now.plusWeeks(1));
    }

    /**
     * Encodes this event for persistence. Format:
     * EVENT|&lt;description&gt;|&lt;statusFlag&gt;|&lt;startDateTime&gt;|&lt;endDateTime&gt;
     *
     * @return encoded string
     */
    @Override
    public String encodeData() {
        return String.join(Data.DELIMITER, TaskType.EVENT.toString(), super.encodeBasic(),
                                        DateTime.formatDateTime(startTime), DateTime.formatDateTime(endTime));
    }

    /**
     * Reconstructs an EventTask from encoded data tokens.
     *
     * @param data token array (expected length 5)
     * @return decoded EventTask
     * @throws IllegalArgumentException if the data length is invalid
     */
    public static Task decodeData(String[] data) throws IllegalArgumentException {
        if (data.length != 5) {
            throw new IllegalArgumentException();
        }
        return new EventTask(data[1], data[2].equals("1"), DateTime.parseDateTime(data[3]),
                                        DateTime.parseDateTime(data[4]));
    }

    /**
     * Returns a human-readable representation including formatted start and end
     * times.
     *
     * @return display string
     */
    @Override
    public String toString() {
        return super.toString() + " (from: " + DateTime.printDateTime(startTime) + " to: "
                                        + DateTime.printDateTime(endTime) + ")";
    }
}
