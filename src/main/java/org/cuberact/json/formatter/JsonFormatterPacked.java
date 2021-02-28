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

import org.cuberact.json.JsonArray;
import org.cuberact.json.JsonObject;
import org.cuberact.json.output.JsonOutput;

/**
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public final class JsonFormatterPacked extends JsonFormatterBase {

    static final JsonFormatterPacked REF = new JsonFormatterPacked();

    private JsonFormatterPacked() {
        // use JsonFormatter.PACKED()
    }

    @Override
    public boolean writeObjectStart(JsonObject json, JsonOutput<?> output) {
        output.write(getObjectStart());
        return true;
    }

    @Override
    public void writeObjectAttr(CharSequence attr, JsonObject json, JsonOutput<?> output) {
        writeString(attr, output);
    }

    @Override
    public void writeObjectColon(JsonObject json, JsonOutput<?> output) {
        output.write(getObjectColon());
    }

    @Override
    public void writeObjectValue(Object value, JsonObject json, JsonOutput<?> output) {
        writeValue(value, output);
    }

    @Override
    public void writeObjectComma(JsonObject json, JsonOutput<?> output) {
        output.write(getObjectComma());
    }

    @Override
    public void writeObjectEnd(JsonObject json, JsonOutput<?> output) {
        output.write(getObjectEnd());
    }

    @Override
    public boolean writeArrayStart(JsonArray json, JsonOutput<?> output) {
        output.write(getArrayStart());
        return true;
    }

    @Override
    public void writeArrayValue(Object value, JsonArray json, JsonOutput<?> output) {
        writeValue(value, output);
    }

    @Override
    public void writeArrayComma(JsonArray json, JsonOutput<?> output) {
        output.write(getArrayComma());
    }

    @Override
    public void writeArrayEnd(JsonArray json, JsonOutput<?> output) {
        output.write(getArrayEnd());
    }
}
