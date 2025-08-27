package components;

import java.util.ArrayList;
import java.util.function.Predicate;

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

    /**
     * Loads task to the todo list without printing anything intended for
     * loading from storage
     *
     * @param task task to be added to the list
     */
    public void loadTask(Task task) {
        tasks.add(task);
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
     * Finds tasks whose descriptions contain all given substrings
     * (case-insensitive). If no substrings are provided, returns all tasks.
     *
     * @param substrings substrings to look for
     * @return list of matching tasks
     */
    public String findTasksByDescription(String... substrings) throws IllegalArgumentException {
        if (substrings == null || substrings.length == 0) {
            throw new IllegalArgumentException("Please provide at least one substring to search for.");
        }

        return buildFilteredTasksString(tasks, task -> {
            String desc = task.getDescription().toLowerCase();
            for (String sub : substrings) {
                if (!desc.contains(sub.toLowerCase())) {
                    return false;
                }
            }
            return true;
        }, "Here are the matching tasks in your list:", "No matching tasks found.");
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
    public String listTasks() {
        return buildFilteredTasksString(tasks, t -> true, "Here are the tasks in your list:", "Nothing to do!");
    }

    /**
     * @return the string of tasks that are due soon
     */
    public String listDueSoonTasks() {
        return buildFilteredTasksString(tasks, Task::isDueSoon, "Here are the tasks that are due soon:",
                                        "You have no tasks that are due soon. Good job!");
    }

    private String buildFilteredTasksString(ArrayList<Task> taskList, Predicate<Task> pred, String header,
                                    String emptyMessage) {
        StringBuilder sb = new StringBuilder(header + "\n");
        int i = 1;
        for (Task task : taskList) {
            if (pred.test(task)) {
                sb.append(String.format("%d. %s\n", i, task.toString()));
                ++i;
            }
        }

        if (i == 1) {
            return emptyMessage + "\n";
        } else {
            return sb.toString();
        }
    }

    public ArrayList<Task> getTasks() {
        return this.tasks;
    }
}
