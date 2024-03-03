import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestBulls {

    @Test
    void testCreateSecretNum() {
        String secretNum = Main.createSecretNum(4);
        assertNotNull(secretNum);
        assertEquals(4, secretNum.length());
        assertTrue(secretNum.matches("[0-9]+")); // Ensure it contains only digits
    }
}