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

import org.cuberact.json.JsonNumber;

/**
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public abstract class JsonBuilderBase<OBJECT, ARRAY> implements JsonBuilder<OBJECT, ARRAY> {
    @Override
    public abstract OBJECT createObject();

    @Override
    public abstract ARRAY createArray();

    protected abstract void addToObject(OBJECT object, String attr, Object value);

    protected abstract void addToArray(ARRAY array, Object value);

    @Override
    public void buildStart() {
        //only for special cases
    }

    @Override
    public void buildEnd() {
        //only for special cases
    }

    @Override
    public void objectCompleted(OBJECT object) {
        //only for special cases
    }

    @Override
    public void arrayCompleted(ARRAY array) {
        //only for special cases
    }

    @Override
    public void addObjectAttr(OBJECT object, String attr) {
        //only for special cases
    }

    public void addArrayComma(ARRAY array) {
        //only for special cases
    }

    @Override
    public void addObjectToObject(OBJECT object, String attr, OBJECT value) {
        addToObject(object, attr, value);
    }

    @Override
    public void addArrayToObject(OBJECT object, String attr, ARRAY value) {
        addToObject(object, attr, value);
    }

    @Override
    public void addStringToObject(OBJECT object, String attr, String value) {
        addToObject(object, attr, value);
    }

    @Override
    public void addBooleanToObject(OBJECT object, String attr, Boolean value) {
        addToObject(object, attr, value);
    }

    @Override
    public void addNullToObject(OBJECT object, String attr) {
        addToObject(object, attr, null);
    }

    @Override
    public void addNumberToObject(OBJECT object, String attr, JsonNumber value) {
        addToObject(object, attr, value);
    }

    @Override
    public void addObjectToArray(ARRAY array, OBJECT value) {
        addToArray(array, value);
    }

    @Override
    public void addArrayToArray(ARRAY array, ARRAY value) {
        addToArray(array, value);
    }

    @Override
    public void addStringToArray(ARRAY array, String value) {
        addToArray(array, value);
    }

    @Override
    public void addBooleanToArray(ARRAY array, Boolean value) {
        addToArray(array, value);
    }

    @Override
    public void addNullToArray(ARRAY array) {
        addToArray(array, null);
    }

    @Override
    public void addNumberToArray(ARRAY array, JsonNumber value) {
        addToArray(array, value);
    }
}
