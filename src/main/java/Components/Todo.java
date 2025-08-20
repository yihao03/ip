package Components;

import java.util.ArrayList;

public class Todo {
  private ArrayList<Task> tasks;

  public Todo() {
    this.tasks = new ArrayList<>();
  }

  /**
   * @param description The todo item's description
   * @return the number of tasks in the list
   */
  public Todo addTask(String description) {
    Task newTask = new Task(description);
    tasks.add(newTask);
    return this;
  }

  /**
   * @return the string of tasks in the list
   */
  public String getTasks() {
    int i = 1;
    StringBuilder sb = new StringBuilder();
    for (Task task : tasks) {
      sb.append(String.format("%d. %s\n", i, task.toString()));
      i++;
    }

    return sb.toString();
  }
}
