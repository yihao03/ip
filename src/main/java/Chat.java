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

        while (true) {
            System.out.println(Dumpy.LINE_SEPARATOR);
            String input = IO.readLine();
            System.out.println(Dumpy.LINE_SEPARATOR);

            if (input.startsWith("mark")) {
                int taskNumber = Integer.parseInt(input.replaceAll("[^0-9-]", ""));
                try {
                    Task task = todo.toggleDone(taskNumber);
                    System.out.println(task.toString());
                } catch (TaskNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            } else if (input.startsWith("delete")) {
                int taskNumber = Integer.parseInt(input.replaceAll("[^0-9-]", ""));
                try {
                    todo.deleteTask(taskNumber);
                } catch (TaskNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                switch (input) {
                case "todo":
                    Task task = Task.createTask();
                    todo.addTask(task);
                    break;
                case "deadline":
                    task = DeadlineTask.createTask();
                    todo.addTask(task);
                    break;
                case "event":
                    task = EventTask.createTask();
                    todo.addTask(task);
                    break;
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
}
