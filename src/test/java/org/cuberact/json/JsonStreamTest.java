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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public class JsonStreamTest {

    @Test
    public void JsonArrayStreamDoubles() {
        JsonArray jsonArray = new JsonArray().add(1.0).add(2.0).add(3.0);
        List<Double> expected = jsonArray.listOf(Double.class);
        jsonArray.add("other");
        List<Double> result = new ArrayList<>();
        jsonArray.streamOf(Double.class).forEach(result::add);
        assertEquals(expected, result);
    }

    @Test
    public void JsonArrayStreamFloats() {
        JsonArray jsonArray = new JsonArray().add(1.0f).add(2.0f).add(3.0f);
        List<Float> expected = jsonArray.listOf(Float.class);
        jsonArray.add("other");
        List<Float> result = new ArrayList<>();
        jsonArray.streamOf(Float.class).forEach(result::add);
        assertEquals(expected, result);
    }

    @Test
    public void JsonArrayStreamStrings() {
        JsonArray jsonArray = new JsonArray().add("1").add("2").add("3");
        List<String> expected = jsonArray.listOf(String.class);
        jsonArray.add(1.0);
        List<String> result = new ArrayList<>();
        jsonArray.streamOf(String.class).forEach(result::add);
        assertEquals(expected, result);
    }

    @Test
    public void JsonObjectStream() {
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
    public void JsonObjectStreamStrings() {
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
}
