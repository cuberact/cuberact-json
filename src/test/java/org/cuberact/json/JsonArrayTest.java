package org.cuberact.json;

import org.cuberact.json.formatter.JsonFormatter;
import org.cuberact.json.parser.JsonParser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public class JsonArrayTest {

    @Test
    public void api() {
        JsonArray json = new JsonArray();
        assertEquals(JsonType.ARRAY, json.type());
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
        assertEquals("hello", json.getString(2,"def"));
        assertEquals(new Integer(45), json.getInt(3,12));
        assertEquals(new Long(12L), json.getLong(4,22L));
        assertEquals(new Float(3.4f), json.getFloat(5, 4.3f));
        assertEquals(new Double(1.2d), json.getDouble(6,123.5d));
        assertEquals(Boolean.TRUE, json.getBoolean(7, false));
        assertNull(json.get(8));
        assertTrue(json.contains("hello"));
        json.remove("hello");
        assertFalse(json.contains("hello"));
        assertTrue(json.contains(12L));
        json.remove(3);
        assertFalse(json.contains(12L));
    }

    @Test
    public void ifNotExists() {
        JsonArray json = new JsonArray();
        JsonObject jo = new JsonObject();
        JsonArray ja = new JsonArray();
        BigInteger bi = new BigInteger("12", 10);
        BigDecimal bd = new BigDecimal("1.2");
        assertEquals(jo, json.getObj(1, jo));
        assertEquals(ja, json.getArr(1, ja));
        assertEquals("DEF", json.getString(1, "DEF"));
        assertEquals(12, json.getInt(1, 12));
        assertEquals(12L, json.getLong(1, 12L));
        assertEquals(1.2, json.getDouble(1, 1.2));
        assertEquals(1.2f, json.getFloat(1, 1.2f));
        assertEquals(true, json.getBoolean(1, true));
        assertEquals(true, json.getBoolean(1, true));
        assertEquals(bi, json.getBigInt(1, bi));
        assertEquals(bd, json.getBigDecimal(1, bd));
    }

    @Test
    public void index() {
        JsonArray jsonArray = new JsonArray()
                .add(1)
                .add(2)
                .add(3);
        jsonArray.add(0, 0);
        assertEquals(Arrays.toString(new int[]{0, 1, 2, 3}), Arrays.toString(jsonArray.list().toArray()));
        jsonArray.set(0, -1);
        assertEquals(Arrays.toString(new int[]{-1, 1, 2, 3}), Arrays.toString(jsonArray.list().toArray()));
        assertTrue(jsonArray.isNotNull(2));
        assertEquals(2, jsonArray.indexOf(2));
    }

    @Test
    public void stream() {
        JsonArray jsonArray = new JsonArray()
                .add(1.0)
                .add(2.0)
                .add(3.0);
        List<Object> expected = jsonArray.stream().collect(Collectors.toList());
        jsonArray.add("other");
        List<Double> result = new ArrayList<>();
        jsonArray.streamOf(Double.class).forEach(result::add);
        assertEquals(expected, result);
    }

    @Test
    public void streamDoubles() {
        JsonArray jsonArray = new JsonArray()
                .add(1.0)
                .add(2.0)
                .add(3.0);
        List<Double> expected = jsonArray.listOf(Double.class);
        jsonArray.add("other");
        List<Double> result = new ArrayList<>();
        jsonArray.streamOf(Double.class).forEach(result::add);
        assertEquals(expected, result);
    }

    @Test
    public void streamFloats() {
        JsonArray jsonArray = new JsonArray()
                .add(1.0f)
                .add(2.0f)
                .add(3.0f);
        List<Float> expected = jsonArray.listOf(Float.class);
        jsonArray.add("other");
        List<Float> result = new ArrayList<>();
        jsonArray.streamOf(Float.class).forEach(result::add);
        assertEquals(expected, result);
    }

    @Test
    public void streamStrings() {
        JsonArray jsonArray = new JsonArray()
                .add("1")
                .add("2")
                .add("3");
        List<String> expected = jsonArray.listOf(String.class);
        jsonArray.add(1.0);
        List<String> result = new ArrayList<>();
        jsonArray.streamOf(String.class).forEach(result::add);
        assertEquals(expected, result);
    }

    @Test
    public void iterateDoubles() {
        JsonArray jsonArray = new JsonArray()
                .add(1.0)
                .add(2.0)
                .add(3.0);
        List<Double> expected = jsonArray.listOf(Double.class);
        jsonArray.add("other");
        List<Double> result = new ArrayList<>();
        for (Double value : jsonArray.iterableOf(Double.class)) {
            result.add(value);
        }
        assertEquals(expected, result);
    }

    @Test
    public void iterateFloats() {
        JsonArray jsonArray = new JsonArray()
                .add(1.0f)
                .add(2.0f)
                .add(3.0f);
        List<Float> expected = jsonArray.listOf(Float.class);
        jsonArray.add("other");
        List<Float> result = new ArrayList<>();
        for (Float value : jsonArray.iterableOf(Float.class)) {
            result.add(value);
        }
        assertEquals(expected, result);
    }

    @Test
    public void iterateStrings() {
        JsonArray jsonArray = new JsonArray()
                .add("1")
                .add("2")
                .add("3");
        List<String> expected = jsonArray.listOf(String.class);
        jsonArray.add(1.0);
        List<String> result = new ArrayList<>(jsonArray.listOf(String.class));
        assertEquals(expected, result);
    }

    @Test
    public void arrayRoot() {
        JsonArray root = new JsonArray();
        assertEquals("[]", root.toString(JsonFormatter.PACKED()));
    }

    @Test
    public void jsonArray() {
        JsonArray root = new JsonArray();
        root.add(null);
        root.add(true);
        root.add(1L);
        root.add(2.0);
        root.add("hello");
        root.add(new JsonArray().add(1L).add(2L).add(3L));
        root.add(new JsonArray().add(1.0).add(2.0).add(3.0));
        root.add(new JsonArray().add("1").add("2").add("3"));
        String expected = "[null,true,1,2.0,'hello',[1,2,3],[1.0,2.0,3.0],['1','2','3']]"
                .replace('\'', '"');
        assertEquals(expected, root.toString(JsonFormatter.PACKED()));
        Json parsed = new JsonParser().parse(expected);
        assertEquals(expected, parsed.toString(JsonFormatter.PACKED()));
    }

    @Test
    public void nestedJsonArray() {
        JsonArray root = new JsonArray();
        root.add(1L);
        JsonArray nested = new JsonArray();
        nested.add(2L);
        nested.add(3L);
        nested.add(4L);
        nested.add(new JsonArray().add(5L).add(6L).add(7L));
        root.add(nested);
        String expected = "[1,[2,3,4,[5,6,7]]]";
        assertEquals(expected, root.toString(JsonFormatter.PACKED()));
        Json parsed = new JsonParser().parse(expected);
        assertEquals(expected, parsed.toString(JsonFormatter.PACKED()));
    }

    @Test
    public void getWrongTypeButIsNull() {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(null);
        assertNull(jsonArray.getObj(0));
    }

    @Test
    public void getWrongType() {
        assertThrows(JsonException.class, () -> {
            JsonArray jsonArray = new JsonArray();
            jsonArray.add("hello");
            jsonArray.getObj(0);
        });
    }

    @Test
    public void getOutOfBound() {
        assertThrows(JsonException.class, () -> {
            JsonArray jsonArray = new JsonArray();
            jsonArray.add(1L);
            jsonArray.get(99);
        });
    }

    @Test
    public void getWithTypeOutOfBound() {
        assertThrows(JsonException.class, () -> {
            JsonArray jsonArray = new JsonArray();
            jsonArray.add(1L);
            jsonArray.getObj(99);
        });
    }

    @Test
    public void addOutOfBound() {
        assertThrows(JsonException.class, () -> {
            JsonArray jsonArray = new JsonArray();
            jsonArray.add(1L);
            jsonArray.add(99, 2L);
        });
    }

    @Test
    public void setOutOfBound() {
        assertThrows(JsonException.class, () -> {
            JsonArray jsonArray = new JsonArray();
            jsonArray.add(1L);
            jsonArray.set(99, 2L);
        });
    }

    @Test
    public void removeOutOfBound() {
        assertThrows(JsonException.class, () -> {
            JsonArray jsonArray = new JsonArray();
            jsonArray.add(1L);
            jsonArray.remove(99);
        });
    }
}
