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
import org.cuberact.json.JsonArray;
import org.cuberact.json.JsonObject;
import org.cuberact.json.output.JsonOutput;
import org.cuberact.json.parser.JsonParser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public class CustomJsonFormatter {

    @Test
    public void formatter() {
        JsonFormatter customJsonFormatter = new JsonFormatter() {
            @Override
            public boolean writeObjectStart(JsonObject json, JsonOutput<?> output) {
                output.write("OBJ (");
                return true;
            }

            @Override
            public void writeObjectEnd(JsonObject json, JsonOutput<?> output) {
                output.write(")");
            }

            @Override
            public boolean writeArrayStart(JsonArray json, JsonOutput<?> output) {
                output.write("ARR <");
                return true;
            }

            @Override
            public void writeArrayEnd(JsonArray json, JsonOutput<?> output) {
                output.write(">");
            }

            @Override
            public void writeObjectColon(JsonObject json, JsonOutput<?> output) {
                output.write(" = ");
            }

            @Override
            public void writeObjectComma(JsonObject json, JsonOutput<?> output) {
                output.write(" | ");
            }

            @Override
            public void writeArrayComma(JsonArray json, JsonOutput<?> output) {
                output.write(" / ");
            }

            @Override
            public void writeObjectAttr(CharSequence attr, JsonObject json, JsonOutput<?> output) {
                output.write("-" + attr + "-");
            }

            @Override
            public void writeObjectValue(Object value, JsonObject json, JsonOutput<?> output) {
                output.write(":");
                if (value instanceof Json) {
                    ((Json) value).toOutput(this, output);
                } else {
                    output.write(String.valueOf(value));
                }
                output.write(":");
            }

            @Override
            public void writeArrayValue(Object value, JsonArray json, JsonOutput<?> output) {
                output.write("#");
                if (value instanceof Json) {
                    ((Json) value).toOutput(this, output);
                } else {
                    output.write(String.valueOf(value));
                }
                output.write("#");
            }
        };
        String jsonAsString = "{'name':'Bob', 'age':15, 'marks':[1,2,{'hello':'world'}]}"
                .replace('\'', '"');
        Json json = new JsonParser().parse(jsonAsString);
        String notJsonString = json.toString(customJsonFormatter);
        String expected = "OBJ (-name- = :Bob: | -age- = :15: | -marks- = :ARR <#1# / #2# / #OBJ (-hello- = :world:)#>:)";
        assertEquals(expected, notJsonString);
    }
}
