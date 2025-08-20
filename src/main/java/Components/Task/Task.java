package Components.Task;

import Utilities.IO;

public class Task {
  private String description;
  private boolean isDone;

  protected Task(String description) {
    this.description = description;
    this.isDone = false;
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

  @Override
  public String toString() {
    String status = isDone ? "[✅] " : "[ ] ";
    return status + description;
  }
}
