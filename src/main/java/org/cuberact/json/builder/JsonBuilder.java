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

import org.cuberact.json.parser.JsonParser;

/**
 * Used by {@link JsonParser#JsonParser(JsonBuilder, org.cuberact.json.number.NumberConverter)} for build Json tree
 *
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public interface JsonBuilder {

    /**
     * @return json object representation
     */
    Object createJsonObject();

    /**
     * @return json array representation
     */
    Object createJsonArray();

    /**
     * @param jsonObject - json object representation created by {@link JsonBuilder#createJsonObject()}
     * @param attr       - attribute name
     * @param value      - attribute value
     */
    void addToJsonObject(Object jsonObject, String attr, Object value);

    /**
     * @param jsonArray - json array representation created by {@link JsonBuilder#createJsonArray()}
     * @param value     - array item value
     */
    void addToJsonArray(Object jsonArray, Object value);

}
