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

package org.cuberact.json.builder;

import org.cuberact.json.JsonArray;
import org.cuberact.json.JsonObject;
import org.cuberact.json.number.JsonNumber;
import org.cuberact.json.number.JsonNumberConverter;
import org.cuberact.json.number.JsonNumberConverterLongDouble;

import java.util.Objects;

/**
 * Default builder which build cuberact json tree structure - {@link JsonObject} and {@link JsonArray}
 *
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public class JsonBuilderTree implements JsonBuilder {

    public static JsonBuilderTree DEFAULT = new JsonBuilderTree(JsonNumberConverterLongDouble.REF);

    private final JsonNumberConverter jsonNumberConverter;

    public JsonBuilderTree(JsonNumberConverter jsonNumberConverter) {
        this.jsonNumberConverter = Objects.requireNonNull(jsonNumberConverter, "JsonNumberConverter");
    }

    /**
     * @return JsonObject - {@link JsonObject}
     */
    @Override
    public Object createObject() {
        return new JsonObject();
    }

    /**
     * @return JsonArray - {@link JsonArray}
     */
    @Override
    public Object createArray() {
        return new JsonArray();
    }

    @Override
    public void addObjectToObject(Object object, String attr, Object subObject) {
        ((JsonObject) object).add(attr, subObject);
    }

    @Override
    public void addArrayToObject(Object object, String attr, Object subArray) {
        ((JsonObject) object).add(attr, subArray);
    }

    @Override
    public void addStringToObject(Object object, String attr, String value) {
        ((JsonObject) object).add(attr, value);
    }

    @Override
    public void addBooleanToObject(Object object, String attr, Boolean value) {
        ((JsonObject) object).add(attr, value);
    }

    @Override
    public void addNullToObject(Object object, String attr) {
        ((JsonObject) object).add(attr, null);
    }

    @Override
    public void addNumberToObject(Object object, String attr, JsonNumber value) {
        ((JsonObject) object).add(attr, jsonNumberConverter.convert(value));
    }

    @Override
    public void addObjectToArray(Object array, Object subObject) {
        ((JsonArray) array).add(subObject);
    }

    @Override
    public void addArrayToArray(Object array, Object subArray) {
        ((JsonArray) array).add(subArray);
    }

    @Override
    public void addStringToArray(Object array, String value) {
        ((JsonArray) array).add(value);
    }

    @Override
    public void addBooleanToArray(Object array, Boolean value) {
        ((JsonArray) array).add(value);
    }

    @Override
    public void addNullToArray(Object array) {
        ((JsonArray) array).add(null);
    }

    @Override
    public void addNumberToArray(Object array, JsonNumber value) {
        ((JsonArray) array).add(jsonNumberConverter.convert(value));
    }
}
