package org.cuberact.json;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public class JsonArrayTest {

    @Test
    public void api() {
        JsonArray json = new JsonArray();

        assertEquals(JsonType.JSON_ARRAY, json.type());

        json.add(new JsonObject());
        json.add(new JsonArray());
        json.add("hello");
        json.add(45);
        json.add(12L);
        json.add(3.4f);
        json.add(1.2d);
        json.add(true);
        json.add(null);

        assertEquals(9, json.size());

        assertEquals(JsonObject.class, json.getObj(0).getClass());
        assertEquals(JsonArray.class, json.getArr(1).getClass());
        assertEquals("hello", json.getString(2));
        assertEquals(new Integer(45), json.getInt(3));
        assertEquals(new Long(12L), json.getLong(4));
        assertEquals(new Float(3.4f), json.getFloat(5));
        assertEquals(new Double(1.2d), json.getDouble(6));
        assertEquals(Boolean.TRUE, json.getBoolean(7));
        assertNull(json.get(8));

        assertTrue(json.contains("hello"));
        json.remove("hello");
        assertFalse(json.contains("hello"));

        assertTrue(json.contains(12L));
        json.remove(3);
        assertFalse(json.contains(12L));
    }
}
