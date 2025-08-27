package utilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class IO {
    private final static BufferedReader in = new BufferedReader(
                                    new InputStreamReader(System.in, StandardCharsets.UTF_8));

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

    public static String readLine(String prompt) {
        System.out.printf(prompt + "\n");
        return readLine();
    }

    public static String extractCommand(String input) {
        int i = 0;
        while (i < input.length() && Character.isLetter(input.charAt(i))) {
            i++;
        }
        return input.substring(0, i).toLowerCase();
    }

    public static String extractArgs(String input, String command) {
        if (input.length() <= command.length()) {
            return "";
        }
        return input.substring(command.length()).trim();
    }

    public static Integer parseIntArg(String args) {
        if (args == null || args.isBlank())
            return null;
        String num = args.replaceAll("[^0-9-]", "");
        if (num.isEmpty())
            return null;
        try {
            return Integer.parseInt(num);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
