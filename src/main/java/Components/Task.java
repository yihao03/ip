package Components;

public class Task {
  private String description;
  private boolean isDone;

  public Task(String description) {
    this.description = description;
    this.isDone = false;
  }

  public boolean toggleDone() {
    this.isDone = !this.isDone;
    return this.isDone;
  }

  public String getDescription() {
    return description;
  }

  @Override
  public String toString() {
    String status = isDone ? "[✅] " : "[ ] ";
    return status + description;
  }
}
