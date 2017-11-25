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
     * @return object representation
     */
    Object createObject();

    /**
     * @return array representation
     */
    Object createArray();

    /**
     * @param object    - json object representation created by {@link JsonBuilder#createObject()}
     * @param attr      - attribute
     * @param subObject - json object representation created by {@link JsonBuilder#createObject()}
     */
    void addObjectToObject(Object object, String attr, Object subObject);

    /**
     * @param object   - json object representation created by {@link JsonBuilder#createObject()}
     * @param attr     - attribute
     * @param subArray - json array representation created by {@link JsonBuilder#createArray()}
     */
    void addArrayToObject(Object object, String attr, Object subArray);

    /**
     * @param object - json object representation created by {@link JsonBuilder#createObject()}
     * @param attr   - attribute
     * @param value  - value
     */
    void addStringToObject(Object object, String attr, String value);

    /**
     * @param object - json object representation created by {@link JsonBuilder#createObject()}
     * @param attr   - attribute
     * @param value  - value
     */
    void addBooleanToObject(Object object, String attr, Boolean value);

    /**
     * @param object - json object representation created by {@link JsonBuilder#createObject()}
     * @param attr   - attribute
     */
    void addNullToObject(Object object, String attr);

    /**
     * @param object - json object representation created by {@link JsonBuilder#createObject()}
     * @param attr   - attribute
     * @param value  - value
     */
    void addNumberToObject(Object object, String attr, JsonNumber value);

    /**
     * @param array     - json array representation created by {@link JsonBuilder#createArray()}
     * @param subObject - json object representation created by {@link JsonBuilder#createObject()}
     */
    void addObjectToArray(Object array, Object subObject);

    /**
     * @param array    - json array representation created by {@link JsonBuilder#createArray()}
     * @param subArray - json array representation created by {@link JsonBuilder#createArray()}
     */
    void addArrayToArray(Object array, Object subArray);

    /**
     * @param array - json array representation created by {@link JsonBuilder#createArray()}
     * @param value - value
     */
    void addStringToArray(Object array, String value);

    /**
     * @param array - json array representation created by {@link JsonBuilder#createArray()}
     * @param value - value
     */
    void addBooleanToArray(Object array, Boolean value);

    /**
     * @param array - json array representation created by {@link JsonBuilder#createArray()}
     */
    void addNullToArray(Object array);

    /**
     * @param array - json array representation created by {@link JsonBuilder#createArray()}
     * @param value - value
     */
    void addNumberToArray(Object array, JsonNumber value);
}
