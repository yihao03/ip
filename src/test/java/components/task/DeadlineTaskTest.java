package components.task;

import org.junit.jupiter.api.Test;

import utilities.DateTime;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class DeadlineTaskTest {

    private DeadlineTask buildDeadline(String description, boolean done, LocalDateTime deadline) {
        String[] tokens = new String[] { TaskType.DEADLINE.toString(), description, done ? "1" : "0",
                DateTime.formatDateTime(deadline) };
        return (DeadlineTask) DeadlineTask.decodeData(tokens);
    }

    @Test
    void encodeDecode_roundTrip() {
        LocalDateTime dl = LocalDateTime.now().plusDays(2).withSecond(0).withNano(0);
        DeadlineTask task = buildDeadline("Submit report", false, dl);
        String encoded = task.encodeData();
        String[] tokens = encoded.split(utilities.Data.DELIMITER);
        DeadlineTask decoded = (DeadlineTask) DeadlineTask.decodeData(tokens);
        assertEquals("Submit report", decoded.getDescription());
        assertFalse(decoded.isDone());
    }

    @Test
    void isDueSoon_trueWithin7DaysAndNotDone() {
        DeadlineTask soon = buildDeadline("Soon", false, LocalDateTime.now().plusDays(6));
        assertTrue(soon.isDueSoon());
    }

    @Test
    void isDueSoon_falseIfDone() {
        DeadlineTask doneSoon = buildDeadline("DoneSoon", true, LocalDateTime.now().plusDays(3));
        assertFalse(doneSoon.isDueSoon());
    }

    @Test
    void isDueSoon_falseIfBeyond7Days() {
        DeadlineTask far = buildDeadline("Far", false, LocalDateTime.now().plusDays(10));
        assertFalse(far.isDueSoon());
    }

    @Test
    void isDueSoon_falseIfInPast() {
        DeadlineTask past = buildDeadline("Past", false, LocalDateTime.now().minusDays(1));
        assertFalse(past.isDueSoon());
    }
}
