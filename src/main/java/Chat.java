import components.Todo;
import components.task.DeadlineTask;
import components.task.EventTask;
import components.task.Task;
import exceptions.TaskNotFoundException;
import utilities.IO;

public class Chat {
    public static int start(Todo todo) {
        // Greeting
        System.out.printf("Hello, I am the smartest chatbot\n" + "%s\n" + "What can I do for you today?\n", Dumpy.LOGO);

        System.out.print(todo.listDueSoonTasks());

        while (true) {
            System.out.println(Dumpy.LINE_SEPARATOR);
            String input = IO.readLine();
            System.out.println(Dumpy.LINE_SEPARATOR);

            String command = IO.extractCommand(input.trim());
            String args = IO.extractArgs(input, command);

            switch (command) {
            case "mark": {
                Integer taskNumber = IO.parseIntArg(args);
                if (taskNumber == null) {
                    System.out.println("Usage: mark <taskNumber>");
                    break;
                }
                try {
                    Task task = todo.toggleDone(taskNumber);
                    System.out.println(task.toString());
                } catch (TaskNotFoundException e) {
                    System.out.println(e.getMessage());
                }
                break;
            }
            case "delete": {
                Integer taskNumber = IO.parseIntArg(args);
                if (taskNumber == null) {
                    System.out.println("Usage: delete <taskNumber>");
                    break;
                }
                try {
                    todo.deleteTask(taskNumber);
                } catch (TaskNotFoundException e) {
                    System.out.println(e.getMessage());
                }
                break;
            }
            case "find": {
                if (args.isBlank()) {
                    System.out.println("Please provide text to find. Usage: find <keywords>");
                    break;
                }
                try {
                    String outcome = todo.findTasksByDescription(args.split("\\s+"));
                    System.out.println(outcome);
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
                break;
            }
            case "todo": {
                Task task = Task.createTask();
                todo.addTask(task);
                break;
            }
            case "deadline": {
                Task task = DeadlineTask.createTask();
                todo.addTask(task);
                break;
            }
            case "event": {
                Task task = EventTask.createTask();
                todo.addTask(task);
                break;
            }
            case "list":
                System.out.print(todo.listTasks());
                break;
            case "exit":
                System.out.println("Goodbye!");
                return 0;
            default:
                System.out.println("Sorry I don't understand :(");
                break;
            }
        }
    }
}
