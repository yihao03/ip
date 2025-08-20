package Exceptions;

public class TaskNotFoundException extends Exception {
  public TaskNotFoundException() {
    super();
  }

  public TaskNotFoundException(int listSize, int input) {
    super(
        String.format(
            "Given index %d exceeds todo list size %d! \n",
            input,
            listSize));
  }
}
