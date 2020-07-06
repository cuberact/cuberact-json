/*
 * Copyright 2017 Michal Nikodim
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.cuberact.json;

import org.cuberact.json.formatter.JsonFormatter;
import org.cuberact.json.parser.JsonParser;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public class JsonObjectTest {

    @Test
    public void api() {
        JsonObject json = new JsonObject();
        assertEquals(JsonType.OBJECT, json.type());
        json.add("obj", new JsonObject());
        json.add("arr", new JsonArray());
        json.add("string", "hello");
        json.add("int", 45);
        json.add("long", 12L);
        json.add("float", 3.4f);
        json.add("double", 1.2d);
        json.add("bigint", new BigInteger("1234567890"));
        json.add("bigDecimal", new BigDecimal("123.4567890"));
        json.add("boolean", true);
        json.add("null", null);
        assertEquals(11, json.size());
        assertEquals(JsonObject.class, json.getObj("obj").getClass());
        assertEquals(JsonArray.class, json.getArr("arr").getClass());
        assertEquals("hello", json.getString("string"));
        assertEquals(new Integer(45), json.getInt("int"));
        assertEquals(new Long(12L), json.getLong("long"));
        assertEquals(new Float(3.4f), json.getFloat("float"));
        assertEquals(new Double(1.2d), json.getDouble("double"));
        assertEquals(new BigInteger("1234567890"), json.getBigInt("bigint"));
        assertEquals(new BigDecimal("123.4567890"), json.getBigDecimal("bigDecimal"));
        assertEquals(Boolean.TRUE, json.getBoolean("boolean"));
        assertTrue(json.contains("null"));
        assertNull(json.get("null"));
        assertTrue(json.contains("obj"));
        assertTrue(json.isNotNull("obj"));

        JsonObject jo1 = new JsonObject();
        jo1.add("1", "2");
        JsonArray ja1 = new JsonArray();
        ja1.add("1");

        assertEquals(jo1, json.getObj("unexisted", jo1));
        assertEquals(ja1, json.getArr("unexisted", ja1));
        assertEquals("abc", json.getString("unexisted", "abc"));
        assertEquals(new Integer(123), json.getInt("unexisted", 123));
        assertEquals(new Long(123), json.getLong("unexisted", 123L));
        assertEquals(Boolean.TRUE, json.getBoolean("unexisted", true));
        assertEquals(new Double(1.543), json.getDouble("unexisted", 1.543));
        assertEquals(new BigInteger("123"), json.getBigInt("unexisted", new BigInteger("123")));
        assertEquals(new BigDecimal("123.123"), json.getBigDecimal("unexisted", new BigDecimal("123.123")));

        assertNotEquals(jo1, json.getObj("obj", jo1));
        assertNotEquals(ja1, json.getArr("arr", ja1));
        assertEquals("hello", json.getString("string", "abc"));
        assertEquals(new Integer(45), json.getInt("int", 123));
        assertEquals(new Long(12L), json.getLong("long", 123L));
        assertEquals(new Float(3.4f), json.getFloat("float", 7.2f));
        assertEquals(new Double(1.2d), json.getDouble("double", 5.5));
        assertEquals(new BigInteger("1234567890"), json.getBigInt("bigint", new BigInteger("123")));
        assertEquals(new BigDecimal("123.4567890"), json.getBigDecimal("bigDecimal", new BigDecimal("555.555")));
        assertEquals(Boolean.TRUE, json.getBoolean("boolean", false));


        json.remove("obj");
        assertFalse(json.contains("obj"));
        assertFalse(json.isNotNull("obj"));
    }

    @Test
    public void stream() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("str1", "one");
        jsonObject.add("str2", "two");
        jsonObject.add("str3", "three");
        jsonObject.add("lng1", 1L);
        jsonObject.add("lng1", 2L);
        jsonObject.add("lng1", 3L);
        Map<String, Object> expected = jsonObject.map();
        Map<String, Object> result = new LinkedHashMap<>();
        jsonObject.stream().forEach(entry -> result.put(entry.getKey(), entry.getValue()));
        assertEquals(expected, result);
    }

    @Test
    public void streamStrings() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("str1", "one");
        jsonObject.add("str2", "two");
        jsonObject.add("str3", "three");
        jsonObject.add("lng1", 1L);
        jsonObject.add("lng1", 2L);
        jsonObject.add("lng1", 3L);
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("str1", "one");
        expected.put("str2", "two");
        expected.put("str3", "three");
        Map<String, String> result = new LinkedHashMap<>();
        jsonObject.streamOf(String.class).forEach(entry -> result.put(entry.getKey(), entry.getValue()));
        assertEquals(expected, result);
    }

    @Test
    public void iterate() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("str1", "one");
        jsonObject.add("str2", "two");
        jsonObject.add("str3", "three");
        jsonObject.add("lng1", 1L);
        jsonObject.add("lng1", 2L);
        jsonObject.add("lng1", 3L);
        Map<String, Object> expected = jsonObject.map();
        Map<String, Object> result = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : jsonObject.iterable()) {
            result.put(entry.getKey(), entry.getValue());
        }
        assertEquals(expected, result);
    }

    @Test
    public void iterateStrings() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("str1", "one");
        jsonObject.add("str2", "two");
        jsonObject.add("str3", "three");
        jsonObject.add("lng1", 1L);
        jsonObject.add("lng1", 2L);
        jsonObject.add("lng1", 3L);
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("str1", "one");
        expected.put("str2", "two");
        expected.put("str3", "three");
        Map<String, String> result = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : jsonObject.iterableOf(String.class)) {
            result.put(entry.getKey(), entry.getValue());
        }
        assertEquals(expected, result);
    }

    @Test
    public void mapOf() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("str1", "one");
        jsonObject.add("str2", "two");
        jsonObject.add("str3", "three");
        jsonObject.add("lng1", 1L);
        jsonObject.add("lng1", 2L);
        jsonObject.add("lng1", 3L);
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("str1", "one");
        expected.put("str2", "two");
        expected.put("str3", "three");
        Map<String, String> result = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : jsonObject.mapOf(String.class).entrySet()) {
            result.put(entry.getKey(), entry.getValue());
        }
        assertEquals(expected, result);
    }

    @Test
    public void objectRoot() {
        JsonObject root = new JsonObject();
        assertEquals("{}", root.toString(JsonFormatter.PACKED()));
    }

    @Test
    public void jsonObject() {
        JsonObject root = new JsonObject();
        root.add("empty", null);
        root.add("boolean", true);
        root.add("double", 1.0);
        root.add("string", "hello");
        root.add("intArray", new JsonArray().add(1L).add(2L).add(3L));
        root.add("floatArray", new JsonArray().add(1.0).add(2.0).add(3.0));
        root.add("stringArray", new JsonArray().add("1").add("2").add("3"));
        String expected = "{'empty':null,'boolean':true,'double':1.0,'string':'hello','intArray':[1,2,3],'floatArray':[1.0,2.0,3.0],'stringArray':['1','2','3']}"
                .replace('\'', '"');
        assertEquals(expected, root.toString(JsonFormatter.PACKED()));
        Json parsed = new JsonParser().parse(expected);
        assertEquals(expected, parsed.toString(JsonFormatter.PACKED()));
    }

    @Test
    public void nestedJsonObject() {
        JsonObject root = new JsonObject();
        root.add("string", "hello");
        JsonObject nested = new JsonObject();
        nested.add("value", true);
        root.add("nested", nested);
        String expected = "{'string':'hello','nested':{'value':true}}"
                .replace('\'', '"');
        assertEquals(expected, root.toString(JsonFormatter.PACKED()));
        Json parsed = new JsonParser().parse(expected);
        assertEquals(expected, parsed.toString(JsonFormatter.PACKED()));
    }

    @Test
    public void complex() {
        JsonObject root = new JsonObject();
        root.add("attr", "value");
        JsonObject person = new JsonObject();
        person.add("name", "nkd");
        person.add("age", 38L);
        person.add("pick", new JsonArray().add(1L).add(2L).add(3L));
        JsonArray children = new JsonArray();
        JsonObject child1 = new JsonObject();
        child1.add("name", "max");
        child1.add("age", 15L);
        JsonObject child2 = new JsonObject();
        child2.add("name", "jack");
        child2.add("age", 9L);
        children.add(child1);
        children.add(child2);
        person.add("children", children);
        root.add("person", person);
        String expected = "{'attr':'value','person':{'name':'nkd','age':38,'pick':[1,2,3],'children':[{'name':'max','age':15},{'name':'jack','age':9}]}}"
                .replace('\'', '"');
        assertEquals(expected, root.toString(JsonFormatter.PACKED()));
        Json parsed = new JsonParser().parse(expected);
        assertEquals(expected, parsed.toString(JsonFormatter.PACKED()));
    }

    @Test
    public void getWrongTypeButIsNull() {
        JsonObject jsonObject = new JsonObject();
        assertNull(jsonObject.getObj("attr"));
    }

    @Test
    public void getWrongType() {
        assertThrows(JsonException.class, () -> {
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("hello", "world");
            jsonObject.getLong("hello");
        });
    }
}
