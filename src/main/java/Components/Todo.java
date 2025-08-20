package Components;

import java.util.ArrayList;

import Exceptions.TaskNotFoundException;

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
   * @param index The index of the item to be marked as done
   *              index is the same as the number displayed when list
   * @return the item marked as done
   */
  public Task toggleDone(int index) throws TaskNotFoundException {
    if (index <= 0 || index > tasks.size()) {
      throw new TaskNotFoundException(tasks.size(), index);
    }
    Task task = tasks.get(index - 1);
    task.toggleDone();
    return task;
  }

  /**
   * @return the string of tasks in the list
   */
  public String getTasks() {
    int i = 1;
    StringBuilder sb = new StringBuilder(
        "Here's what you have to do!\n");
    for (Task task : tasks) {
      sb.append(String.format("%d. %s\n", i, task.toString()));
      i++;
    }

    return sb.toString();
  }
}
