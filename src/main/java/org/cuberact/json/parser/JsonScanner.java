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
import org.cuberact.json.number.JsonNumber;

import static org.cuberact.json.input.JsonInput.END_OF_INPUT;
import static org.cuberact.json.optimize.CharTable.hexBitShift;
import static org.cuberact.json.optimize.CharTable.toInt;

/**
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
final class JsonScanner {

    private final JsonInput input;
    private final char[] buffer = new char[1024];
    char lastReadChar;

    JsonScanner(JsonInput input) {
        this.input = input;
    }

    private void nextChar() {
        lastReadChar = input.nextChar();
    }

    void nextImportantChar() {
        for (; ; ) {
            nextChar();
            if (!(lastReadChar == ' ' || lastReadChar == '\n' || lastReadChar == '\r' || lastReadChar == '\t')) {
                return;
            }
        }
    }

    void consumeTrue() {
        nextChar();
        if (lastReadChar == 'r') {
            nextChar();
            if (lastReadChar == 'u') {
                nextChar();
                if (lastReadChar == 'e') {
                    nextImportantChar();
                    return;
                }
            }
        }
        throw new JsonException(error("Expected true"));
    }

    void consumeFalse() {
        nextChar();
        if (lastReadChar == 'a') {
            nextChar();
            if (lastReadChar == 'l') {
                nextChar();
                if (lastReadChar == 's') {
                    nextChar();
                    if (lastReadChar == 'e') {
                        nextImportantChar();
                        return;
                    }
                }
            }
        }
        throw new JsonException(error("Expected false"));
    }

    void consumeNull() {
        nextChar();
        if (lastReadChar == 'u') {
            nextChar();
            if (lastReadChar == 'l') {
                nextChar();
                if (lastReadChar == 'l') {
                    nextImportantChar();
                    return;
                }
            }
        }
        throw new JsonException(error("Expected null"));
    }

    JsonNumber consumeNumber() {
        int i = 0;
        buffer[i++] = lastReadChar;
        if (buffer[0] == '-') {
            nextChar();
            if (lastReadChar < '0' || lastReadChar > '9') {
                throw new JsonException(error("Expected correct number"));
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

    String consumeString() {
        StringBuilder token = null;
        int count = 0;
        for (; ; ) {
            nextChar();
            if (lastReadChar == '"') {
                nextImportantChar();
                if (token != null) {
                    token.append(buffer, 0, count);
                    return token.toString();
                }
                return new String(buffer, 0, count);
            } else if (lastReadChar == '\\') {
                nextChar();
                if (lastReadChar == 'b') {
                    lastReadChar = '\b';
                } else if (lastReadChar == 'f') {
                    lastReadChar = '\f';
                } else if (lastReadChar == 'n') {
                    lastReadChar = '\n';
                } else if (lastReadChar == 'r') {
                    lastReadChar = '\r';
                } else if (lastReadChar == 't') {
                    lastReadChar = '\t';
                } else if (lastReadChar == 'u') {
                    lastReadChar = consumeUnicodeChar();
                }
            } else if (lastReadChar == END_OF_INPUT) {
                break;
            }
            buffer[count++] = lastReadChar;
            if (count == 1024) {
                count = 0;
                if (token == null) {
                    token = new StringBuilder(2064);
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
            nextChar();
            if ((lastReadChar > '/' && lastReadChar < ':') || (lastReadChar > '@' && lastReadChar < 'G') || (lastReadChar > '`' && lastReadChar < 'g')) { //0-9 a-f A-F
                unicodeChar += (toInt(lastReadChar) << hexBitShift(h));
            } else {
                throw new JsonException(error("Expected 4 digits hex number"));
            }
        }
        return (char) (unicodeChar);
    }

    String error(String error) {
        return "Parse error on position " + input.position() + " - " + error;
    }
}
