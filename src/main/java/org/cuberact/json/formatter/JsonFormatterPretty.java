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

package org.cuberact.json.formatter;

import org.cuberact.json.output.JsonOutput;

/**
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public class JsonFormatterPretty extends JsonFormatterBase {

    protected String actualIndent;
    protected String indent;

    public JsonFormatterPretty() {
        this("", "    ");
    }

    public JsonFormatterPretty(String startIndent, String indent) {
        this.actualIndent = startIndent;
        this.indent = indent;
    }

    @Override
    public void writeObjectStart(JsonOutput output) {
        output.write("{\n");
        actualIndent += indent;
        output.write(actualIndent);
    }

    @Override
    public void writeObjectEnd(JsonOutput output) {
        if (actualIndent.length() <= indent.length()) {
            actualIndent = "";
        } else {
            actualIndent = actualIndent.substring(0, actualIndent.length() - indent.length());
        }
        output.write("\n");
        output.write(actualIndent);
        output.write("}");
    }

    @Override
    public void writeArrayStart(JsonOutput output) {
        output.write("[");
    }

    @Override
    public void writeArrayEnd(JsonOutput output) {
        output.write("]");
    }

    @Override
    public void writeObjectColon(JsonOutput output) {
        output.write(" : ");
    }

    @Override
    public void writeObjectComma(JsonOutput output) {
        output.write(",\n");
        output.write(actualIndent);
    }

    @Override
    public void writeArrayComma(JsonOutput output) {
        output.write(", ");
    }
}
