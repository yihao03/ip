package components.task;

import java.time.LocalDateTime;

import utilities.Data;
import utilities.DateTime;
import utilities.IO;

public class EventTask extends Task {
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public EventTask(String description, Boolean status, LocalDateTime startTime, LocalDateTime endTime) {
        super(description, status);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public EventTask(String description, LocalDateTime startTime, LocalDateTime endTime) {
        this(description, false, startTime, endTime);
    }

    /**
     * Takes over main application to create task
     *
     * @param in in passed from the main application to read line
     * @return the task created to be added to a todo list
     */
    public static Task createTask() {
        System.out.println("Please provide the task name");
        String description = IO.readLine();
        while (true) {
            System.out.println("Please provide the event start time (e.g., 2023-10-31 18:00)");
            LocalDateTime startTime = DateTime.getDateTime();

            System.out.println("Please provide the event end time (e.g., 2023-10-31 18:00)");
            LocalDateTime endTime = DateTime.getDateTime();

            if (startTime.isAfter(endTime)) {
                System.out.println("Start time must be before end time. Please try again.");
            } else {
                return new EventTask(description, startTime, endTime);
            }
        }
    }

    @Override
    public String encodeData() {
        return String.join(Data.DELIMITER, TaskType.EVENT.toString(), super.encodeBasic(),
                                        DateTime.formatDateTime(startTime), DateTime.formatDateTime(endTime));
    }

    public static Task decodeData(String[] data) throws IllegalArgumentException {
        if (data.length != 5) {
            throw new IllegalArgumentException();
        }
        return new EventTask(data[1], data[2].equals("1"), DateTime.parseDateTime(data[3]),
                                        DateTime.parseDateTime(data[4]));
    }

    @Override
    public String toString() {
        return super.toString() + " (from: " + DateTime.formatDateTime(startTime) + " to: "
                                        + DateTime.formatDateTime(endTime) + ")";
    }
}
