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
import org.cuberact.json.JsonNumber;
import org.cuberact.json.input.JsonInput;
import static org.cuberact.json.input.JsonInput.END_OF_INPUT;
import static org.cuberact.json.optimize.CharTable.hexBitShift;
import static org.cuberact.json.optimize.CharTable.toInt;

/**
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
final class JsonScanner {

    private final JsonInput input;
    private final char[] buffer = new char[4096];
    char lastReadChar;

    JsonScanner(JsonInput input) {
        this.input = input;
    }

    private char nextChar() {
        return lastReadChar = input.nextChar();
    }

    char nextImportantChar() {
        for (; ; ) {
            nextChar();
            if (!(lastReadChar == ' ' || lastReadChar == '\n' || lastReadChar == '\r' || lastReadChar == '\t')) {
                return lastReadChar;
            }
        }
    }

    String consumeString() {
        StringBuilder token = null;
        int count = 0;
        for (; ; ) {
            nextChar();
            if (lastReadChar == '"') {
                nextImportantChar();
                if (token == null) return new String(buffer, 0, count);
                token.append(buffer, 0, count);
                return token.toString();
            } else if (lastReadChar == '\\') {
                nextChar();
                switch (lastReadChar) {
                    case 'b':
                        lastReadChar = '\b';
                        break;
                    case 'f':
                        lastReadChar = '\f';
                        break;
                    case 'n':
                        lastReadChar = '\n';
                        break;
                    case 'r':
                        lastReadChar = '\r';
                        break;
                    case 't':
                        lastReadChar = '\t';
                        break;
                    case 'u':
                        lastReadChar = consumeUnicodeChar();
                        break;
                }
            } else if (lastReadChar == END_OF_INPUT) {
                break;
            }
            buffer[count++] = lastReadChar;
            if (count == 4096) {
                count = 0;
                if (token == null) {
                    token = new StringBuilder(8000);
                }
                token.append(buffer);
            }
        }
        throw jsonException("Expected \"");
    }

    void consumeTrue() {
        if (nextChar() == 'r' && nextChar() == 'u' && nextChar() == 'e') {
            nextImportantChar();
            return;
        }
        throw jsonException("Expected true");
    }

    void consumeFalse() {
        if (nextChar() == 'a' && nextChar() == 'l' && nextChar() == 's' && nextChar() == 'e') {
            nextImportantChar();
            return;
        }
        throw jsonException("Expected false");
    }

    void consumeNull() {
        if (nextChar() == 'u' && nextChar() == 'l' && nextChar() == 'l') {
            nextImportantChar();
            return;
        }
        throw jsonException("Expected null");
    }

    JsonNumber consumeNumber() {
        int i = 0;
        buffer[i++] = lastReadChar;
        if (buffer[0] == '-') {
            nextChar();
            if (lastReadChar < '0' || lastReadChar > '9') {
                throw jsonException("Expected correct number");
            }
            buffer[i++] = lastReadChar;
        }
        for (; ; ) {
            nextChar();
            if (lastReadChar < '0' || lastReadChar > '9') break;
            buffer[i++] = lastReadChar;
        }
        boolean containsDot = lastReadChar == '.';
        if (containsDot) {
            buffer[i++] = '.';
            for (; ; ) {
                nextChar();
                if (lastReadChar < '0' || lastReadChar > '9') break;
                buffer[i++] = lastReadChar;
            }
            if (lastReadChar == 'e' || lastReadChar == 'E') {
                buffer[i++] = 'e';
                nextChar();
                if (lastReadChar == '-' || lastReadChar == '+' || !(lastReadChar < '0' || lastReadChar > '9')) {
                    buffer[i++] = lastReadChar;
                    for (; ; ) {
                        nextChar();
                        if (lastReadChar < '0' || lastReadChar > '9') break;
                        buffer[i++] = lastReadChar;
                    }
                }
            }
        }
        if (lastReadChar == ' ' || lastReadChar == '\n' || lastReadChar == '\r' || lastReadChar == '\t') {
            nextImportantChar();
        }
        return new JsonNumber(buffer, i, containsDot);
    }

    private char consumeUnicodeChar() {
        int unicodeChar = 0;
        for (int h = 0; h < 4; h++) {
            nextChar();
            if ((lastReadChar > '/' && lastReadChar < ':') || (lastReadChar > '@' && lastReadChar < 'G') || (lastReadChar > '`' && lastReadChar < 'g')) { //0-9 a-f A-F
                unicodeChar += (toInt(lastReadChar) << hexBitShift(h));
            } else {
                throw jsonException("Expected 4 digits hex number");
            }
        }
        return (char) (unicodeChar);
    }

    JsonException jsonException(String error) {
        return new JsonException("Parse error on position " + input.position() + " - " + error);
    }
}
