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
 *  Builder which not building DOM but directly write input to output.
 *  Suitable for input formatting, for example.
 *
 * @author Michal Nikodim (michal.nikodim@topmonks.com)
 */
public class JsonBuilderOutput<E> extends JsonBuilderBase<E, E> {

    private static final String OBJ = "OBJ";
    private static final String ARR = "ARR";

    private final JsonOutput<E> output;
    private final JsonFormatter formatter;

    private final ThreadLocal<Stack<String>> stackTypes = ThreadLocal.withInitial(Stack::new);
    private final ThreadLocal<Stack<AtomicInteger>> stackComma = ThreadLocal.withInitial(Stack::new);

    public JsonBuilderOutput(JsonOutput<E> output) {
        this(output, JsonFormatter.PRETTY());
    }

    public JsonBuilderOutput(JsonOutput<E> output, JsonFormatter formatter) {
        this.output = output;
        this.formatter = formatter;
    }

    @Override
    public void buildStart() {
        stackTypes.get().clear();
        stackComma.get().clear();
    }

    @Override
    public void buildEnd() {
        output.flushBuffer();
    }

    @Override
    public E createObject() {
        formatter.writeObjectStart(output);
        stackTypes.get().push(OBJ);
        stackComma.get().push(new AtomicInteger(0));
        return output.getResult();
    }

    @Override
    public E createArray() {
        formatter.writeArrayStart(output);
        stackTypes.get().push(ARR);
        stackComma.get().push(new AtomicInteger(0));
        return output.getResult();
    }

    @Override
    public void objectCompleted(E object) {
        formatter.writeObjectEnd(output);
        stackTypes.get().pop();
        stackComma.get().pop();
    }

    @Override
    public void arrayCompleted(E array) {
        formatter.writeArrayEnd(output);
        stackTypes.get().pop();
        stackComma.get().pop();
    }

    @Override
    public void addObjectAttr(E object, String attr) {
        writeComma();
        formatter.writeObjectAttr(attr, output);
        formatter.writeObjectColon(output);
    }

    @Override
    public void addArrayComma(E array) {
        writeComma();
    }

    @Override
    protected void addToObject(E object, String attr, Object value) {
        writeComma();
        formatter.writeObjectAttr(attr, output);
        formatter.writeObjectColon(output);
        formatter.writeObjectValue(value, output);
    }

    @Override
    protected void addToArray(E o, Object value) {
        writeComma();
        formatter.writeArrayValue(value, output);
    }

    @Override
    protected Object convertJsonNumber(JsonNumber jsonNumber) {
        return jsonNumber; //don't convert
    }

    private void writeComma() {
        if (!stackComma.get().isEmpty() && stackComma.get().peek().getAndIncrement() > 0) {
            if (Objects.equals(OBJ, stackTypes.get().peek())) {
                formatter.writeObjectComma(output);
            } else {
                formatter.writeArrayComma(output);
            }
        }
    }

    @Override
    public void addObjectToObject(E object, String attr, E value) {
        //do nothing
    }

    @Override
    public void addArrayToObject(E object, String attr, E value) {
        //do nothing
    }

    @Override
    public void addArrayToArray(E array, E value) {
        //do nothing
    }

    @Override
    public void addObjectToArray(E array, E value) {
        //do nothing
    }
}
