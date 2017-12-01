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
public class JsonBuilderTree implements JsonBuilder<JsonObject, JsonArray> {

    public static JsonBuilderTree DEFAULT = new JsonBuilderTree(JsonNumberConverterLongDouble.REF);

    private final JsonNumberConverter jsonNumberConverter;

    public JsonBuilderTree(JsonNumberConverter jsonNumberConverter) {
        this.jsonNumberConverter = Objects.requireNonNull(jsonNumberConverter, "JsonNumberConverter");
    }

    @Override
    public JsonObject createObject() {
        return new JsonObject();
    }

    @Override
    public JsonArray createArray() {
        return new JsonArray();
    }

    @Override
    public void addObjectToObject(JsonObject object, String attr, JsonObject subObject) {
        object.add(attr, subObject);
    }

    @Override
    public void addArrayToObject(JsonObject object, String attr, JsonArray subArray) {
        object.add(attr, subArray);
    }

    @Override
    public void addStringToObject(JsonObject object, String attr, String value) {
        object.add(attr, value);
    }

    @Override
    public void addBooleanToObject(JsonObject object, String attr, Boolean value) {
        object.add(attr, value);
    }

    @Override
    public void addNullToObject(JsonObject object, String attr) {
        object.add(attr, null);
    }

    @Override
    public void addNumberToObject(JsonObject object, String attr, JsonNumber value) {
        object.add(attr, jsonNumberConverter.convert(value));
    }

    @Override
    public void addObjectToArray(JsonArray array, JsonObject subObject) {
        array.add(subObject);
    }

    @Override
    public void addArrayToArray(JsonArray array, JsonArray subArray) {
        array.add(subArray);
    }

    @Override
    public void addStringToArray(JsonArray array, String value) {
        array.add(value);
    }

    @Override
    public void addBooleanToArray(JsonArray array, Boolean value) {
        array.add(value);
    }

    @Override
    public void addNullToArray(JsonArray array) {
        array.add(null);
    }

    @Override
    public void addNumberToArray(JsonArray array, JsonNumber value) {
        array.add(jsonNumberConverter.convert(value));
    }
}
