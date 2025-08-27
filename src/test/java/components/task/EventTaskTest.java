package components.task;

import org.junit.jupiter.api.Test;

import utilities.DateTime;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class EventTaskTest {

    @Test
    void encodeDecode_roundTrip() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusHours(2);
        EventTask event = new EventTask("Workshop", start, end);
        String encoded = event.encodeData();
        String[] tokens = encoded.split(utilities.Data.DELIMITER);
        EventTask decoded = (EventTask) EventTask.decodeData(tokens);
        assertEquals("Workshop", decoded.getDescription());
        assertFalse(decoded.isDone());
    }

    @Test
    void isDueSoon_trueWhenStartsWithinWeek() {
        LocalDateTime start = LocalDateTime.now().plusDays(3);
        LocalDateTime end = start.plusHours(1);
        EventTask event = new EventTask("Seminar", start, end);
        assertTrue(event.isDueSoon());
    }

    @Test
    void isDueSoon_falseWhenStartsAfterWeek() {
        LocalDateTime start = LocalDateTime.now().plusDays(8);
        LocalDateTime end = start.plusHours(1);
        EventTask event = new EventTask("LaterEvent", start, end);
        assertFalse(event.isDueSoon());
    }

    @Test
    void isDueSoon_falseWhenAlreadyDone() {
        LocalDateTime start = LocalDateTime.now().plusDays(2);
        LocalDateTime end = start.plusHours(1);
        EventTask event = new EventTask("DoneEvent", true, start, end);
        assertFalse(event.isDueSoon());
    }
}
