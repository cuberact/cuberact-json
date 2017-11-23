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

package org.cuberact.json.parser;

import org.cuberact.json.JsonException;
import org.cuberact.json.input.JsonInput;
import org.cuberact.json.number.NumberConverter;

import static org.cuberact.json.input.JsonInput.END_OF_INPUT;
import static org.cuberact.json.optimize.CharTable.hexBitShift;
import static org.cuberact.json.optimize.CharTable.toInt;

/**
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
final class JsonScanner {

    private JsonInput input;
    private char[] buffer = new char[1024];
    private char lastReadChar;
    private int position;
    private char[] errorBuffer = new char[64];

    JsonScanner(JsonInput input) {
        this.input = input;
    }

    private char nextChar() {
        position++;
        lastReadChar = errorBuffer[position & 63] = input.nextChar();
        return lastReadChar;
    }

    char nextImportantChar() {
        for (; ; ) {
            nextChar();
            if (!isWhiteChar(lastReadChar)) {
                return lastReadChar;
            }
        }
    }

    char lastReadChar() {
        return lastReadChar;
    }

    private boolean isWhiteChar(char c) {
        return c == ' ' || c == '\n' || c == '\r' || c == '\t';
    }

    private boolean isLineBreak(char c) {
        return c == '\n' || c == '\r';
    }

    private boolean isNumber(char c) {
        return c > '/' && c < ':'; //0-9
    }

    String error(String error) {
        StringBuilder message = new StringBuilder("Parse error on position ");
        message.append(position).append("\n\n");
        try {
            int arrowPosition = -1;
            position++;
            for (int i = 0; i < errorBuffer.length; i++) {
                char c = errorBuffer[position & 63];
                position++;
                if (!isLineBreak(c) && c != 0 && c != JsonInput.END_OF_INPUT) {
                    message.append(c);
                    arrowPosition++;
                }
            }
            for (int i = 0; i < 36; i++) {
                char c = nextChar();
                if (!isLineBreak(c)) {
                    if (c == JsonInput.END_OF_INPUT) {
                        break;
                    }
                    message.append(c);
                }
            }
            message.append("\n");
            for (int i = 0; i < arrowPosition; i++) {
                message.append(".");
            }
            message.append("^ ERROR - ").append(error).append("\n");
        } catch (Throwable t) {
            message.append(" -- ").append(error).append(" [pointer build failed: ").append(t.getMessage()).append("]\n");
        }
        return message.toString();
    }

    void consumeTrue() {
        if (!(nextChar() == 'r' && nextChar() == 'u' && nextChar() == 'e')) {
            throw new JsonException(error("Expected true"));
        }
        nextImportantChar();
    }

    void consumeFalse() {
        if (!(nextChar() == 'a' && nextChar() == 'l' && nextChar() == 's' && nextChar() == 'e')) {
            throw new JsonException(error("Expected false"));
        }
        nextImportantChar();
    }

    void consumeNull() {
        if (!(nextChar() == 'u' && nextChar() == 'l' && nextChar() == 'l')) {
            throw new JsonException(error("Expected null"));
        }
        nextImportantChar();
    }

    Number consumeNumber(NumberConverter numberConverter) {
        try {
            char c;
            int i = 0;
            buffer[i++] = lastReadChar();
            if (buffer[0] == '-') {
                if (!isNumber(c = nextChar())) {
                    throw new JsonException(error("Expected correct number"));
                }
                buffer[i++] = c;
            }
            while (isNumber(c = nextChar())) {
                buffer[i++] = c;
            }
            boolean containsDot = c == '.';
            if (containsDot) {
                buffer[i++] = '.';
                while (isNumber(c = nextChar())) {
                    buffer[i++] = c;
                }
                if (c == 'e' || c == 'E') {
                    buffer[i++] = 'e';
                    c = nextChar();
                    if (c == '-' || c == '+' || isNumber(c)) {
                        buffer[i++] = c;
                        while (isNumber(c = nextChar())) {
                            buffer[i++] = c;
                        }
                    }
                }
            }
            if (isWhiteChar(c)) {
                nextImportantChar();
            }
            if (containsDot) {
                return numberConverter.convertFloatingPointNumber(buffer, 0, i);
            }
            return numberConverter.convertWholeNumber(buffer, 0, i);
        } catch (Throwable t) {
            throw new JsonException(error("Wrong number"), t);
        }
    }

    String consumeString() {
        StringBuilder token = null;
        int i = 0;
        char c;
        while ((c = nextChar()) != END_OF_INPUT) {
            if (c == '"') {
                nextImportantChar();
                if (token != null) {
                    token.append(buffer, 0, i);
                    return token.toString();
                }
                return new String(buffer, 0, i);
            } else if (c == '\\') {
                c = nextChar();
                if (c == 'b') {
                    c = '\b';
                } else if (c == 'f') {
                    c = '\f';
                } else if (c == 'n') {
                    c = '\n';
                } else if (c == 'r') {
                    c = '\r';
                } else if (c == 't') {
                    c = '\t';
                } else if (c == 'u') {
                    c = consumeUnicodeChar();
                }
            }
            buffer[i++] = c;
            if (i == buffer.length) {
                i = 0;
                if (token == null) {
                    token = new StringBuilder();
                    token.append(buffer);
                } else {
                    token.append(buffer);
                }
            }
        }
        throw new JsonException(error("Expected \""));
    }

    private char consumeUnicodeChar() {
        int unicodeChar = 0;
        for (int h = 0; h < 4; h++) {
            char c = nextChar();
            if ((c > '/' && c < ':') || (c > '@' && c < 'G') || (c > '`' && c < 'g')) { //0-9 a-f A-F
                unicodeChar += (toInt(c) << hexBitShift(h));
            } else {
                throw new JsonException(error("Expected 4 digits hex number"));
            }
        }
        return (char) (unicodeChar);
    }
}
