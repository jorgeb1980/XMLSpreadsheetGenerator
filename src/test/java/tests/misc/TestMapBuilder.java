package tests.misc;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static xml.spreadsheet.utils.MapBuilder.mapOf;

public class TestMapBuilder {

    @Test
    public void testWrongArgumentNumber() {
        try {
            mapOf("single argument - should fail");
            fail();
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    @Test
    public void testTrivialSuccessCase() {
        Map<String, Object> map = mapOf("first", 1, "second", 2);
        assertEquals(1, map.get("first"));
        assertEquals(2, map.get("second"));
        assertNull(map.get("not exists"));
        assertEquals(2, map.keySet().size());
    }

    @Test
    public void testNullValues() {
        Map<String, Object> map = mapOf("first", 1, "second", null, null, 3);
        assertEquals(1, map.get("first"));
        assertNull(map.get("second"));
        assertEquals(1, map.keySet().size());
    }

    @Test
    public void testKeyTypes() {
        try {
            mapOf("first", 1, 2, 2);
            fail();
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }
}
