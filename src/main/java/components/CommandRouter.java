package components;

import java.time.LocalDateTime;

import components.task.DeadlineTask;
import components.task.EventTask;
import components.task.Task;
import exceptions.TaskNotFoundException;
import utilities.Data;
import utilities.DateTime;
import utilities.EventBus;
import utilities.EventListener;
import utilities.IO;

/**
 * Routes and processes user commands in the application. Implements
 * EventListener to handle user input and delegates command execution to
 * appropriate handler methods. Supports both direct command execution and
 * interactive task creation workflows.
 */
public class CommandRouter implements EventListener {
    private Todo todo;

    /**
     * Constructs a new CommandRouter with the specified Todo instance.
     * Automatically subscribes to the EventBus to receive input events.
     *
     * @param todo the Todo instance to manage tasks
     */
    public CommandRouter(Todo todo) {
        this.todo = todo;
        EventBus.subscribe(this);
    }

    /**
     * Handles user input by echoing it and processing the command. Ignores null
     * or empty input strings.
     *
     * @param input the user input string to process
     */
    public void onInput(String input) {
        if (input == null || input.trim().isEmpty()) {
            return;
        }

        // Process the command
        processCommand(input.trim());
    }

    /**
     * Processes a command by parsing it and delegating to the appropriate
     * handler. Automatically saves the todo list after command execution.
     *
     * @param input the trimmed command string to process
     */
    private void processCommand(String input) {
        String command = IO.extractCommand(input);
        String args = IO.extractArgs(input, command);

        switch (command.toLowerCase()) {
        case "mark":
            handleMarkCommand(args);
            break;
        case "unmark":
            handleUnmarkCommand(args);
            break;
        case "delete":
            handleDeleteCommand(args);
            break;
        case "find":
            handleFindCommand(args);
            break;
        case "todo":
            createTodoInteractive();
            break;
        case "deadline":
            createDeadlineInteractive();
            break;
        case "event":
            createEventInteractive();
            break;
        case "list":
            handleListCommand();
            break;
        case "help":
            handleHelpCommand();
            break;
        case "exit":
            handleExitCommand();
            break;
        default:
            EventBus.publish("Sorry I don't understand :(", false);
            EventBus.publish("Type 'help' to see available commands.", false);
        }

        // Save after each command
        Data.saveListToFile(todo);
    }

    /**
     * Handles the mark command to mark a task as done. Validates the task
     * number argument and provides appropriate feedback.
     *
     * @param args the argument string containing the task number
     */
    private void handleMarkCommand(String args) {
        Integer taskNumber = IO.parseIntArg(args);
        if (taskNumber == null) {
            EventBus.publish("Usage: mark <taskNumber>", false);
            return;
        }

        try {
            Task task = todo.toggleDone(taskNumber);
            EventBus.publish("Nice! I've marked this task as done:", false);
            EventBus.publish("  " + task.toString(), false);
        } catch (TaskNotFoundException e) {
            EventBus.publish(e.getMessage(), false);
        }
    }

    /**
     * Handles the unmark command to mark a task as not done. Validates the task
     * number argument and provides appropriate feedback.
     *
     * @param args the argument string containing the task number
     */
    private void handleUnmarkCommand(String args) {
        Integer taskNumber = IO.parseIntArg(args);
        if (taskNumber == null) {
            EventBus.publish("Usage: unmark <taskNumber>", false);
            return;
        }

        try {
            Task task = todo.toggleDone(taskNumber);
            EventBus.publish("OK, I've marked this task as not done yet:",
                                            false);
            EventBus.publish("  " + task.toString(), false);
        } catch (TaskNotFoundException e) {
            EventBus.publish(e.getMessage(), false);
        }
    }

    /**
     * Handles the delete command to remove a task from the list. Validates the
     * task number argument and provides appropriate feedback.
     *
     * @param args the argument string containing the task number
     */
    private void handleDeleteCommand(String args) {
        Integer taskNumber = IO.parseIntArg(args);
        if (taskNumber == null) {
            EventBus.publish("Usage: delete <taskNumber>", false);
            return;
        }

        try {
            String result = todo.deleteTask(taskNumber);
            EventBus.publish(result, false);
        } catch (TaskNotFoundException e) {
            EventBus.publish(e.getMessage(), false);
        }
    }

    /**
     * Handles the find command to search for tasks by description keywords.
     * Validates that search keywords are provided and performs the search.
     *
     * @param args the argument string containing search keywords
     */
    private void handleFindCommand(String args) {
        if (args.isBlank()) {
            EventBus.publish("Please provide text to find. Usage: find <keywords>",
                                            false);
            return;
        }

        try {
            String result = todo.findTasksByDescription(args.split("\\s+"));
            EventBus.publish(result, false);
        } catch (IllegalArgumentException e) {
            EventBus.publish(e.getMessage(), false);
        }
    }

    /**
     * Handles the list command to display all tasks.
     */
    private void handleListCommand() {
        String result = todo.listTasks();
        EventBus.publish(result, false);
    }

    /**
     * Handles the help command to display available commands and usage
     * information.
     */
    private void handleHelpCommand() {
        String help = """
                                        Here are the available commands:

                                        Task Management:
                                        • list - Show all tasks
                                        • todo - Add a todo task (interactive)
                                        • deadline - Add a deadline task (interactive)
                                        • event - Add an event task (interactive)
                                        • mark <number> - Mark task as done
                                        • unmark <number> - Mark task as not done
                                        • delete <number> - Delete a task
                                        • find <keywords> - Find tasks by description

                                        Other:
                                        • help - Show this help message
                                        • exit - Exit the application

                                        Date format: dd/MM/yyyy HHmm (e.g., 25/12/2024 1800)
                                        """;
        EventBus.publish(help, false);
    }

    /**
     * Handles the exit command to save tasks and display goodbye message.
     */
    private void handleExitCommand() {
        Data.saveListToFile(todo);
        EventBus.publish("Goodbye! Your tasks have been saved.", false);
        System.exit(0);
    }

    /**
     * Creates a new todo task through interactive prompts. Prompts the user for
     * a task description and creates the task upon valid input.
     */
    private void createTodoInteractive() {
        EventBus.publish("Please provide the task description:", false);

        EventBus.getInputAsync(description -> {
            if (description.trim().isEmpty()) {
                EventBus.publish("Task description cannot be empty.", false);
                return;
            }

            Task task = new Task(description.trim());
            String result = todo.addTask(task);
            EventBus.publish(result, false);
            Data.saveListToFile(todo);
        });
    }

    /**
     * Creates a new deadline task through interactive prompts. Prompts the user
     * for a task description and deadline, creating the task upon valid input.
     */
    private void createDeadlineInteractive() {
        EventBus.publish("Please provide the task description:", false);

        EventBus.getInputAsync(description -> {
            if (description.trim().isEmpty()) {
                EventBus.publish("Task description cannot be empty.", false);
                return;
            }

            EventBus.publish("Please provide the deadline ("
                                            + DateTime.INPUT_DATE_FORMAT + "):",
                                            false);

            EventBus.getInputAsync(dateInput -> {
                try {
                    LocalDateTime deadline = DateTime.parseDateTime(
                                                    dateInput.trim());
                    Task task = new DeadlineTask(description.trim(), deadline);
                    String result = todo.addTask(task);
                    EventBus.publish(result, false);
                    Data.saveListToFile(todo);
                } catch (Exception e) {
                    EventBus.publish("Invalid date format. Task creation cancelled.",
                                                    false);
                    EventBus.publish("Expected format: "
                                                    + DateTime.INPUT_DATE_FORMAT,
                                                    false);
                }
            });
        });
    }

    /**
     * Creates a new event task through interactive prompts. Prompts the user
     * for event description, start time, and end time, creating the task upon
     * valid input for all fields.
     */
    private void createEventInteractive() {
        EventBus.publish("Please provide the event description:", false);

        EventBus.getInputAsync(description -> {
            if (description.trim().isEmpty()) {
                EventBus.publish("Event description cannot be empty.", false);
                return;
            }

            EventBus.publish("Please provide the start time ("
                                            + DateTime.INPUT_DATE_FORMAT + "):",
                                            false);

            EventBus.getInputAsync(startInput -> {
                try {
                    LocalDateTime startTime = DateTime.parseDateTime(
                                                    startInput.trim());
                    EventBus.publish("Please provide the end time ("
                                                    + DateTime.INPUT_DATE_FORMAT
                                                    + "):", false);

                    EventBus.getInputAsync(endInput -> {
                        try {
                            LocalDateTime endTime = DateTime.parseDateTime(
                                                            endInput.trim());
                            Task task = new EventTask(description.trim(),
                                                            startTime, endTime);
                            String result = todo.addTask(task);
                            EventBus.publish(result, false);
                            Data.saveListToFile(todo);
                        } catch (Exception e) {
                            EventBus.publish("Invalid end time format. Event creation cancelled.",
                                                            false);
                            EventBus.publish("Expected format: "
                                                            + DateTime.INPUT_DATE_FORMAT,
                                                            false);
                        }
                    });
                } catch (Exception e) {
                    EventBus.publish("Invalid start time format. Event creation cancelled.",
                                                    false);
                    EventBus.publish("Expected format: "
                                                    + DateTime.INPUT_DATE_FORMAT,
                                                    false);
                }
            });
        });
    }

    /**
     * Handles message events from the EventBus. CommandRouter does not need to
     * process messages, only input events.
     *
     * @param message the message content (unused)
     * @param isUser whether the message is from a user (unused)
     */
    @Override
    public void onMessage(String message, boolean isUser) {
        // CommandRouter doesn't need to handle messages, only input
    }
}
