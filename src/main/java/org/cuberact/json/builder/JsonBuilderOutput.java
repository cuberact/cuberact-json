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

import org.cuberact.json.formatter.JsonFormatter;
import org.cuberact.json.number.JsonNumber;
import org.cuberact.json.output.JsonOutput;

import java.util.Objects;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Builder which not building DOM but directly write input to output.
 * Suitable for input formatting, for example.
 *
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public class JsonBuilderOutput extends JsonBuilderBase<JsonOutput, JsonOutput> {

    private static final String OBJ = "OBJ";
    private static final String ARR = "ARR";

    private final JsonOutput output;
    private final JsonFormatter formatter;

    private final Stack<String> stackTypes = new Stack<>();
    private final Stack<AtomicInteger> stackComma = new Stack<>();

    public JsonBuilderOutput(JsonOutput output) {
        this(output, JsonFormatter.PRETTY());
    }

    public JsonBuilderOutput(JsonOutput output, JsonFormatter formatter) {
        this.output = output;
        this.formatter = formatter;
    }

    @Override
    public void buildEnd() {
        output.flushBuffer();
    }

    @Override
    public JsonOutput createObject() {
        formatter.writeObjectStart(output);
        stackTypes.push(OBJ);
        stackComma.push(new AtomicInteger(0));
        return output;
    }

    @Override
    public JsonOutput createArray() {
        formatter.writeArrayStart(output);
        stackTypes.push(ARR);
        stackComma.push(new AtomicInteger(0));
        return output;
    }

    @Override
    public void objectCompleted(JsonOutput object) {
        formatter.writeObjectEnd(output);
        stackTypes.pop();
        stackComma.pop();
    }

    @Override
    public void arrayCompleted(JsonOutput array) {
        formatter.writeArrayEnd(output);
        stackTypes.pop();
        stackComma.pop();
    }

    @Override
    public void addObjectAttr(JsonOutput object, String attr) {
        writeComma();
        formatter.writeObjectAttr(attr, output);
        formatter.writeObjectColon(output);
    }

    @Override
    public void addArrayComma(JsonOutput array) {
        writeComma();
    }

    @Override
    protected void addToObject(JsonOutput object, String attr, Object value) {
        writeComma();
        formatter.writeObjectAttr(attr, output);
        formatter.writeObjectColon(output);
        formatter.writeObjectValue(value, output);
    }

    @Override
    protected void addToArray(JsonOutput o, Object value) {
        writeComma();
        formatter.writeArrayValue(value, output);
    }

    @Override
    protected Object convertJsonNumber(JsonNumber jsonNumber) {
        return jsonNumber; //don't convert
    }

    private void writeComma() {
        if (stackComma.peek().getAndIncrement() > 0) {
            if (Objects.equals(OBJ, stackTypes.peek())) {
                formatter.writeObjectComma(output);
            } else {
                formatter.writeArrayComma(output);
            }
        }
    }

    @Override
    public void addObjectToObject(JsonOutput object, String attr, JsonOutput value) {
        //do nothing
    }

    @Override
    public void addArrayToObject(JsonOutput object, String attr, JsonOutput value) {
        //do nothing
    }

    @Override
    public void addArrayToArray(JsonOutput array, JsonOutput value) {
        //do nothing
    }

    @Override
    public void addObjectToArray(JsonOutput array, JsonOutput value) {
        //do nothing
    }
}
