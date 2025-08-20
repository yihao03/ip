package Components.Task;

import java.io.Console;
import java.time.LocalDateTime;

import Utilities.DateTime;

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
   * @param console console passed from the main application to read line
   * @return the task created to be added to a todo list
   */
  public static Task createTask(Console console) {
    System.out.println("Please provide the task name");
    String description = console.readLine("> ");

    System.out.println("Please provide the event start time (e.g., 2023-10-31 18:00)");
    LocalDateTime startTime = DateTime.getDateTime(console);

    System.out.println("Please provide the event end time (e.g., 2023-10-31 18:00)");
    LocalDateTime endTime = DateTime.getDateTime(console);

    return new EventTask(description, startTime, endTime);
  }

  @Override
  public String toString() {
    return super.toString() + " (from: " + DateTime.formatDateTime(startTime) + " to: "
        + DateTime.formatDateTime(endTime) + ")";
  }
}
