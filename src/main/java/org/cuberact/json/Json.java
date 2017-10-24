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

import org.cuberact.json.formatter.JsonFormatter;
import org.cuberact.json.output.JsonOutput;
import org.cuberact.json.output.JsonOutputStringBuilder;

import java.io.Serializable;
import java.util.Objects;

/**
 * Parent of {@link JsonObject} and {@link JsonArray}
 *
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public abstract class Json implements Serializable {

    private static final long serialVersionUID = 1L;

    public abstract JsonType type();

    public final String toString() {
        return toString(JsonFormatter.PRETTY());
    }

    public final String toString(JsonFormatter formatter) {
        JsonOutputStringBuilder writer = new JsonOutputStringBuilder();
        toOutput(Objects.requireNonNull(formatter, "formatter"), writer);
        return writer.result().toString();
    }

    public abstract void toOutput(JsonFormatter formatter, JsonOutput output);
}
