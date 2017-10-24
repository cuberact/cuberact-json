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

package org.cuberact.json.parser;

import org.cuberact.json.formatter.JsonFormatter;
import org.junit.Test;
import org.cuberact.json.Json;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

/**
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */

public class JsonParserTest {

    @Test
    public void parseExample1() {
        String jsonAsString = "{\n" +
                "  \"rect\": [486,\"\\u0048\\u0065\\u006c\\u006C\\u006FWorld\",{\"data\" : \"\\u011B\\u0161\\u010D\\u0159\\u017E\\u00FD\\u00E1\\u00ED\\u00E9\"},-23.54],\n" +
                "  \"perspectiveSelector\": {\n" +
                "    \"perspectives\": [ true, false],\n" +
                "    \"selected\": null,\n" +
                "    \"some\": [1,2,3.2]\n" +
                "  }\n" +
                "}";
        String expected = "{\"rect\":[486,\"HelloWorld\",{\"data\":\"ěščřžýáíé\"},-23.54],\"perspectiveSelector\":{\"perspectives\":[true,false],\"selected\":null,\"some\":[1,2,3.2]}}";
        Json jsonFromString = parseFromString(jsonAsString);
        Json jsonFromReader = parseFromReader(jsonAsString);
        assertEquals(expected, jsonFromString.toString(JsonFormatter.PACKED()));
        assertEquals(expected, jsonFromReader.toString(JsonFormatter.PACKED()));
    }

    @Test
    public void parseExample2() {
        String jsonAsString = "{\n" +
                "\t\"id\" : \"abcdef1234567890\",\n" +
                "\t\"apiKey\" : \"abcdef-ghijkl\",\n" +
                "\t\"name\" : \"Main_key\",\n" +
                "\t\"validFrom\" : 1505858400000,\n" +
                "\t\"softQuota\" : 10000000,\n" +
                "\t\"hardQuota\" : 10.12345,\n" +
                "\t\"useSignature\" : null,\n" +
                "\t\"state\" : \"ENABLED\",\n" +
                "\t\"mocking\" : true,\n" +
                "\t\"clientIpUsed\" : false,\n" +
                "\t\"clientIpEnforced\" : false\n" +
                "}";
        String expected = "{\"id\":\"abcdef1234567890\",\"apiKey\":\"abcdef-ghijkl\",\"name\":\"Main_key\",\"validFrom\":1505858400000,\"softQuota\":10000000,\"hardQuota\":10.12345,\"useSignature\":null,\"state\":\"ENABLED\",\"mocking\":true,\"clientIpUsed\":false,\"clientIpEnforced\":false}";
        Json jsonFromString = parseFromString(jsonAsString);
        Json jsonFromReader = parseFromReader(jsonAsString);
        assertEquals(expected, jsonFromString.toString(JsonFormatter.PACKED()));
        assertEquals(expected, jsonFromReader.toString(JsonFormatter.PACKED()));
    }

    @Test
    public void parseExample3() {
        String jsonAsString = "[1,\n" +
                "[2, \n" +
                "[3, \n" +
                "[4, \n" +
                "[5], \n" +
                "4.4], \n" +
                "3.3], \n" +
                "2.2], \n" +
                "1.1]";
        String expected = "[1,[2,[3,[4,[5],4.4],3.3],2.2],1.1]";
        Json jsonFromString = parseFromString(jsonAsString);
        Json jsonFromReader = parseFromReader(jsonAsString);
        assertEquals(expected, jsonFromString.toString(JsonFormatter.PACKED()));
        assertEquals(expected, jsonFromReader.toString(JsonFormatter.PACKED()));
    }

    @Test
    public void parseExample4() {
        String jsonAsString = "[[[[[[[[[[1]]]]]]]]]]";
        Json jsonFromString = parseFromString(jsonAsString);
        Json jsonFromReader = parseFromReader(jsonAsString);
        assertEquals(jsonAsString, jsonFromString.toString(JsonFormatter.PACKED()));
        assertEquals(jsonAsString, jsonFromReader.toString(JsonFormatter.PACKED()));
    }

    @Test
    public void parseExample5() {
        String jsonAsString = "[1,-2,\"text\",true,false,null,1.1,-1.1,{},[]]";
        Json jsonFromString = parseFromString(jsonAsString);
        Json jsonFromReader = parseFromReader(jsonAsString);
        assertEquals(jsonAsString, jsonFromString.toString(JsonFormatter.PACKED()));
        assertEquals(jsonAsString, jsonFromReader.toString(JsonFormatter.PACKED()));
    }

    @Test
    public void parseExample6() {
        String jsonAsString = "[1.1, -1.1, " +
                "2.2e10, 2.2E10, -2.2e10, -2.2E-10, 2.2e-10, 2.2E-10, -2.2e-10, -2.2E-10, " +
                "12345.12345e8, 12345.12345e-8, -12345.12345e8, -12345.12345e-8" +
                "]";
        String expected = "[1.1,-1.1,2.2E10,2.2E10,-2.2E10,-2.2E-10,2.2E-10,2.2E-10,-2.2E-10,-2.2E-10,1.234512345E12,1.234512345E-4,-1.234512345E12,-1.234512345E-4]";
        Json jsonFromString = parseFromString(jsonAsString);
        Json jsonFromReader = parseFromReader(jsonAsString);
        assertEquals(expected, jsonFromString.toString(JsonFormatter.PACKED()));
        assertEquals(expected, jsonFromReader.toString(JsonFormatter.PACKED()));
    }

    @Test
    public void parseExample7() {
        String jsonAsString = "{\"1\":{\"2\":{\"3\":{\"4\":{\"5\":{\"6\":{\"7\":{\"8\":{\"9\":{\"name\":\"jack\"}}}}},\"age\":15}}}}}";
        Json jsonFromString = parseFromString(jsonAsString);
        Json jsonFromReader = parseFromReader(jsonAsString);
        assertEquals(jsonAsString, jsonFromString.toString(JsonFormatter.PACKED()));
        assertEquals(jsonAsString, jsonFromReader.toString(JsonFormatter.PACKED()));
    }

    private Json parseFromString(String jsonAsString) {
        return new JsonParser().parse(jsonAsString);
    }

    private Json parseFromReader(String jsonAsString) {
        InputStream stream = new ByteArrayInputStream(jsonAsString.getBytes(StandardCharsets.UTF_8));
        Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
        return new JsonParser().parse(reader);
    }
}
