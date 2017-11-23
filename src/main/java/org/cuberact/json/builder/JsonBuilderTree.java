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
 * Default builder which build Recipe Json tree structure - {@link JsonObject} and {@link JsonArray}
 *
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public class JsonBuilderTree implements JsonBuilder {

    public static JsonBuilderTree DEFAULT = new JsonBuilderTree(JsonNumberConverterLongDouble.REF);

    private final JsonNumberConverter jsonNumberConverter;

    public JsonBuilderTree(JsonNumberConverter jsonNumberConverter) {
        this.jsonNumberConverter = Objects.requireNonNull(jsonNumberConverter, "NumberConverter");
    }

    /**
     * @return JsonObject - {@link JsonObject}
     */
    @Override
    public Object createJsonObject() {
        return new JsonObject();
    }

    /**
     * @return JsonArray - {@link JsonArray}
     */
    @Override
    public Object createJsonArray() {
        return new JsonArray();
    }

    @Override
    public void addJsonObjectToJsonObject(Object jsonObject, String attr, Object subJsonObject) {
        ((JsonObject) jsonObject).add(attr, subJsonObject);
    }

    @Override
    public void addJsonArrayToJsonObject(Object jsonObject, String attr, Object subJsonArray) {
        ((JsonObject) jsonObject).add(attr, subJsonArray);
    }

    @Override
    public void addStringToJsonObject(Object jsonObject, String attr, String value) {
        ((JsonObject) jsonObject).add(attr, value);
    }

    @Override
    public void addBooleanToJsonObject(Object jsonObject, String attr, Boolean value) {
        ((JsonObject) jsonObject).add(attr, value);
    }

    @Override
    public void addNullToJsonObject(Object jsonObject, String attr) {
        ((JsonObject) jsonObject).add(attr, null);
    }

    @Override
    public void addNumberToJsonObject(Object jsonObject, String attr, JsonNumber value) {
        ((JsonObject) jsonObject).add(attr, jsonNumberConverter.convert(value));
    }

    @Override
    public void addJsonObjectToJsonArray(Object jsonArray, Object subJsonObject) {
        ((JsonArray) jsonArray).add(subJsonObject);
    }

    @Override
    public void addJsonArrayToJsonArray(Object jsonArray, Object subJsonArray) {
        ((JsonArray) jsonArray).add(subJsonArray);
    }

    @Override
    public void addStringToJsonArray(Object jsonArray, String value) {
        ((JsonArray) jsonArray).add(value);
    }

    @Override
    public void addBooleanToJsonArray(Object jsonArray, Boolean value) {
        ((JsonArray) jsonArray).add(value);
    }

    @Override
    public void addNullToJsonArray(Object jsonArray) {
        ((JsonArray) jsonArray).add(null);
    }

    @Override
    public void addNumberToJsonArray(Object jsonArray, JsonNumber value) {
        ((JsonArray) jsonArray).add(jsonNumberConverter.convert(value));
    }
}
