package components;

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
    public static final String LINE_SEPARATOR = "---------------------------------------------------------------------";
    /** ASCII art logo displayed on startup. */
    public static final String LOGO = """
                                    ██████╗ ██╗   ██╗███╗   ███╗██████╗ ██╗   ██╗
                                    ██╔══██╗██║   ██║████╗ ████║██╔══██╗╚██╗ ██╔╝
                                    ██║  ██║██║   ██║██╔████╔██║██████╔╝ ╚████╔╝
                                    ██║  ██║██║   ██║██║╚██╔╝██║██╔═══╝   ╚██╔╝
                                    ██████╔╝╚██████╔╝██║ ╚═╝ ██║██║        ██║
                                    ╚═════╝  ╚═════╝ ╚═╝     ╚═╝╚═╝        ╚═╝
                                    """;
    private CommandRouter commandRouter;
    private Todo todo;

    /**
     * Constructs a new Dumpy instance. Initializes the todo list by reading
     * from persistent storage and sets up the command router.
     */
    public Dumpy() {
        this.todo = Data.readListFromFile();
        this.commandRouter = new CommandRouter(todo);
    }
}
