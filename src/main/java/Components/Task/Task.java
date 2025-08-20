package Components.Task;

import java.io.BufferedReader;
import java.io.IOException;

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

  /**
   * takes over main application to create task
   *
   * @return the task created to be added to a todo list
   */
  public static Task createTask() {
    System.out.printf("Please provide the task name\n> ");
    String description = IO.readLine();
    return new Task(description);
  }

  @Override
  public String toString() {
    String status = isDone ? "[✅] " : "[ ] ";
    return status + description;
  }
}
