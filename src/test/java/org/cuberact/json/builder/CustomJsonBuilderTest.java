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

package org.cuberact.json.builder;

import org.cuberact.json.number.JsonNumber;
import org.cuberact.json.number.JsonNumberConverterIntFloat;
import org.cuberact.json.parser.JsonParser;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public class CustomJsonBuilderTest {

    @Test
    public void buildToMapAndList() {
        JsonBuilder customJsonBuilder = new JsonBuilder<Map<String, Object>, List<Object>>() {

            @Override
            public Map<String, Object> createObject() {
                return new LinkedHashMap<>();
            }

            @Override
            public List<Object> createArray() {
                return new ArrayList<>();
            }

            @Override
            public void addObjectToObject(Map<String, Object> object, String attr, Map<String, Object> subObject) {
                object.put(attr, subObject);
            }

            @Override
            public void addArrayToObject(Map<String, Object> object, String attr, List<Object> subArray) {
                object.put(attr, subArray);
            }

            @Override
            public void addStringToObject(Map<String, Object> object, String attr, String value) {
                object.put(attr, value);
            }

            @Override
            public void addBooleanToObject(Map<String, Object> object, String attr, Boolean value) {
                object.put(attr, value);
            }

            @Override
            public void addNullToObject(Map<String, Object> object, String attr) {
                object.put(attr, null);
            }

            @Override
            public void addNumberToObject(Map<String, Object> object, String attr, JsonNumber value) {
                object.put(attr, JsonNumberConverterIntFloat.REF.convert(value));
            }

            @Override
            public void addObjectToArray(List<Object> array, Map<String, Object> subObject) {
                array.add(subObject);
            }

            @Override
            public void addArrayToArray(List<Object> array, List<Object> subArray) {
                array.add(subArray);
            }

            @Override
            public void addStringToArray(List<Object> array, String value) {
                array.add(value);
            }

            @Override
            public void addBooleanToArray(List<Object> array, Boolean value) {
                array.add(value);
            }

            @Override
            public void addNullToArray(List<Object> array) {
                array.add(null);
            }

            @Override
            public void addNumberToArray(List<Object> array, JsonNumber value) {
                array.add(JsonNumberConverterIntFloat.REF.convert(value));
            }
        };

        JsonParser jsonParser = new JsonParser(customJsonBuilder);
        Map<String, Object> map = jsonParser.parse("{ \"array\" : [1,2,3] }");

        assertEquals(1, map.size());
        assertEquals(ArrayList.class, map.get("array").getClass());
        assertEquals(3, ((List) map.get("array")).size());
        assertEquals(1, ((List) map.get("array")).get(0));
        assertEquals(2, ((List) map.get("array")).get(1));
        assertEquals(3, ((List) map.get("array")).get(2));
    }
}
