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

import org.cuberact.json.JsonException;
import org.cuberact.json.output.JsonOutput;

/**
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public class JsonFormatterPretty extends JsonFormatterBase {

    public static final Config DEFAULT_CONFIG = new Config();

    private final Config cfg;
    private String indent = "";
    private boolean objectStarted = false;

    public JsonFormatterPretty() {
        this(DEFAULT_CONFIG);
    }

    public JsonFormatterPretty(Config cfg) {
        this.cfg = cfg.copy();
    }

    @Override
    public void writeObjectStart(JsonOutput output) {
        output.write(cfg.objectStart);
        objectStarted = true;
    }

    @Override
    public void writeObjectAttr(CharSequence attr, JsonOutput output) {
        if (objectStarted) {
            output.write(cfg.lineBreak);
            incIndent();
            output.write(indent);
            objectStarted = false;
        }
        super.writeObjectAttr(attr, output);
    }

    @Override
    public void writeObjectColon(JsonOutput output) {
        output.write(cfg.objectColon);
    }

    protected void writeString(CharSequence value, JsonOutput output) {
        output.write(cfg.quotationMark);
        escape(value, output);
        output.write(cfg.quotationMark);
    }

    @Override
    public void writeObjectComma(JsonOutput output) {
        output.write(cfg.objectComma);
        output.write(cfg.lineBreak);
        output.write(indent);
    }

    @Override
    public void writeObjectEnd(JsonOutput output) {
        if (!objectStarted) {
            decIndent();
            output.write(cfg.lineBreak);
            output.write(indent);
        }
        objectStarted = false;
        output.write(cfg.objectEnd);
    }

    @Override
    public void writeArrayStart(JsonOutput output) {
        output.write(cfg.arrayStart);
    }

    @Override
    public void writeArrayComma(JsonOutput output) {
        output.write(cfg.arrayComma);
    }

    @Override
    public void writeArrayEnd(JsonOutput output) {
        output.write(cfg.arrayEnd);
    }

    private void incIndent() {
        indent += cfg.indent;
    }

    private void decIndent() {
        int size = indent.length() - cfg.indent.length();
        if (size > 0) {
            indent = indent.substring(0, size);
        } else {
            indent = "";
        }
    }

    public static class Config implements Cloneable {
        public String indent = "    ";
        public String objectStart = "{";
        public String objectEnd = "}";
        public String arrayStart = "[";
        public String arrayEnd = "]";
        public String objectColon = " : ";
        public String objectComma = ",";
        public String arrayComma = ", ";
        public String quotationMark = "\"";
        public String lineBreak = "\n";

        Config copy() {
            try {
                return (Config) this.clone();
            } catch (CloneNotSupportedException e) {
                throw new JsonException(e);
            }
        }
    }
}
