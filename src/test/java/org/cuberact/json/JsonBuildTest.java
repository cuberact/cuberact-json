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
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Michal NkD Nikodim (michal.nikodim@gmail.com)
 */
public class JsonBuildTest {

    @Test
    public void objectRoot() {
        JsonObject root = new JsonObject();
        assertEquals("{}", root.toString(JsonFormatter.PACKED()));
    }

    @Test
    public void arrayRoot() {
        JsonArray root = new JsonArray();
        assertEquals("[]", root.toString(JsonFormatter.PACKED()));
    }

    @Test
    public void jsonObject() {
        JsonObject root = new JsonObject();
        root.add("empty", null);
        root.add("boolean", true);
        root.add("double", 1.0);
        root.add("string", "ahoj");
        root.add("intArray", new JsonArray().add(1L).add(2L).add(3L));
        root.add("floatArray", new JsonArray().add(1.0).add(2.0).add(3.0));
        root.add("stringArray", new JsonArray().add("1").add("2").add("3"));
        String result = "{'empty':null,'boolean':true,'double':1.0,'string':'ahoj','intArray':[1,2,3],'floatArray':[1.0,2.0,3.0],'stringArray':['1','2','3']}".replace('\'', '"');
        assertEquals(result, root.toString(JsonFormatter.PACKED()));

        Json parsed = new JsonParser().parse(result);
        assertEquals(result, parsed.toString(JsonFormatter.PACKED()));
    }

    @Test
    public void jsonArray() {
        JsonArray root = new JsonArray();
        root.add(null);
        root.add(true);
        root.add(1L);
        root.add(2.0);
        root.add("ahoj");
        root.add(new JsonArray().add(1L).add(2L).add(3L));
        root.add(new JsonArray().add(1.0).add(2.0).add(3.0));
        root.add(new JsonArray().add("1").add("2").add("3"));
        String result = "[null,true,1,2.0,'ahoj',[1,2,3],[1.0,2.0,3.0],['1','2','3']]".replace('\'', '"');
        assertEquals(result, root.toString(JsonFormatter.PACKED()));

        Json parsed = new JsonParser().parse(result);
        assertEquals(result, parsed.toString(JsonFormatter.PACKED()));
    }

    @Test
    public void nestedJsonObject() {
        JsonObject root = new JsonObject();
        root.add("string", "ahoj");
        JsonObject nested = new JsonObject();
        nested.add("value", true);
        root.add("nested", nested);
        String result = "{'string':'ahoj','nested':{'value':true}}".replace('\'', '"');
        assertEquals(result, root.toString(JsonFormatter.PACKED()));
        Json parsed = new JsonParser().parse(result);
        assertEquals(result, parsed.toString(JsonFormatter.PACKED()));
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
        String result = "[1,[2,3,4,[5,6,7]]]";
        assertEquals(result, root.toString(JsonFormatter.PACKED()));

        Json parsed = new JsonParser().parse(result);
        assertEquals(result, parsed.toString(JsonFormatter.PACKED()));
    }

    @Test
    public void complexJson() {
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
        child2.add("name", "neto");
        child2.add("age", 9L);
        children.add(child1);
        children.add(child2);
        person.add("children", children);
        root.add("person", person);
        String result = "{'attr':'value','person':{'name':'nkd','age':38,'pick':[1,2,3],'children':[{'name':'max','age':15},{'name':'neto','age':9}]}}".replace('\'', '"');
        assertEquals(result, root.toString(JsonFormatter.PACKED()));

        Json parsed = new JsonParser().parse(result);
        assertEquals(result, parsed.toString(JsonFormatter.PACKED()));
    }

    @Test
    public void testArray() {
        String source = "{'arr':[1,2,3,4,2147483647,-2147483648]}".replace('\'', '"');
        JsonObject json = new JsonParser().parse(source);
        assertEquals(JsonArray.class, json.getArr("arr").getClass());
        assertEquals(source, json.toString(JsonFormatter.PACKED()));
    }

    @Test
    public void testArrayMixed1() {
        String source = "{'arr':[true,'hello','a','b']}".replace('\'', '"');
        JsonObject json = new JsonParser().parse(source);
        assertEquals(JsonArray.class, json.getArr("arr").getClass());
        assertEquals(source, json.toString(JsonFormatter.PACKED()));
    }

    @Test
    public void testArrayMixed2() {
        String source = "{'arr':[1.3234234,'hello','a','b']}".replace('\'', '"');
        JsonObject json = new JsonParser().parse(source);
        assertEquals(JsonArray.class, json.getArr("arr").getClass());
        assertEquals(source, json.toString(JsonFormatter.PACKED()));
    }
}
