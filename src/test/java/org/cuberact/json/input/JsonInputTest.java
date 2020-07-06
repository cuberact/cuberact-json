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

package org.cuberact.json.input;

import org.cuberact.json.Json;
import org.cuberact.json.JsonException;
import org.cuberact.json.formatter.JsonFormatter;
import org.cuberact.json.parser.JsonParser;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.cuberact.json.input.JsonInput.END_OF_INPUT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public class JsonInputTest {

    private final String jsonAsString = "{\n" +
            "  \"rect\": [486,\"\\u0048\\u0065\\u006c\\u006C\\u006FWorld\",{\"data\" : \"\\u011B\\u0161\\u010D\\u0159\\u017E\\u00FD\\u00E1\\u00ED\\u00E9\"},-23.54],\n" +
            "  \"perspectiveSelector\": {\n" +
            "    \"perspectives\": [ true, false],\n" +
            "    \"selected\": null,\n" +
            "    \"some\": [1,2,3.2]\n" +
            "  }\n" +
            "}";
    private final String expected = "{\"rect\":[486,\"HelloWorld\",{\"data\":\"ěščřžýáíé\"},-23.54],\"perspectiveSelector\":{\"perspectives\":[true,false],\"selected\":null,\"some\":[1,2,3.2]}}";

    @Test
    public void jsonInputCharSequence() {
        Json json = new JsonParser().parse(jsonAsString);
        assertEquals(expected, json.toString(JsonFormatter.PACKED()));
    }

    @Test
    public void jsonInputCharArray() {
        Json json = new JsonParser().parse(jsonAsString.toCharArray());
        assertEquals(expected, json.toString(JsonFormatter.PACKED()));
    }

    @Test
    public void jsonInputReader() {
        Json json = new JsonParser().parse(new StringReader(jsonAsString));
        assertEquals(expected, json.toString(JsonFormatter.PACKED()));
    }

    @Test
    public void jsonInputCharSequenceEndOfInput() {
        readInputWithAsserts(new JsonInputCharSequence("123"));
    }

    @Test
    public void jsonInputCharArrayEndOfInput() {
        readInputWithAsserts(new JsonInputCharArray("123".toCharArray()));
    }

    @Test
    public void jsonInputReaderEndOfInput() {
        readInputWithAsserts(new JsonInputReader(new StringReader("123")));
    }

    private void readInputWithAsserts(JsonInput input) {
        assertEquals('1', input.nextChar());
        assertEquals(1, input.position());
        assertEquals('2', input.nextChar());
        assertEquals(2, input.position());
        assertEquals('3', input.nextChar());
        assertEquals(3, input.position());
        assertEquals(END_OF_INPUT, input.nextChar());
        assertEquals(3, input.position());
        assertEquals(END_OF_INPUT, input.nextChar());
        assertEquals(3, input.position());
        assertEquals(END_OF_INPUT, input.nextChar());
        assertEquals(3, input.position());
    }

    @Test
    public void jsonInputReaderException() {
        assertThrows(JsonException.class, () -> {
            JsonInput input = new JsonInputReader(new StringReader("123") {
                @Override
                public int read() throws IOException {
                    throw new IOException("simulated exception");
                }
            });
            input.nextChar();
        });
    }
}
