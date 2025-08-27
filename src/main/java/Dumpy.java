import components.Todo;
import utilities.Data;

public class Dumpy {
    public static final String LINE_SEPARATOR = "----------------------------------------------------------------------";
    public static final String LOGO = """
                                    ██████╗ ██╗   ██╗███╗   ███╗██████╗ ██╗   ██╗
                                    ██╔══██╗██║   ██║████╗ ████║██╔══██╗╚██╗ ██╔╝
                                    ██║  ██║██║   ██║██╔████╔██║██████╔╝ ╚████╔╝
                                    ██║  ██║██║   ██║██║╚██╔╝██║██╔═══╝   ╚██╔╝
                                    ██████╔╝╚██████╔╝██║ ╚═╝ ██║██║        ██║
                                    ╚═════╝  ╚═════╝ ╚═╝     ╚═╝╚═╝        ╚═╝
                                    """;

    public static void main(String[] args) {
        // Initialization
        Todo todo = Data.readListFromFile();

        Chat.start(todo);

        Data.saveListToFile(todo);
    }
}
