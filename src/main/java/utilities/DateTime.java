package utilities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateTime {
    public static final String EXAMPLE_DATE = "2025-10-31 23:59";
    public static final String INPUT_DATE_FORMAT = "yyyy-MM-dd HH:mm";
    public static final String OUTPUT_DATE_FORMAT = "d MMM yyyy, h:mm a";

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateTime.INPUT_DATE_FORMAT);
    private static DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(DateTime.OUTPUT_DATE_FORMAT);

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

    public static LocalDateTime parseDateTime(String date) throws DateTimeParseException {
        return LocalDateTime.parse(date, formatter);
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(outputFormatter);
    }
}
