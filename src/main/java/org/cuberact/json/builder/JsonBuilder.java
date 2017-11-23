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

import org.cuberact.json.number.JsonNumber;
import org.cuberact.json.parser.JsonParser;

/**
 * Used by {@link JsonParser#JsonParser(JsonBuilder)} for build Json tree
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
     * @param jsonObject    - json object representation created by {@link JsonBuilder#createJsonObject()}
     * @param attr          - attribute
     * @param subJsonObject - json object representation created by {@link JsonBuilder#createJsonObject()}
     */
    void addJsonObjectToJsonObject(Object jsonObject, String attr, Object subJsonObject);

    /**
     * @param jsonObject   - json object representation created by {@link JsonBuilder#createJsonObject()}
     * @param attr         - attribute
     * @param subJsonArray - json array representation created by {@link JsonBuilder#createJsonArray()}
     */
    void addJsonArrayToJsonObject(Object jsonObject, String attr, Object subJsonArray);

    /**
     * @param jsonObject - json object representation created by {@link JsonBuilder#createJsonObject()}
     * @param attr       - attribute
     * @param value      - value
     */
    void addStringToJsonObject(Object jsonObject, String attr, String value);

    /**
     * @param jsonObject - json object representation created by {@link JsonBuilder#createJsonObject()}
     * @param attr       - attribute
     * @param value      - value
     */
    void addBooleanToJsonObject(Object jsonObject, String attr, Boolean value);

    /**
     * @param jsonObject - json object representation created by {@link JsonBuilder#createJsonObject()}
     * @param attr       - attribute
     */
    void addNullToJsonObject(Object jsonObject, String attr);

    /**
     * @param jsonObject - json object representation created by {@link JsonBuilder#createJsonObject()}
     * @param attr       - attribute
     * @param value      - value
     */
    void addNumberToJsonObject(Object jsonObject, String attr, JsonNumber value);

    /**
     * @param jsonArray     - json array representation created by {@link JsonBuilder#createJsonArray()}
     * @param subJsonObject - json object representation created by {@link JsonBuilder#createJsonObject()}
     */
    void addJsonObjectToJsonArray(Object jsonArray, Object subJsonObject);

    /**
     * @param jsonArray    - json array representation created by {@link JsonBuilder#createJsonArray()}
     * @param subJsonArray - json array representation created by {@link JsonBuilder#createJsonArray()}
     */
    void addJsonArrayToJsonArray(Object jsonArray, Object subJsonArray);

    /**
     * @param jsonArray - json array representation created by {@link JsonBuilder#createJsonArray()}
     * @param value     - value
     */
    void addStringToJsonArray(Object jsonArray, String value);

    /**
     * @param jsonArray - json array representation created by {@link JsonBuilder#createJsonArray()}
     * @param value     - value
     */
    void addBooleanToJsonArray(Object jsonArray, Boolean value);

    /**
     * @param jsonArray - json array representation created by {@link JsonBuilder#createJsonArray()}
     */
    void addNullToJsonArray(Object jsonArray);

    /**
     * @param jsonArray - json array representation created by {@link JsonBuilder#createJsonArray()}
     * @param value     - value
     */
    void addNumberToJsonArray(Object jsonArray, JsonNumber value);
}
