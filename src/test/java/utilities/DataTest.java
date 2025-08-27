package utilities;

import components.Todo;
import components.task.*;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class DataTest {

    private static Path dataDir = Path.of("data");
    private static Path dataFile = dataDir.resolve("todo_list.txt");
    private static String originalContent;

    @BeforeAll
    static void backupIfExists() throws IOException {
        if (Files.exists(dataFile)) {
            originalContent = Files.readString(dataFile);
        }
    }

    @AfterAll
    static void restore() throws IOException {
        if (originalContent != null) {
            Files.writeString(dataFile, originalContent);
        } else if (Files.exists(dataFile)) {
            Files.delete(dataFile);
            if (Files.exists(dataDir) && Files.list(dataDir).findAny().isEmpty()) {
                Files.delete(dataDir);
            }
        }
    }

    @Test
    void createAndPersist_tasksRoundTrip() {
        Todo todo = new Todo();

        Task basic = Task.decodeData(new String[] { TaskType.TODO.toString(), "Alpha", "0" });
        todo.loadTask(basic);

        String[] dlTokens = new String[] { TaskType.DEADLINE.toString(), "Beta", "0",
                DateTime.formatDateTime(LocalDateTime.now().plusDays(2)) };
        todo.loadTask(DeadlineTask.decodeData(dlTokens));

        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusHours(3);
        todo.loadTask(new EventTask("Gamma", start, end));

        Data.saveListToFile(todo);
        Todo loaded = Data.readListFromFile();

        assertEquals(3, loaded.getTasks().size());
        assertTrue(loaded.getTasks().get(1) instanceof DeadlineTask);
        assertTrue(loaded.getTasks().get(2) instanceof EventTask);
    }

    @Test
    void decode_corruptLineProducesCorruptTask() {
        String line = "UNKNOWN" + Data.DELIMITER + "something" + Data.DELIMITER + "1";
        Task t = Data.decodeData(line);
        assertEquals("Corrupt Task", t.getDescription());
    }

    @Test
    void encode_producesLinePerTask() {
        Todo todo = new Todo();
        todo.loadTask(Task.decodeData(new String[] { TaskType.TODO.toString(), "One", "0" }));
        todo.loadTask(Task.decodeData(new String[] { TaskType.TODO.toString(), "Two", "0" }));
        String encoded = Data.encodeData(todo);
        long lines = encoded.lines().filter(s -> !s.isBlank()).count();
        assertEquals(2, lines);
    }
}
