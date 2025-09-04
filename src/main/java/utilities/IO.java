package utilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Utility class providing simple console I/O helpers and lightweight command
 * parsing utilities for the application.
 * <p>
 * Methods are static and rely on a shared {@link BufferedReader} wrapping
 * {@code System.in} using UTF-8 encoding.
 */
public class IO {
    /** Shared UTF-8 buffered reader for standard input. */
    private static final BufferedReader in = new BufferedReader(
                                    new InputStreamReader(System.in, StandardCharsets.UTF_8));

    /**
     * @deprecated. Reads a single line from standard input after printing a ">
     * " prompt. If no console is attached (e.g., running in an IDE), it echoes
     * the entered line back to standard output for visibility.
     *
     * @return the line read, or an empty string on error (never null)
     */
    public static String readLine() {
        try {
            System.out.printf("> ");
            String line = in.readLine();
            // print output if not running in a console environment
            if (System.console() == null && line != null) {
                System.out.println(line);
            }
            return line;
        } catch (Exception e) {
            System.out.println("Error reading input: " + e.getMessage());
            return "";
        }
    }

    /**
     * @deprecated. a custom prompt (with newline) and then delegates to
     * {@link #readLine()}.
     *
     * @param prompt message to show before reading
     * @return user input line (never null, empty string on error)
     */
    public static String readLine(String prompt) {
        System.out.printf(prompt + "\n");
        return readLine();
    }

    /**
     * Extracts the leading alphabetic characters from the provided input
     * (intended as the command token) and returns them in lower case.
     * Non-letter characters terminate the scan.
     *
     * @param input full user input string
     * @return lower-cased leading command segment (possibly empty)
     */
    public static String extractCommand(String input) {
        int i = 0;
        while (i < input.length() && Character.isLetter(input.charAt(i))) {
            i++;
        }
        return input.substring(0, i).toLowerCase();
    }

    /**
     * Returns the argument substring following the command portion, trimmed.
     *
     * @param input original input line
     * @param command the command previously extracted
     * @return arguments string (empty if none)
     */
    public static String extractArgs(String input, String command) {
        if (input.length() <= command.length()) {
            return "";
        }
        return input.substring(command.length()).trim();
    }

    /**
     * Attempts to parse an integer from an argument string by stripping all
     * characters except digits and minus signs. Returns null if no numeric
     * content remains or parsing fails.
     *
     * @param args raw argument string
     * @return parsed Integer or null if absent / invalid
     */
    public static Integer parseIntArg(String args) {
        if (args == null || args.isBlank()) {
            return null;
        }
        String num = args.replaceAll("[^0-9-]", "");
        if (num.isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(num);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
