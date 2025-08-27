import components.Todo;
import utilities.Data;

/**
 * Application bootstrap class containing the {@code main} entry point.
 * <p>
 * Responsibilities:
 * <ul>
 * <li>Load persisted task list from storage</li>
 * <li>Delegate to {@link Chat#start(Todo)} for interactive session</li>
 * <li>Persist any changes back to storage upon exit</li>
 * </ul>
 */
public class Dumpy {
    /** Visual separator line used to delimit console output sections. */
    public static final String LINE_SEPARATOR = "----------------------------------------------------------------------";
    /** ASCII art logo displayed on startup. */
    public static final String LOGO = """
                                    ██████╗ ██╗   ██╗███╗   ███╗██████╗ ██╗   ██╗
                                    ██╔══██╗██║   ██║████╗ ████║██╔══██╗╚██╗ ██╔╝
                                    ██║  ██║██║   ██║██╔████╔██║██████╔╝ ╚████╔╝
                                    ██║  ██║██║   ██║██║╚██╔╝██║██╔═══╝   ╚██╔╝
                                    ██████╔╝╚██████╔╝██║ ╚═╝ ██║██║        ██║
                                    ╚═════╝  ╚═════╝ ╚═╝     ╚═╝╚═╝        ╚═╝
                                    """;

    /**
     * Program entry point. Loads tasks, starts chat session, then saves tasks.
     *
     * @param args command line arguments (unused)
     */
    public static void main(String[] args) {
        // Initialization
        Todo todo = Data.readListFromFile();

        Chat.start(todo);

        Data.saveListToFile(todo);
    }
}
