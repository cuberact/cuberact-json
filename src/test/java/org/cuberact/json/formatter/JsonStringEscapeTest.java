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

package org.cuberact.json.formatter;

import org.cuberact.json.Json;
import org.cuberact.json.optimize.JsonEscape;
import org.cuberact.json.output.JsonOutputStringBuilder;
import org.cuberact.json.parser.JsonParser;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public class JsonStringEscapeTest {

    @Test
    public void jsonWithEscapedChars() {
        String jsonAsString = "{\"\\\"\b\r\f\n\t\\\\\":\"\\\"\b\r\f\n\\\\\"}";
        Json json = new JsonParser().parse(jsonAsString);
        String expected = "{\"\\\"\\b\\r\\f\\n\\t\\\\\":\"\\\"\\b\\r\\f\\n\\\\\"}";
        Assert.assertEquals(expected, json.toString(JsonFormatter.PACKED()));
    }

    @Test
    public void jsonWithLowUnicode() {
        String jsonAsString = "{\"\\u000e\\u0010\":\"\\u000f\"}";
        Json json = new JsonParser().parse(jsonAsString);
        String expected = "{\"\\u000e\\u0010\":\"\\u000f\"}";
        Assert.assertEquals(expected, json.toString(JsonFormatter.PACKED()));
    }

    @Test
    public void escapeExample1() {
        String value = "\"\\\\hello\b\r\n\f\"/";
        String result = escapeByFormatter(value);
        assertEquals("\\\"\\\\\\\\hello\\b\\r\\n\\f\\\"/", result);
    }

    @Test
    public void escapeExample2() {
        String value = "'hello'";
        String result = escapeByFormatter(value);
        assertEquals("'hello'", result);
    }

    @Test
    public void escapeExample3() {
        String value = "\"\"\"\"\"";
        String result = escapeByFormatter(value);
        assertEquals("\\\"\\\"\\\"\\\"\\\"", result);
    }

    private String escapeByFormatter(String value) {
        JsonOutputStringBuilder output = new JsonOutputStringBuilder();
        JsonEscape.escape(value, output);
        return output.getResult().toString();
    }
}
