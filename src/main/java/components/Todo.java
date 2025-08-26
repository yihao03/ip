package components;

import java.util.ArrayList;

import components.task.Task;
import exceptions.TaskNotFoundException;
import utilities.IO;

public class Todo {
    private ArrayList<Task> tasks;

    public Todo() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Adds task to the todo list and prints the total number of tasks
     * 
     * @param task task to be added to the list
     * @return the total number of tasks in the list after adding the new task
     */
    public void addTask(Task task) {
        tasks.add(task);
        System.out.println("Task added! You now have " + tasks.size() + " tasks in your list.");
    }

    public void deleteTask(int index) throws TaskNotFoundException {
        if (index <= 0 || index > tasks.size()) {
            throw new TaskNotFoundException(tasks.size(), index);
        }
        Task task = tasks.get(index - 1);

        // Confirmation because Prof Ben taught me the 4 SWE Principles
        String confirmation = IO.readLine(
                                        "Are you sure you want to remove this task? (y/N) \n" + task.getDescription());
        System.out.println(confirmation);
        if (confirmation.trim().equals("y")) {
            tasks.remove(index - 1);
            System.out.println("Task deleted! You now have " + tasks.size() + " tasks in your list.");
        } else {
            System.out.println("Task not deleted.");
        }
    }

    /**
     * @param index The index of the item to be marked as done index is the same
     * as the number displayed when list
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
        StringBuilder sb = new StringBuilder("Here's what you have to do!\n");
        for (Task task : tasks) {
            sb.append(String.format("%d. %s\n", i, task.toString()));
            i++;
        }

        return sb.toString();
    }
}
