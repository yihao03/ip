package components.task;

/**
 * Enumeration of supported task categories.
 *
 * TODO - Simple task with only a description (no temporal data).
 * DEADLINE - Task associated with a single due date/time.
 * EVENT - Task spanning a start and end date/time.
 */
public enum TaskType {
    TODO,
    DEADLINE,
    EVENT
}
