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

/**
 * Default builder which build Recipe Json tree structure - {@link JsonObject} and {@link JsonArray}
 * This builder is Thread-safe
 *
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public class JsonBuilderTree implements JsonBuilder {

    public static final JsonBuilderTree REF = new JsonBuilderTree();

    private JsonBuilderTree() {
        //singleton, use REF
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

    /**
     * @param jsonObject - {@link JsonObject}
     * @param attr       - attribute name
     * @param value      - attribute value
     */
    @Override
    public void addToJsonObject(Object jsonObject, String attr, Object value) {
        ((JsonObject) jsonObject).add(attr, value);
    }

    /**
     * @param jsonArray - {@link JsonArray}
     * @param value     - array item value
     */
    @Override
    public void addToJsonArray(Object jsonArray, Object value) {
        ((JsonArray) jsonArray).add(value);
    }
}
