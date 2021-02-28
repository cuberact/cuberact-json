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

import java.io.Serial;
import java.math.BigDecimal;
import java.math.BigInteger;
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

    @Serial
    private static final long serialVersionUID = 1L;

    private final List<Object> data = new ArrayList<>();

    @Override
    public JsonType type() {
        return JsonType.ARRAY;
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

    public BigInteger getBigInt(int index) {
        return getInternal(index, BigInteger.class);
    }

    public BigDecimal getBigDecimal(int index) {
        return getInternal(index, BigDecimal.class);
    }

    public Boolean getBoolean(int index) {
        return getInternal(index, Boolean.class);
    }

    private boolean outOfRange(int index){
        return index < 0 || index > data.size()-1;
    }

    public JsonObject getObj(int index, JsonObject ifNotExists) {
        if (outOfRange(index)) return ifNotExists;
        return getInternal(index, JsonObject.class);
    }

    public JsonArray getArr(int index, JsonArray ifNotExists) {
        if (outOfRange(index)) return ifNotExists;
        return getInternal(index, JsonArray.class);
    }

    public String getString(int index, String ifNotExists) {
        if (outOfRange(index)) return ifNotExists;
        return getInternal(index, String.class);
    }

    public Integer getInt(int index, Integer ifNotExists) {
        if (outOfRange(index)) return ifNotExists;
        return getInternal(index, Integer.class);
    }

    public Long getLong(int index, Long ifNotExists) {
        if (outOfRange(index)) return ifNotExists;
        return getInternal(index, Long.class);
    }

    public Float getFloat(int index, Float ifNotExists) {
        if (outOfRange(index)) return ifNotExists;
        return getInternal(index, Float.class);
    }

    public Double getDouble(int index, Double ifNotExists) {
        if (outOfRange(index)) return ifNotExists;
        return getInternal(index, Double.class);
    }

    public BigInteger getBigInt(int index, BigInteger ifNotExists) {
        if (outOfRange(index)) return ifNotExists;
        return getInternal(index, BigInteger.class);
    }

    public BigDecimal getBigDecimal(int index, BigDecimal ifNotExists) {
        if (outOfRange(index)) return ifNotExists;
        return getInternal(index, BigDecimal.class);
    }

    public Boolean getBoolean(int index, Boolean ifNotExists) {
        if (outOfRange(index)) return ifNotExists;
        return getInternal(index, Boolean.class);
    }

    public JsonArray add(Object value) {
        data.add(value);
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
        data.remove(value);
        return this;
    }

    public int indexOf(Object value) {
        return data.indexOf(value);
    }

    public boolean isNotNull(int index) {
        return get(index) != null;
    }

    public boolean contains(Object value) {
        return data.contains(value);
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
    public void toOutput(JsonFormatter formatter, JsonOutput<?> output) {
        if (formatter.writeArrayStart(this, output)) {
            for (int i = 0, len = data.size(); i < len; i++) {
                if (i != 0) {
                    formatter.writeArrayComma(this, output);
                }
                formatter.writeArrayValue(data.get(i),this,  output);
            }
            formatter.writeArrayEnd(this, output);
        }
    }

    private <E> E getInternal(int index, Class<E> type) {
        try {
            Object value = data.get(index);
            return getValueAsType(value, type);
        } catch (Throwable t) {
            throw new JsonException(t);
        }
    }
}
