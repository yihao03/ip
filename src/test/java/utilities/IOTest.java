package utilities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

// CHECKSTYLE:OFF: AbbreviationAsWordInName
class IOTest {

    @Test
    void testExtractCommand() {
        assertEquals("list", IO.extractCommand("List all items"));
        assertEquals("", IO.extractCommand("123start"));
        assertEquals("add", IO.extractCommand("Add123value"));
        assertEquals("quit", IO.extractCommand("QUIT"));
        assertEquals("", IO.extractCommand(""));
    }

    @Test
    void testExtractArgs() {
        assertEquals("all items", IO.extractArgs("list all items", "list"));
        assertEquals("", IO.extractArgs("list", "list"));
        assertEquals("", IO.extractArgs("li", "list"));
        assertEquals("123value", IO.extractArgs("add 123value", "add"));
    }

    @Test
    void testParseIntArg() {
        assertNull(IO.parseIntArg(null));
        assertNull(IO.parseIntArg(""));
        assertEquals(42, IO.parseIntArg(" id: 42 apples"));
        assertEquals(-15, IO.parseIntArg(" -15 "));
        assertNull(IO.parseIntArg("abc"));
        assertNull(IO.parseIntArg("--5"));
        assertEquals(0, IO.parseIntArg("zero0"));
    }
}
// CHECKSTYLE:ON: AbbreviationAsWordInName
