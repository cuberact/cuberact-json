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

    @Serial
    private static final long serialVersionUID = 1L;

    private final Map<String, Object> data = new LinkedHashMap<>();

    @Override
    public JsonType type() {
        return JsonType.OBJECT;
    }

    public Object get(String attr) {
        return data.get(attr);
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

    public BigInteger getBigInt(String attr) {
        return getInternal(attr, BigInteger.class);
    }

    public BigDecimal getBigDecimal(String attr) {
        return getInternal(attr, BigDecimal.class);
    }

    public Boolean getBoolean(String attr) {
        return getInternal(attr, Boolean.class);
    }

    public JsonObject getObj(String attr, JsonObject ifNotExists) {
        if (!data.containsKey(attr)) return ifNotExists;
        return getInternal(attr, JsonObject.class);
    }

    public JsonArray getArr(String attr, JsonArray ifNotExists) {
        if (!data.containsKey(attr)) return ifNotExists;
        return getInternal(attr, JsonArray.class);
    }

    public String getString(String attr, String ifNotExists) {
        if (!data.containsKey(attr)) return ifNotExists;
        return getInternal(attr, String.class);
    }

    public Integer getInt(String attr, Integer ifNotExists) {
        if (!data.containsKey(attr)) return ifNotExists;
        return getInternal(attr, Integer.class);
    }

    public Long getLong(String attr, Long ifNotExists) {
        if (!data.containsKey(attr)) return ifNotExists;
        return getInternal(attr, Long.class);
    }

    public Float getFloat(String attr, Float ifNotExists) {
        if (!data.containsKey(attr)) return ifNotExists;
        return getInternal(attr, Float.class);
    }

    public Double getDouble(String attr, Double ifNotExists) {
        if (!data.containsKey(attr)) return ifNotExists;
        return getInternal(attr, Double.class);
    }

    public BigInteger getBigInt(String attr, BigInteger ifNotExists) {
        if (!data.containsKey(attr)) return ifNotExists;
        return getInternal(attr, BigInteger.class);
    }

    public BigDecimal getBigDecimal(String attr, BigDecimal ifNotExists) {
        if (!data.containsKey(attr)) return ifNotExists;
        return getInternal(attr, BigDecimal.class);
    }

    public Boolean getBoolean(String attr, Boolean ifNotExists) {
        if (!data.containsKey(attr)) return ifNotExists;
        return getInternal(attr, Boolean.class);
    }
    
    public JsonObject add(String attr, Object value) {
        data.put(attr, value);
        return this;
    }

    public JsonObject remove(String attr) {
        data.remove(attr);
        return this;
    }

    public boolean isNotNull(String attr) {
        return data.get(attr) != null;
    }

    public boolean contains(String attr) {
        return data.containsKey(attr);
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
    public void toOutput(JsonFormatter formatter, JsonOutput<?> output) {
        if (formatter.writeObjectStart(this, output)) {
            boolean addComma = false;
            for (Entry<String, Object> entry : data.entrySet()) {
                if (addComma) {
                    formatter.writeObjectComma(this,output);
                } else {
                    addComma = true;
                }
                formatter.writeObjectAttr(entry.getKey(),this, output);
                formatter.writeObjectColon(this,output);
                formatter.writeObjectValue(entry.getValue(),this, output);
            }
            formatter.writeObjectEnd(this,output);
        }
    }

    private <E> E getInternal(String attr, Class<E> type) {
        Object value = data.get(attr);
        return getValueAsType(value, type);
    }
}
