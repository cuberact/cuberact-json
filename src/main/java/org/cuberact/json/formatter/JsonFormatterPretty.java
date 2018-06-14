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

import java.util.Objects;

import static org.cuberact.json.optimize.JsonEscape.escape;

/**
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public class JsonFormatterPretty extends JsonFormatterBase {

    public static final Config DEFAULT_CONFIG = new Config();

    private final Config cfg;
    private int indentsCount;
    private boolean objectStarted = false;

    public JsonFormatterPretty() {
        this(DEFAULT_CONFIG);
    }

    public JsonFormatterPretty(Config cfg) {
        this.cfg = Objects.requireNonNull(cfg, "Config").copy();
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
            indentsCount++;
            writeIndent(output);
            objectStarted = false;
        }
        super.writeObjectAttr(attr, output);
    }

    private void writeIndent(JsonOutput output) {
        switch (indentsCount) {
            case 0:
                break;
            case 5:
                output.write(cfg.indent);
            case 4:
                output.write(cfg.indent);
            case 3:
                output.write(cfg.indent);
            case 2:
                output.write(cfg.indent);
            case 1:
                output.write(cfg.indent);
                break;
            default:
                for (int i = 0; i < indentsCount; i++) {
                    output.write(cfg.indent);
                }
        }
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
        writeIndent(output);
    }

    @Override
    public void writeObjectEnd(JsonOutput output) {
        if (!objectStarted) {
            output.write(cfg.lineBreak);
            indentsCount--;
            writeIndent(output);
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
