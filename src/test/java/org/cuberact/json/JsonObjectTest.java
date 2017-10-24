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

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public class JsonObjectTest {

    @Test
    public void api() {
        JsonObject json = new JsonObject();

        assertEquals(JsonType.JSON_OBJECT, json.type());

        json.add("obj", new JsonObject());
        json.add("arr", new JsonArray());
        json.add("string", "hello");
        json.add("int", 45);
        json.add("long", 12L);
        json.add("float", 3.4f);
        json.add("double", 1.2d);
        json.add("boolean", true);
        json.add("null", null);

        assertEquals(9, json.size());

        assertEquals(JsonObject.class, json.getObj("obj").getClass());
        assertEquals(JsonArray.class, json.getArr("arr").getClass());
        assertEquals("hello", json.getString("string"));
        assertEquals(new Integer(45), json.getInt("int"));
        assertEquals(new Long(12L), json.getLong("long"));
        assertEquals(new Float(3.4f), json.getFloat("float"));
        assertEquals(new Double(1.2d), json.getDouble("double"));
        assertEquals(Boolean.TRUE, json.getBoolean("boolean"));
        assertTrue(json.contains("null"));
        assertNull(json.get("null"));

        assertTrue(json.contains("obj"));
        assertTrue(json.isNotNull("obj"));
        json.remove("obj");
        assertFalse(json.contains("obj"));
        assertFalse(json.isNotNull("obj"));
    }
}
