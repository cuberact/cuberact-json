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
 * Used by {@link JsonParser#JsonParser(JsonBuilder)}
 *
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public interface JsonBuilder<OBJECT, ARRAY> {

    /**
     * Called on build start. Before json parsing. Only for special cases - for example if we need directly write to output and not build a DOM
     */
    void buildStart();

    /**
     * Called on build end. If error in json, this method will not be called. Only for special cases - for example if we need directly write to output and not build a DOM
     */
    void buildEnd();

    /**
     * @return object representation
     */
    OBJECT createObject();

    /**
     * @return array representation
     */
    ARRAY createArray();

    /**
     * Called after finish OBJECT. Only for special cases - for example if we need directly write to output and not build a DOM
     *
     * @param object - json object representation created by {@link JsonBuilder#createObject()}
     */
    void objectCompleted(OBJECT object);

    /**
     * Called after finish OBJECT. Only for special cases - for example if we need directly write to output and not build a DOM
     *
     * @param array - json array representation created by {@link JsonBuilder#createArray()}
     */
    void arrayCompleted(ARRAY array);

    /**
     * Called before parse subObject or subArray. Only for special cases - for example if we need directly write to output and not build a DOM
     *
     * @param object - json object representation created by {@link JsonBuilder#createObject()}
     * @param attr   - attribute
     */
    void addObjectAttr(OBJECT object, String attr);

    /**
     * Called before parse subObject or subArray. Only for special cases - for example if we need directly write to output and not build a DOM
     *
     * @param array - json array representation created by {@link JsonBuilder#createArray()}
     */
    void addArrayComma(ARRAY array);

    /**
     * @param object - json object representation created by {@link JsonBuilder#createObject()}
     * @param attr   - attribute
     * @param value  - finished json object representation created by {@link JsonBuilder#createObject()}
     */
    void addObjectToObject(OBJECT object, String attr, OBJECT value);

    /**
     * @param object - json object representation created by {@link JsonBuilder#createObject()}
     * @param attr   - attribute
     * @param value  - finished json array representation created by {@link JsonBuilder#createArray()}
     */
    void addArrayToObject(OBJECT object, String attr, ARRAY value);

    /**
     * @param object - json object representation created by {@link JsonBuilder#createObject()}
     * @param attr   - attribute
     * @param value  - value
     */
    void addStringToObject(OBJECT object, String attr, String value);

    /**
     * @param object - json object representation created by {@link JsonBuilder#createObject()}
     * @param attr   - attribute
     * @param value  - value
     */
    void addBooleanToObject(OBJECT object, String attr, Boolean value);

    /**
     * @param object - json object representation created by {@link JsonBuilder#createObject()}
     * @param attr   - attribute
     */
    void addNullToObject(OBJECT object, String attr);

    /**
     * @param object - json object representation created by {@link JsonBuilder#createObject()}
     * @param attr   - attribute
     * @param value  - value
     */
    void addNumberToObject(OBJECT object, String attr, JsonNumber value);

    /**
     * @param array - json array representation created by {@link JsonBuilder#createArray()}
     * @param value - finished json object representation created by {@link JsonBuilder#createObject()}
     */
    void addObjectToArray(ARRAY array, OBJECT value);

    /**
     * @param array - json array representation created by {@link JsonBuilder#createArray()}
     * @param value - finished json array representation created by {@link JsonBuilder#createArray()}
     */
    void addArrayToArray(ARRAY array, ARRAY value);

    /**
     * @param array - json array representation created by {@link JsonBuilder#createArray()}
     * @param value - value
     */
    void addStringToArray(ARRAY array, String value);

    /**
     * @param array - json array representation created by {@link JsonBuilder#createArray()}
     * @param value - value
     */
    void addBooleanToArray(ARRAY array, Boolean value);

    /**
     * @param array - json array representation created by {@link JsonBuilder#createArray()}
     */
    void addNullToArray(ARRAY array);

    /**
     * @param array - json array representation created by {@link JsonBuilder#createArray()}
     * @param value - value
     */
    void addNumberToArray(ARRAY array, JsonNumber value);
}
