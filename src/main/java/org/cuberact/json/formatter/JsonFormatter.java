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
import org.cuberact.json.output.JsonOutput;

/**
 * Format json output {@link Json#toString(JsonFormatter)} or {@link Json#toOutput(JsonFormatter, JsonOutput)}
 *
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public interface JsonFormatter {

    static JsonFormatter PACKED() {
        return JsonFormatterPacked.REF;
    }

    static JsonFormatter PRETTY() {
        return new JsonFormatterPretty();
    }

    void appendJsonObjectStart(JsonOutput output);

    void appendJsonObjectEnd(JsonOutput output);

    void appendJsonArrayStart(JsonOutput output);

    void appendJsonArrayEnd(JsonOutput output);

    void appendJsonObjectColon(JsonOutput output);

    void appendJsonObjectComma(JsonOutput output);

    void appendJsonArrayComma(JsonOutput output);

    void appendJsonObjectAttribute(CharSequence attribute, JsonOutput output);

    void appendJsonObjectValue(Object value, JsonOutput output);

    void appendJsonArrayValue(Object value, JsonOutput output);
}
