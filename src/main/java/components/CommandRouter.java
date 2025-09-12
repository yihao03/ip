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
    // Command constants
    private static final String COMMAND_MARK = "mark";
    private static final String COMMAND_DELETE = "delete";
    private static final String COMMAND_FIND = "find";
    private static final String COMMAND_TODO = "todo";
    private static final String COMMAND_DEADLINE = "deadline";
    private static final String COMMAND_EVENT = "event";
    private static final String COMMAND_LIST = "list";
    private static final String COMMAND_HELP = "help";
    private static final String COMMAND_EXIT = "exit";

    // Usage messages
    private static final String USAGE_MARK = "Usage: mark <taskNumber>";
    private static final String USAGE_UNMARK = "Usage: unmark <taskNumber>";
    private static final String USAGE_DELETE = "Usage: delete <taskNumber>";
    private static final String USAGE_FIND = "Please provide text to find. Usage: find <keywords>";

    // User messages
    private static final String MESSAGE_TASK_MARKED = "Nice! I've marked this task as done:";
    private static final String MESSAGE_TASK_UNMARKED = "OK, I've marked this task as not done yet:";
    private static final String MESSAGE_UNKNOWN_COMMAND = "Sorry I don't understand :(";
    private static final String MESSAGE_HELP_HINT = "Type 'help' to see available commands.";
    private static final String MESSAGE_GOODBYE = "Goodbye! Your tasks have been saved.";
    private static final String MESSAGE_EMPTY_DESCRIPTION = "Task description cannot be empty.";
    private static final String MESSAGE_EMPTY_EVENT_DESCRIPTION = "Event description cannot be empty.";
    private static final String MESSAGE_INVALID_DATE = "Invalid date format. Task creation cancelled.";
    private static final String MESSAGE_INVALID_START_TIME = "Invalid start time format. Event creation cancelled.";
    private static final String MESSAGE_INVALID_END_TIME = "Invalid end time format. Event creation cancelled.";
    private static final String MESSAGE_EXPECTED_FORMAT = "Expected format: ";

    // Prompts
    private static final String PROMPT_TASK_DESCRIPTION = "Please provide the task description:";
    private static final String PROMPT_EVENT_DESCRIPTION = "Please provide the event description:";
    private static final String PROMPT_DEADLINE = "Please provide the deadline (";
    private static final String PROMPT_START_TIME = "Please provide the start time (";
    private static final String PROMPT_END_TIME = "Please provide the end time (";

    private Todo todo;

    /**
     * Constructs a new CommandRouter with the specified Todo instance.
     * Automatically subscribes to the EventBus to receive input events.
     *
     * @param todo the Todo instance to manage tasks
     */
    public CommandRouter(Todo todo) {
        assert todo != null : "Todo instance cannot be null";
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
        assert input != null : "Input string cannot be null";
        assert !input.trim().isEmpty() : "Input should not be empty after trimming";
        String command = IO.extractCommand(input);
        String args = IO.extractArgs(input, command);
        switch (command.toLowerCase()) {
        case COMMAND_MARK:
            handleMarkCommand(args);
            break;
        case COMMAND_DELETE:
        case "delete":
            handleDeleteCommand(args);
            break;
        case COMMAND_FIND:
            handleFindCommand(args);
            break;
        case COMMAND_TODO:
            createTodoInteractive();
            break;
        case COMMAND_DEADLINE:
            createDeadlineInteractive();
            break;
        case COMMAND_EVENT:
            createEventInteractive();
            break;
        case COMMAND_LIST:
            handleListCommand();
            break;
        case COMMAND_HELP:
            handleHelpCommand();
            break;
        case COMMAND_EXIT:
            handleExitCommand();
            break;
        default:
            handleUnknownCommand();
        }

        // Save after each command
        Data.saveListToFile(todo);
    }

    /**
     * Handles unknown commands by providing appropriate error messages.
     */
    private void handleUnknownCommand() {
        EventBus.publish(MESSAGE_UNKNOWN_COMMAND, false);
        EventBus.publish(MESSAGE_HELP_HINT, false);
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
            EventBus.publish(USAGE_MARK, false);
            return;
        }

        try {
            assert taskNumber != null : "Task number should be validated before calling toggleDone";
            Task task = todo.toggleDone(taskNumber);
            String result = task.isDone() ? "as done" : "as not done";
            EventBus.publish(String.format("Nice! I've marked this task %s:",
                                            result), false);
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
            EventBus.publish(USAGE_DELETE, false);
            return;
        }

        try {
            assert taskNumber != null : "Task number should be validated before calling deleteTask";
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
            EventBus.publish(USAGE_FIND, false);
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
        EventBus.publish(MESSAGE_GOODBYE, false);
        System.exit(0);
    }

    /**
     * Creates a new todo task through interactive prompts. Prompts the user for
     * a task description and creates the task upon valid input.
     */
    private void createTodoInteractive() {
        EventBus.publish(PROMPT_TASK_DESCRIPTION, false);

        EventBus.getInputAsync(description -> {
            if (isEmptyDescription(description)) {
                return;
            }

            Task task = new Task(description.trim());
            addTaskAndSave(task);
        });
    }

    /**
     * Creates a new deadline task through interactive prompts. Prompts the user
     * for a task description and deadline, creating the task upon valid input.
     */
    private void createDeadlineInteractive() {
        EventBus.publish(PROMPT_TASK_DESCRIPTION, false);

        EventBus.getInputAsync(description -> {
            if (isEmptyDescription(description)) {
                return;
            }

            EventBus.publish(
                            PROMPT_DEADLINE + DateTime.INPUT_DATE_FORMAT + "):",
                            false);

            EventBus.getInputAsync(dateInput -> {
                try {
                    LocalDateTime deadline = DateTime
                                    .parseDateTime(dateInput.trim());
                    Task task = new DeadlineTask(description.trim(), deadline);
                    addTaskAndSave(task);
                } catch (Exception e) {
                    publishDateError(MESSAGE_INVALID_DATE);
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
        EventBus.publish(PROMPT_EVENT_DESCRIPTION, false);

        EventBus.getInputAsync(description -> {
            if (isEmptyEventDescription(description)) {
                return;
            }

            EventBus.publish(PROMPT_START_TIME + DateTime.INPUT_DATE_FORMAT
                            + "):", false);

            EventBus.getInputAsync(startInput -> {
                try {
                    LocalDateTime startTime = DateTime
                                    .parseDateTime(startInput.trim());
                    EventBus.publish(PROMPT_END_TIME
                                    + DateTime.INPUT_DATE_FORMAT + "):", false);

                    EventBus.getInputAsync(endInput -> {
                        try {
                            LocalDateTime endTime = DateTime
                                            .parseDateTime(endInput.trim());
                            Task task = new EventTask(description.trim(),
                                            startTime, endTime);
                            addTaskAndSave(task);
                        } catch (Exception e) {
                            publishDateError(MESSAGE_INVALID_END_TIME);
                        }
                    });
                } catch (Exception e) {
                    publishDateError(MESSAGE_INVALID_START_TIME);
                }
            });
        });
    }

    /**
     * Checks if the provided description is empty and publishes error message
     * if so.
     *
     * @param description the description to validate
     * @return true if description is empty, false otherwise
     */
    private boolean isEmptyDescription(String description) {
        if (description.trim().isEmpty()) {
            EventBus.publish(MESSAGE_EMPTY_DESCRIPTION, false);
            return true;
        }
        return false;
    }

    /**
     * Checks if the provided event description is empty and publishes error
     * message if so.
     *
     * @param description the description to validate
     * @return true if description is empty, false otherwise
     */
    private boolean isEmptyEventDescription(String description) {
        if (description.trim().isEmpty()) {
            EventBus.publish(MESSAGE_EMPTY_EVENT_DESCRIPTION, false);
            return true;
        }
        return false;
    }

    /**
     * Adds a task to the todo list and saves the data.
     *
     * @param task the task to add
     */
    private void addTaskAndSave(Task task) {
        String result = todo.addTask(task);
        EventBus.publish(result, false);
        Data.saveListToFile(todo);
    }

    /**
     * Publishes date format error messages.
     *
     * @param errorMessage the specific error message to publish
     */
    private void publishDateError(String errorMessage) {
        EventBus.publish(errorMessage, false);
        EventBus.publish(MESSAGE_EXPECTED_FORMAT + DateTime.INPUT_DATE_FORMAT,
                        false);
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
