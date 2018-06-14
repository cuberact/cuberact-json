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

import org.cuberact.json.Json;
import org.cuberact.json.JsonArray;
import org.cuberact.json.JsonObject;

/**
 * Default builder for Json DOM - {@link Json} - {@link JsonObject} and {@link JsonArray}
 *
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public class JsonBuilderDom extends JsonBuilderBase<JsonObject, JsonArray> {

    public static final JsonBuilderDom REF = new JsonBuilderDom();

    @Override
    public JsonObject createObject() {
        return new JsonObject();
    }

    @Override
    public JsonArray createArray() {
        return new JsonArray();
    }

    @Override
    protected void addToObject(JsonObject object, String attr, Object value) {
        object.add(attr, value);
    }

    @Override
    protected void addToArray(JsonArray array, Object value) {
        array.add(value);
    }
}
