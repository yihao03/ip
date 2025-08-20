package Components.Task;

import java.io.Console;

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
   * @param console console passed from the main application to read line
   * @return the task created to be added to a todo list
   */
  public static Task createTask(Console console) {
    System.out.println("Please provide the task name");
    String description = console.readLine("> ");
    return new Task(description);
  }

  @Override
  public String toString() {
    String status = isDone ? "[✅] " : "[ ] ";
    return status + description;
  }
}
