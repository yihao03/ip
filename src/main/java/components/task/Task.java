package components.task;

import utilities.Data;
import utilities.IO;

public class Task {
    protected String description;
    protected boolean isDone;

    protected Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    protected Task(String description, boolean isDone) {
        this.description = description;
        this.isDone = isDone;
    }

    public boolean toggleDone() {
        this.isDone = !this.isDone;
        return this.isDone;
    }

    public String getDescription() {
        return description;
    }

    /**
     * takes over main application to create task
     *
     * @return the task created to be added to a todo list
     */
    public static Task createTask() {
        System.out.println("Please provide the task name");
        String description = IO.readLine();
        return new Task(description);
    }

    public static Task createCorruptTask() {
        return new Task("Corrupt Task");
    }

    public static Task decodeData(String[] data) throws IllegalArgumentException {
        if (data.length != 3) {
            throw new IllegalArgumentException();
        }
        return new Task(data[1], data[2].equals("1"));
    }

    public String encodeData() {
        String status = this.isDone ? "1" : "0";
        return String.join(Data.DELIMITER, TaskType.TODO.toString(), this.description, status);
    }

    @Override
    public String toString() {
        return "Task []";
    }
}
