package components.task;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import utilities.Data;

public class TaskTest {

    @Test
    void encodeDecode_basicTodo() {
        Task original = new Task("Write docs");
        assertFalse(original.isDone());

        String encoded = original.encodeData();
        String[] tokens = encoded.split(Data.DELIMITER);
        Task decoded = Task.decodeData(tokens);

        assertEquals("Write docs", decoded.getDescription());
        assertFalse(decoded.isDone());
    }

    @Test
    void toggleDone_flipsState() {
        Task t = new Task("Run tests");
        assertFalse(t.isDone());
        t.toggleDone();
        assertTrue(t.isDone());
        t.toggleDone();
        assertFalse(t.isDone());
    }

    @Test
    void decode_invalidTokenLength_returnsCorruptViaDataDecode() {
        String corruptLine = String.join(Data.DELIMITER, "TODO", "OnlyTwoTokens");
        Task decoded = utilities.Data.decodeData(corruptLine);
        assertEquals("Corrupt Task", decoded.getDescription());
    }
}
