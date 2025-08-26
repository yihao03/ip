package components.task;

import java.time.LocalDateTime;

import utilities.DateTime;
import utilities.IO;

public class EventTask extends Task {
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private EventTask(String description, LocalDateTime startTime, LocalDateTime endTime) {
        super(description);
        this.startTime = startTime;
        this.endTime = endTime;
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
    public String toString() {
        return super.toString() + " (from: " + DateTime.formatDateTime(startTime) + " to: "
                                        + DateTime.formatDateTime(endTime) + ")";
    }
}
