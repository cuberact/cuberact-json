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

import org.cuberact.json.JsonArray;
import org.cuberact.json.JsonNumber;
import org.cuberact.json.JsonObject;
import org.cuberact.json.output.JsonOutput;

/**
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public class JsonFormatterPretty extends JsonFormatterBase {

    protected int indentCount = 0;

    @Override
    public boolean writeObjectStart(JsonObject json, JsonOutput<?> output) {
        if (flat() && isFlat(json)) {
            output.write(getObjectStart());
            output.write(getObjectEnd());
            return false;
        }
        output.write(getObjectStart());
        indentCount++;
        return true;
    }

    @Override
    public void writeObjectAttr(CharSequence attr, JsonObject json, JsonOutput<?> output) {
        output.write(getLineBreak());
        writeIndent(output);
        writeString(attr, output);
    }

    @Override
    public void writeObjectColon(JsonObject json, JsonOutput<?> output) {
        output.write(getObjectColon());
    }

    @Override
    public void writeObjectValue(Object value, JsonObject json, JsonOutput<?> output) {
        writeValue(value, output);
    }

    @Override
    public void writeObjectComma(JsonObject json, JsonOutput<?> output) {
        output.write(getObjectComma());
    }

    @Override
    public void writeObjectEnd(JsonObject json, JsonOutput<?> output) {
        output.write(getLineBreak());
        indentCount--;
        writeIndent(output);
        output.write(getObjectEnd());
    }

    @Override
    public boolean writeArrayStart(JsonArray json, JsonOutput<?> output) {
        if (flat() && isFlat(json)) {
            output.write(getArrayStart());
            int len = getArrayStart().length();
            int arrayLen = json.size();
            for (int i = 0; i < arrayLen; i++) {
                if (i != 0) {
                    output.write(getFlatArrayComma());
                    len += getFlatArrayComma().length();
                    if (len > getFlatArrayLineLength()) {
                        len = getFlatArrayIndent().length();
                        output.write(getLineBreak());
                        writeIndent(output);
                        output.write(getFlatArrayIndent());
                    }
                }
                len += writeFlatValue(json.get(i), output);
            }
            output.write(getArrayEnd());
            return false;
        }
        output.write(getArrayStart());
        indentCount++;
        return true;
    }

    @Override
    public void writeArrayValue(Object value, JsonArray json, JsonOutput<?> output) {
        output.write(getLineBreak());
        writeIndent(output);
        writeValue(value, output);
    }

    @Override
    public void writeArrayComma(JsonArray json, JsonOutput<?> output) {
        output.write(getArrayComma());
    }

    @Override
    public void writeArrayEnd(JsonArray json, JsonOutput<?> output) {
        output.write(getLineBreak());
        indentCount--;
        writeIndent(output);
        output.write(getArrayEnd());
    }

    protected void writeIndent(JsonOutput<?> output) {
        for (int i = 0; i < indentCount; i++) {
            output.write(getIndent());
        }
    }

    protected boolean isFlat(JsonObject json) {
        if (json == null) {
            return false;
        }
        return json.size() == 0;
    }

    protected boolean isFlat(JsonArray json) {
        if (json == null) {
            return false;
        }
        for (Object value : json.iterable()) {
            if (value instanceof CharSequence) {
                continue;
            }
            if (value instanceof JsonNumber) {
                continue;
            }
            if (value instanceof Integer) {
                continue;
            }
            if (value instanceof Long) {
                continue;
            }
            if (value instanceof Double) {
                continue;
            }
            if (value instanceof Float) {
                continue;
            }
            if (value instanceof Boolean) {
                continue;
            }
            if (value instanceof JsonArray && ((JsonArray) value).size() == 0) {
                continue;
            }
            if (value instanceof JsonObject && ((JsonObject) value).size() == 0) {
                continue;
            }
            return false;
        }
        return true;
    }

    protected int writeFlatValue(Object value, JsonOutput<?> output) {
        int len = 0;
        if (value instanceof JsonNumber) {
            String str = value.toString();
            output.write(str);
            len += str.length();
        } else if (value instanceof CharSequence) {
            len += writeString((CharSequence) value, output);
        } else if (value instanceof Boolean) {
            String str = ((Boolean) value) ? getBooleanTrue() : getBooleanFalse();
            output.write(str);
            len += str.length();
        } else if (value instanceof Double || value instanceof Float) {
            String str = String.valueOf(value).replace(',', '.');
            output.write(str);
            len += str.length();
        } else if (value instanceof JsonArray) {
            String str = getArrayStart() + getArrayEnd();
            output.write(str);
            len += str.length();
        } else if (value instanceof JsonObject) {
            String str = getObjectStart() + getObjectEnd();
            output.write(str);
            len += str.length();
        } else {
            String str = value.toString();
            output.write(str);
            len += str.length();
        }
        return len;
    }

    @Override
    protected String getObjectColon() {
        return ": ";
    }

    protected String getIndent() {
        return "  ";
    }

    protected String getLineBreak() {
        return "\n";
    }

    protected boolean flat() {
        return true;
    }

    protected String getFlatArrayComma() {
        return ", ";
    }

    protected String getFlatArrayIndent() {
        return " ";
    }

    protected int getFlatArrayLineLength() {
        return 150;
    }
}
