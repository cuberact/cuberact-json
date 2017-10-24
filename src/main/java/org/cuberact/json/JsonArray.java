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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Json array
 *
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public class JsonArray extends Json {

    private static final long serialVersionUID = 1L;

    private final List<Object> data = new ArrayList<>();

    @Override
    public JsonType type() {
        return JsonType.JSON_ARRAY;
    }

    public Object get(int index) {
        try {
            return data.get(index);
        } catch (Throwable t) {
            throw new JsonException(t);
        }
    }

    public JsonObject getObj(int index) {
        return getInternal(index, JsonObject.class);
    }

    public JsonArray getArr(int index) {
        return getInternal(index, JsonArray.class);
    }

    public String getString(int index) {
        return getInternal(index, String.class);
    }

    public Integer getInt(int index) {
        return getInternal(index, Integer.class);
    }

    public Long getLong(int index) {
        return getInternal(index, Long.class);
    }

    public Float getFloat(int index) {
        return getInternal(index, Float.class);
    }

    public Double getDouble(int index) {
        return getInternal(index, Double.class);
    }

    public Boolean getBoolean(int index) {
        return getInternal(index, Boolean.class);
    }

    public JsonArray add(Object value) {
        try {
            data.add(value);
        } catch (Throwable t) {
            throw new JsonException(t);
        }
        return this;
    }

    public JsonArray add(int index, Object value) {
        try {
            data.add(index, value);
        } catch (Throwable t) {
            throw new JsonException(t);
        }
        return this;
    }

    public JsonArray set(int index, Object value) {
        try {
            data.set(index, value);
        } catch (Throwable t) {
            throw new JsonException(t);
        }
        return this;
    }

    public JsonArray remove(int index) {
        try {
            data.remove(index);
        } catch (Throwable t) {
            throw new JsonException(t);
        }
        return this;
    }

    public JsonArray remove(Object value) {
        try {
            data.remove(value);
        } catch (Throwable t) {
            throw new JsonException(t);
        }
        return this;
    }

    public int indexOf(Object value) {
        try {
            return data.indexOf(value);
        } catch (Throwable t) {
            throw new JsonException(t);
        }
    }

    public boolean isNotNull(int index) {
        return get(index) != null;
    }

    public boolean contains(Object value) {
        try {
            return data.contains(value);
        } catch (Throwable t) {
            throw new JsonException(t);
        }
    }

    public int size() {
        return data.size();
    }

    public List<Object> list() {
        return data;
    }

    public <E> List<E> listOf(Class<E> type) {
        return streamOf(type).collect(Collectors.toList());
    }

    public Iterable<Object> iterable() {
        return list();
    }

    public <E> Iterable<E> iterableOf(Class<E> type) {
        return listOf(type);
    }

    public Stream<Object> stream() {
        return data.stream();
    }

    @SuppressWarnings("unchecked")
    public <E> Stream<E> streamOf(Class<E> type) {
        return (Stream<E>) data.stream()
                .filter(value -> value != null && type.isAssignableFrom(value.getClass()));
    }

    @Override
    public void toOutput(JsonFormatter formatter, JsonOutput output) {
        formatter.appendJsonArrayStart(output);
        boolean addComma = false;
        for (Object value : data) {
            if (addComma) {
                formatter.appendJsonArrayComma(output);
            }
            addComma = true;
            formatter.appendJsonArrayValue(value, output);
        }
        formatter.appendJsonArrayEnd(output);
    }

    @SuppressWarnings("unchecked")
    private <E> E getInternal(int index, Class<E> type) {
        try {
            Object value = data.get(index);
            if (value == null) {
                return null;
            }
            if (!type.isAssignableFrom(value.getClass())) {
                throw new JsonException("Wrong value[" + index + "] type. Expected " + type.getName() + ", but is " + value.getClass().getName());
            }
            return (E) value;
        } catch (Throwable t) {
            throw new JsonException(t);
        }
    }
}
