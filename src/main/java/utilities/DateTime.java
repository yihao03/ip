package utilities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateTime {
  public static final String EXAMPLE_DATE = "2025-10-31 23:59";
  public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";

  private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateTime.DATE_FORMAT);

  public static LocalDateTime getDateTime() {
    String date;
    while (true) {
      date = IO.readLine();
      try {
        return LocalDateTime.parse(date, formatter);
      } catch (DateTimeParseException e) {
        System.out.printf("Invalid format. Use %s (e.g., %s)\n", DateTime.DATE_FORMAT, DateTime.EXAMPLE_DATE);
      }
    }
  }

  public static String formatDateTime(LocalDateTime dateTime) {
    return dateTime.format(formatter);
  }
}
