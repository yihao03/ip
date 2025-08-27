package components.task;

import java.time.LocalDateTime;

import utilities.Data;
import utilities.DateTime;
import utilities.IO;

public class DeadlineTask extends Task {
    private LocalDateTime deadline;

    public DeadlineTask(String description, LocalDateTime deadline) {
        super(description);
        this.deadline = deadline;
    }

    public DeadlineTask(String description, Boolean status, LocalDateTime deadline) {
        super(description, status);
        this.deadline = deadline;
    }

    /**
     * takes over main application to create task
     * 
     * @return the task created to be added to a todo list
     */
    public static Task createTask() {
        System.out.println("Please provide the task name");
        String description = IO.readLine();

        System.out.printf("Please provide the deadline (%s)\n", DateTime.INPUT_DATE_FORMAT);
        LocalDateTime deadline = DateTime.getDateTime();

        return new DeadlineTask(description, deadline);
    }

    public static Task decodeData(String[] data) throws IllegalArgumentException {
        if (data.length != 4) {
            throw new IllegalArgumentException();
        }
        return new DeadlineTask(data[1], data[2].equals("1"), DateTime.parseDateTime(data[3]));
    }

    @Override
    public String encodeData() {
        return String.join(Data.DELIMITER, TaskType.DEADLINE.toString(), super.encodeBasic(),
                                        DateTime.formatDateTime(this.deadline));
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + DateTime.formatDateTime(deadline) + ")";
    }
}
