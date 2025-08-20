import java.io.Console;

import Components.Todo;

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
    Console console = System.console();
    Todo todo = new Todo();

    if (console == null) {
      System.err.println("No console available. Please run this program in a terminal.");
      return;
    }

    // Greeting
    System.out.printf("Hello, I am the smartest chatbot\n" +
        "%s\n" +
        "What can I do for you today?\n", Dumpy.LOGO);

    while (true) {
      System.out.println(Dumpy.LINE_SEPARATOR);
      String input = console.readLine("> ");
      System.out.println(Dumpy.LINE_SEPARATOR);

      switch (input) {
        case "list":
          System.out.print(todo.getTasks());
          break;
        case "exit":
          System.out.println("Goodbye!");
          return;
        default:
          todo.addTask(input);
          System.out.println("added: " + input);
          break;
      }
    }
  }
}
