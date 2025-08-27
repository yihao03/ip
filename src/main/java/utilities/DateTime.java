package utilities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Utility class for parsing and formatting date-time values used by the app.
 * Input format: {@link #INPUT_DATE_FORMAT}. Output (display) format:
 * {@link #OUTPUT_DATE_FORMAT}.
 */
public class DateTime {
    /** Example date string shown to users as a formatting guide. */
    public static final String EXAMPLE_DATE = "2025-10-31 23:59";
    /** Pattern expected for user input / internal serialization. */
    public static final String INPUT_DATE_FORMAT = "yyyy-MM-dd HH:mm";
    /** Pattern used for user-friendly display output. */
    public static final String OUTPUT_DATE_FORMAT = "d MMM yyyy, h:mm a";

    /** Formatter for parsing / formatting in the canonical input pattern. */
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateTime.INPUT_DATE_FORMAT);
    /** Formatter for pretty-printing to users. */
    private static DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(DateTime.OUTPUT_DATE_FORMAT);

    /**
     * Continuously reads lines from {@link IO#readLine()} until a valid
     * date-time matching {@link #INPUT_DATE_FORMAT} is entered.
     *
     * @return parsed {@link LocalDateTime}
     */
    public static LocalDateTime getDateTime() {
        String date;
        while (true) {
            date = IO.readLine();
            try {
                return DateTime.parseDateTime(date);
            } catch (DateTimeParseException e) {
                System.out.printf("Invalid format. Use %s (e.g., %s)\n", DateTime.INPUT_DATE_FORMAT,
                                                DateTime.EXAMPLE_DATE);
            }
        }
    }

    /**
     * Parses a date-time string using the canonical input format.
     *
     * @param date raw date-time string
     * @return parsed {@link LocalDateTime}
     * @throws DateTimeParseException if the string does not match
     * {@link #INPUT_DATE_FORMAT}
     */
    public static LocalDateTime parseDateTime(String date) throws DateTimeParseException {
        return LocalDateTime.parse(date, formatter);
    }

    /**
     * Formats a {@link LocalDateTime} into the canonical input format (useful
     * for storage).
     *
     * @param dateTime date-time to format
     * @return formatted string in {@link #INPUT_DATE_FORMAT}
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(formatter);
    }

    /**
     * Formats a {@link LocalDateTime} into a user-friendly display format.
     *
     * @param dateTime date-time to format
     * @return formatted string in {@link #OUTPUT_DATE_FORMAT}
     */
    public static String printDateTime(LocalDateTime dateTime) {
        return dateTime.format(outputFormatter);
    }
}
