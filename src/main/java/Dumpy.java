import Components.Todo;
import Components.Task.DeadlineTask;
import Components.Task.EventTask;
import Components.Task.Task;
import Exceptions.TaskNotFoundException;
import Utilities.IO;

public class Dumpy {
  private static final String LINE_SEPARATOR = "-----------------------------------";
  private static final String LOGO = """
      ██████╗ ██╗   ██╗███╗   ███╗██████╗ ██╗   ██╗
      ██╔══██╗██║   ██║████╗ ████║██╔══██╗╚██╗ ██╔╝
      ██║  ██║██║   ██║██╔████╔██║██████╔╝ ╚████╔╝
      ██║  ██║██║   ██║██║╚██╔╝██║██╔═══╝   ╚██╔╝
      ██████╔╝╚██████╔╝██║ ╚═╝ ██║██║        ██║
      ╚═════╝  ╚═════╝ ╚═╝     ╚═╝╚═╝        ╚═╝
      """;

  public static void main(String[] args) {
    // Initialization
    Todo todo = new Todo();

    // Greeting
    System.out.printf("Hello, I am the smartest chatbot\n" +
        "%s\n" +
        "What can I do for you today?\n", Dumpy.LOGO);

    while (true) {
      System.out.println(Dumpy.LINE_SEPARATOR);
      String input = IO.readLine();
      System.out.println(Dumpy.LINE_SEPARATOR);

      if (input.startsWith("mark")) {
        int taskNumber = Integer.parseInt(
            input.replaceAll("[^0-9-]", ""));
        try {
          Task task = todo.toggleDone(taskNumber);
          System.out.println(task.toString());
        } catch (TaskNotFoundException e) {
          System.out.println(e.getMessage());
        }
      } else if (input.startsWith("delete")) {
        int taskNumber = Integer.parseInt(
            input.replaceAll("[^0-9-]", ""));
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
            System.out.print(todo.getTasks());
            break;
          case "exit":
            System.out.println("Goodbye!");
            return;
          default:
            System.out.println("Sorry I don't understand :(");
            break;
        }
      }
    }
  }
}
