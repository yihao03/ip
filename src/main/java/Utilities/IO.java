package Utilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class IO {
  private final static BufferedReader in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));

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
}
