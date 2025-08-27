package components;

import components.task.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import utilities.DateTime;

public class TodoTest {

    @Test
    void addTask_increasesSize() {
        Todo todo = new Todo();
        todo.addTask(Task.decodeData(new String[] { TaskType.TODO.toString(), "Task A", "0" }));
        assertEquals(1, todo.getTasks().size());
    }

    @Test
    void listTasks_formatsAll() {
        Todo todo = new Todo();
        todo.loadTask(Task.decodeData(new String[] { TaskType.TODO.toString(), "Alpha", "0" }));
        todo.loadTask(Task.decodeData(new String[] { TaskType.TODO.toString(), "Beta", "0" }));
        String out = todo.listTasks();
        assertTrue(out.contains("1."));
        assertTrue(out.contains("Alpha"));
        assertTrue(out.contains("2."));
        assertTrue(out.contains("Beta"));
    }

    @Test
    void findTasksByDescription_filtersCaseInsensitiveAllSubstrings() {
        Todo todo = new Todo();
        todo.loadTask(Task.decodeData(new String[] { TaskType.TODO.toString(), "Write Integration Tests", "0" }));
        todo.loadTask(Task.decodeData(new String[] { TaskType.TODO.toString(), "Refactor Parser", "0" }));
        String out = todo.findTasksByDescription("write", "tests");
        assertTrue(out.toLowerCase().contains("write integration tests"));
        assertFalse(out.toLowerCase().contains("refactor parser"));
    }

    @Test
    void findTasksByDescription_noArgsThrows() {
        Todo todo = new Todo();
        assertThrows(IllegalArgumentException.class, () -> todo.findTasksByDescription());
    }

    @Test
    void toggleDone_flipsStatus() throws Exception {
        Todo todo = new Todo();
        todo.loadTask(Task.decodeData(new String[] { TaskType.TODO.toString(), "Toggle Me", "0" }));
        assertFalse(todo.getTasks().get(0).isDone());
        todo.toggleDone(1);
        assertTrue(todo.getTasks().get(0).isDone());
    }

    @Test
    void listDueSoonTasks_includesOnlyDueSoon() {
        Todo todo = new Todo();
        String[] dlSoon = new String[] { TaskType.DEADLINE.toString(), "SoonDeadline", "0",
                DateTime.formatDateTime(LocalDateTime.now().plusDays(2)) };
        todo.loadTask(DeadlineTask.decodeData(dlSoon));

        String[] dlFar = new String[] { TaskType.DEADLINE.toString(), "FarDeadline", "0",
                DateTime.formatDateTime(LocalDateTime.now().plusDays(15)) };
        todo.loadTask(DeadlineTask.decodeData(dlFar));

        todo.loadTask(new EventTask("SoonEvent", LocalDateTime.now().plusDays(3),
                                        LocalDateTime.now().plusDays(3).plusHours(1)));

        String out = todo.listDueSoonTasks();
        assertTrue(out.contains("SoonDeadline"));
        assertTrue(out.contains("SoonEvent"));
        assertFalse(out.contains("FarDeadline"));
    }
}
