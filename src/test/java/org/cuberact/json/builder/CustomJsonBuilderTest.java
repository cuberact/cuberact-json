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

import org.cuberact.json.JsonNumber;
import org.cuberact.json.parser.JsonParser;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public class CustomJsonBuilderTest {

    @Test
    public void buildToMapAndList() {
        JsonBuilder customJsonBuilder = new JsonBuilderBase<Map<String, Object>, List<Object>>() {

            @Override
            public Map<String, Object> createObject() {
                return new LinkedHashMap<>();
            }

            @Override
            public List<Object> createArray() {
                return new ArrayList<>();
            }

            @Override
            protected void addToObject(Map<String, Object> object, String attr, Object value) {
                object.put(attr, value);
            }

            @Override
            protected void addToArray(List<Object> array, Object value) {
                if (value instanceof JsonNumber) {
                    array.add(((JsonNumber) value).asInt());
                } else {
                    array.add(value);
                }
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
