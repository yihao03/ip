package components;

import java.util.ArrayList;
import java.util.function.Predicate;

import components.task.Task;
import exceptions.TaskNotFoundException;
import utilities.IO;

/**
 * Represents an in-memory task list. Provides operations to add/load tasks,
 * delete with confirmation, search by description substrings, toggle completion
 * status, list all tasks, list tasks due soon, and access the underlying list.
 */
public class Todo {
    private ArrayList<Task> tasks;

    public Todo() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Adds task to the todo list and prints the total number of tasks
     *
     * @param task task to be added to the list
     */
    public String addTask(Task task) {
        tasks.add(task);
        return "Task added! You now have " + tasks.size()
                        + " tasks in your list.";
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

    /**
     * Deletes the task at the given 1-based index after a confirmation prompt.
     *
     * @param index 1-based index of the task to delete
     * @throws TaskNotFoundException if the index is out of bounds
     */
    public String deleteTask(int index) throws TaskNotFoundException {
        if (index <= 0 || index > tasks.size()) {
            throw new TaskNotFoundException(tasks.size(), index);
        }
        Task task = tasks.get(index - 1);

        // Confirmation because Prof Ben taught me the 4 SWE Principles
        String confirmation = IO.readLine(
                        "Are you sure you want to remove this task? (y/N) \n"
                                        + task.getDescription());
        System.out.println(confirmation);
        if (confirmation.trim().equals("y")) {
            tasks.remove(index - 1);
            return "Task deleted! You now have " + tasks.size()
                            + " tasks in your list.";
        } else {
            return "Task not deleted.";
        }
    }

    /**
     * Use {@link #fuzzyFindTasks(String)} instead for better search Finds tasks
     * whose descriptions contain all given substrings (case-insensitive). If no
     * substrings are provided, returns all tasks.
     *
     * @param substrings substrings to look for
     * @return list of matching tasks
     * @deprecated
     */
    public String findTasksByDescription(String... substrings)
                    throws IllegalArgumentException {
        if (substrings == null || substrings.length == 0) {
            throw new IllegalArgumentException(
                            "Please provide at least one substring to search for.");
        }

        return buildFilteredTasksString(tasks, task -> {
            String desc = task.getDescription().toLowerCase();

            for (String sub : substrings) {
                if (!desc.contains(sub.toLowerCase())) {
                    return false;
                }
            }

            return true;
        }, "Here are the matching tasks in your list:",
                        "No matching tasks found.");
    }

    /**
     * Finds tasks using fuzzy matching where characters from the search term
     * appear in order in the task description (case-insensitive).
     *
     * @param searchTerm the term to fuzzy search for
     * @return list of matching tasks
     * @throws IllegalArgumentException if search term is null or empty
     */
    public String fuzzyFindTasks(String searchTerm)
                    throws IllegalArgumentException {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            throw new IllegalArgumentException("Please provide a search term.");
        }

        String cleanSearchTerm = searchTerm.trim().toLowerCase();

        return buildFilteredTasksString(tasks, task -> {
            return fuzzyMatch(task.getDescription().toLowerCase(),
                            cleanSearchTerm);
        }, "Here are the matching tasks in your list:",
                        "No matching tasks found.");
    }

    /**
     * Performs simple fuzzy matching by checking if characters from the search
     * term appear in order in the target string.
     *
     * @param target the string to search in
     * @param search the string to search for
     * @return true if all characters in search appear in order in target
     */
    private boolean fuzzyMatch(String target, String search) {
        int targetIndex = 0;
        int searchIndex = 0;

        while (targetIndex < target.length() && searchIndex < search.length()) {
            if (target.charAt(targetIndex) == search.charAt(searchIndex)) {
                searchIndex++;
            }
            targetIndex++;
        }

        return searchIndex == search.length();
    }

    /**
     * Toggles the completion status of the task at the given 1-based index.
     *
     * @param index 1-based index of the task to toggle
     * @return the task after its done state has been toggled
     * @throws TaskNotFoundException if the index is out of bounds
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
     * Lists all tasks currently stored.
     *
     * @return formatted string of all tasks, or a message if none exist
     */
    public String listTasks() {
        return buildFilteredTasksString(tasks, t -> true,
                        "Here are the tasks in your list:", "Nothing to do!");
    }

    /**
     * Lists tasks that are considered due soon (delegates logic to
     * Task::isDueSoon).
     *
     * @return formatted string of due-soon tasks, or a congratulatory message
     *             if none
     */
    public String listDueSoonTasks() {
        return buildFilteredTasksString(tasks, Task::isDueSoon,
                        "Here are the tasks that are due soon:",
                        "You have no tasks that are due soon. Good job!");
    }

    /**
     * Builds a formatted string listing tasks that satisfy the given predicate.
     *
     * @param taskList list of tasks to inspect
     * @param pred predicate determining inclusion
     * @param header header line to print when there is at least one match
     * @param emptyMessage message returned if no tasks match
     * @return formatted string of matching tasks (1-based numbering) or the
     *             empty message
     */
    private String buildFilteredTasksString(ArrayList<Task> taskList,
                    Predicate<Task> pred, String header, String emptyMessage) {
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

    /**
     * Returns the underlying mutable task list.
     *
     * @return internal ArrayList of tasks
     */
    public ArrayList<Task> getTasks() {
        return this.tasks;
    }
}
