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
import org.cuberact.json.JsonNumber;
import org.cuberact.json.output.JsonOutput;

import static org.cuberact.json.optimize.JsonEscape.escape;

/**
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public abstract class JsonFormatterBase implements JsonFormatter {

    @Override
    public void writeObjectAttr(CharSequence attr, JsonOutput output) {
        writeString(attr, output);
    }

    @Override
    public void writeObjectValue(Object value, JsonOutput output) {
        if (value instanceof Json) {
            ((Json) value).toOutput(this, output);
        } else if (value instanceof JsonNumber) {
            output.write(value.toString());
        } else if (value instanceof CharSequence) {
            writeString((CharSequence) value, output);
        } else if (value instanceof Double || value instanceof Float) {
            output.write(String.valueOf(value).replace(',', '.'));
        } else {
            output.write(String.valueOf(value));
        }
    }

    protected void writeString(CharSequence value, JsonOutput output) {
        output.write("\"");
        escape(value, output);
        output.write("\"");
    }

    @Override
    public void writeArrayValue(Object value, JsonOutput output) {
        writeObjectValue(value, output);
    }
}
