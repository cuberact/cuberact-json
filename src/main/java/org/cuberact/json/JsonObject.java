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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * Json object
 *
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public class JsonObject extends Json {

    private static final long serialVersionUID = 1L;

    private final Map<String, Object> data = new LinkedHashMap<>();

    @Override
    public JsonType type() {
        return JsonType.JSON_OBJECT;
    }

    public Object get(String attr) {
        try {
            return data.get(attr);
        } catch (Throwable t) {
            throw new JsonException(t);
        }
    }

    public JsonObject getObj(String attr) {
        return getInternal(attr, JsonObject.class);
    }

    public JsonArray getArr(String attr) {
        return getInternal(attr, JsonArray.class);
    }

    public String getString(String attr) {
        return getInternal(attr, String.class);
    }

    public Integer getInt(String attr) {
        return getInternal(attr, Integer.class);
    }

    public Long getLong(String attr) {
        return getInternal(attr, Long.class);
    }

    public Float getFloat(String attr) {
        return getInternal(attr, Float.class);
    }

    public Double getDouble(String attr) {
        return getInternal(attr, Double.class);
    }

    public Boolean getBoolean(String attr) {
        return getInternal(attr, Boolean.class);
    }

    public JsonObject add(String attr, Object value) {
        try {
            data.put(attr, value);
        } catch (Throwable t) {
            throw new JsonException(t);
        }
        return this;
    }

    public JsonObject remove(String attr) {
        try {
            data.remove(attr);
        } catch (Throwable t) {
            throw new JsonException(t);
        }
        return this;
    }

    public boolean isNotNull(String attr) {
        try {
            return data.get(attr) != null;
        } catch (Throwable t) {
            throw new JsonException(t);
        }
    }

    public boolean contains(String attr) {
        try {
            return data.containsKey(attr);
        } catch (Throwable t) {
            throw new JsonException(t);
        }
    }

    public int size() {
        return data.size();
    }

    public Map<String, Object> map() {
        return data;
    }

    public <E> Map<String, E> mapOf(Class<E> type) {
        return streamOf(type).collect(toMap(Entry::getKey, Entry::getValue));
    }

    public Iterable<Entry<String, Object>> iterable() {
        return data.entrySet();
    }

    public <E> Iterable<Entry<String, E>> iterableOf(Class<E> type) {
        return streamOf(type).collect(toList());
    }

    public Stream<Entry<String, Object>> stream() {
        return data.entrySet().stream();
    }

    @SuppressWarnings("unchecked")
    public <E> Stream<Entry<String, E>> streamOf(Class<E> type) {
        return data.entrySet().stream()
                .filter(entry -> entry.getValue() != null && type.isAssignableFrom(entry.getValue().getClass()))
                .map(value -> (Entry<String, E>) value);
    }

    @Override
    public void toOutput(JsonFormatter formatter, JsonOutput output) {
        formatter.appendJsonObjectStart(output);
        boolean addComma = false;
        for (Entry<String, Object> entry : data.entrySet()) {
            if (addComma) {
                formatter.appendJsonObjectComma(output);
            }
            addComma = true;
            formatter.appendJsonObjectAttribute(entry.getKey(), output);
            formatter.appendJsonObjectColon(output);
            formatter.appendJsonObjectValue(entry.getValue(), output);
        }
        formatter.appendJsonObjectEnd(output);
    }

    @SuppressWarnings("unchecked")
    private <E> E getInternal(String attr, Class<E> type) {
        try {
            Object value = data.get(attr);
            if (value == null) {
                return null;
            }
            if (!type.isAssignableFrom(value.getClass())) {
                throw new JsonException("Wrong value type for attr\"" + attr + "\". Expected " + type.getName() + ", but is " + value.getClass().getName());
            }
            return (E) value;
        } catch (Throwable t) {
            throw new JsonException(t);
        }
    }
}
