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
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

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
        return writer.getResult().toString();
    }

    public abstract void toOutput(JsonFormatter formatter, JsonOutput output);


    public final void convertJsonNumbers(Function<JsonNumber, Object> converter, boolean callOnChildren) {
        if (this instanceof JsonObject) {
            JsonObject jo = (JsonObject) this;
            for (Map.Entry<String, Object> entry : jo.iterable()) {
                if (entry.getValue() instanceof JsonNumber) {
                    entry.setValue(converter.apply((JsonNumber) entry.getValue()));
                } else if (callOnChildren && entry.getValue() instanceof Json) {
                    ((Json) entry.getValue()).convertJsonNumbers(converter, true);
                }
            }
        } else {
            JsonArray ja = (JsonArray) this;
            for (int i = 0; i < ja.size(); i++) {
                Object o = ja.get(i);
                if (o instanceof JsonNumber) {
                    ja.set(i, converter.apply((JsonNumber) o));
                } else if (callOnChildren && o instanceof Json) {
                    ((Json) o).convertJsonNumbers(converter, true);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    <E> E getValueAsType(Object value, Class<E> type) {
        if (value == null) {
            return null;
        }
        if (type.isAssignableFrom(value.getClass())) {
            return (E) value;
        }
        if (value instanceof JsonNumber) {
            return (E) ((JsonNumber) value).asNumber((Class<Number>) type);
        }
        throw new JsonException("Wrong value type. Expected " + type.getName() + ", but actual is " + value.getClass().getName());
    }
}
