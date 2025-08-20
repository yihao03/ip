package Components.Task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import Utilities.DateTime;
import Utilities.IO;

public class DeadlineTask extends Task {
  private LocalDateTime deadline;

  private DeadlineTask(String description, LocalDateTime deadline) {
    super(description);
    this.deadline = deadline;
  }

  protected static LocalDateTime getDateTime() {
    String date;
    while (true) {
      date = IO.readLine();
      try {
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
      } catch (DateTimeParseException e) {
        System.out.printf("Invalid format. Use %s (e.g., %s)\n", DateTime.DATE_FORMAT, DateTime.EXAMPLE_DATE);
      }
    }
  }

  /**
   * takes over main application to create task
   * 
   * @return the task created to be added to a todo list
   */
  public static Task createTask() {
    System.out.println("Please provide the task name");
    String description = IO.readLine();

    System.out.printf("Please provide the deadline (%s)\n", DateTime.DATE_FORMAT);
    LocalDateTime deadline = DateTime.getDateTime();

    return new DeadlineTask(description, deadline);
  }

  @Override
  public String toString() {
    return super.toString() + " (by: " + DateTime.formatDateTime(deadline) + ")";
  }
}
