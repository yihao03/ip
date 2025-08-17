import java.io.Console;

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
    Console console = System.console();

    if (console == null) {
      System.err.println("No console available. Please run this program in a terminal.");
      return;
    }

    // Greeting
    System.out.printf("Hello, I am the smartest chatbot\n" +
        "%s\n" +
        "What can I do for you today?\n", Dumpy.LOGO);

    System.out.println(Dumpy.LINE_SEPARATOR);

    System.out.println("Goodbye!");
    return;

    // while (true) {
    // System.out.println(Dumpy.LINE_SEPARATOR);
    // String input = console.readLine("> ");
    // System.out.println(Dumpy.LINE_SEPARATOR);
    //
    // switch (input) {
    // case "exit":
    // System.out.println("Goodbye!");
    // return;
    // default:
    // System.out.println("You said: " + input);
    // break;
    // }
    // }
  }
}
