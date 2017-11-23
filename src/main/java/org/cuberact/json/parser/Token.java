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
import org.cuberact.json.number.NumberConverter;

import static org.cuberact.json.input.JsonInput.END_OF_INPUT;

/**
 * Consumer for json data types
 *
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
class Token {

    private static final ThreadLocal<char[]> THREAD_BUFFER = ThreadLocal.withInitial(() -> new char[4096]);
    private static final int[] CHAR_TO_INT = new int[103];
    private static final int[] HEX_BIT_SHIFT = new int[]{12, 8, 4, 0};

    static {
        CHAR_TO_INT['0'] = 0;
        CHAR_TO_INT['1'] = 1;
        CHAR_TO_INT['2'] = 2;
        CHAR_TO_INT['3'] = 3;
        CHAR_TO_INT['4'] = 4;
        CHAR_TO_INT['5'] = 5;
        CHAR_TO_INT['6'] = 6;
        CHAR_TO_INT['7'] = 7;
        CHAR_TO_INT['8'] = 8;
        CHAR_TO_INT['9'] = 9;
        CHAR_TO_INT['a'] = 10;
        CHAR_TO_INT['b'] = 11;
        CHAR_TO_INT['c'] = 12;
        CHAR_TO_INT['d'] = 13;
        CHAR_TO_INT['e'] = 14;
        CHAR_TO_INT['f'] = 15;
        CHAR_TO_INT['A'] = 10;
        CHAR_TO_INT['B'] = 11;
        CHAR_TO_INT['C'] = 12;
        CHAR_TO_INT['D'] = 13;
        CHAR_TO_INT['E'] = 14;
        CHAR_TO_INT['F'] = 15;
    }

    private Token() {
        //hidden constructor
    }

    static void consumeTrue(JsonScanner scanner) {
        if (!(scanner.nextChar() == 'r' && scanner.nextChar() == 'u' && scanner.nextChar() == 'e')) {
            throw new JsonException(scanner.buildExceptionMessage("Expected true"));
        }
        scanner.nextImportantChar();
    }

    static void consumeFalse(JsonScanner scanner) {
        if (!(scanner.nextChar() == 'a' && scanner.nextChar() == 'l' && scanner.nextChar() == 's' && scanner.nextChar() == 'e')) {
            throw new JsonException(scanner.buildExceptionMessage("Expected false"));
        }
        scanner.nextImportantChar();
    }

    static void consumeNull(JsonScanner scanner) {
        if (!(scanner.nextChar() == 'u' && scanner.nextChar() == 'l' && scanner.nextChar() == 'l')) {
            throw new JsonException(scanner.buildExceptionMessage("Expected null"));
        }
        scanner.nextImportantChar();
    }

    static Number consumeNumber(JsonScanner scanner, NumberConverter numberConverter) {
        try {
            char[] buffer = THREAD_BUFFER.get();
            char c;
            int i = 0;
            buffer[i++] = scanner.lastReadChar();
            if (buffer[0] == '-') {
                if (!isNumber(c = scanner.nextChar())) {
                    throw new JsonException(scanner.buildExceptionMessage("Expected correct number"));
                }
                buffer[i++] = c;
            }
            while (isNumber(c = scanner.nextChar())) {
                buffer[i++] = c;
            }
            boolean containsDot = c == '.';
            if (containsDot) {
                buffer[i++] = '.';
                while (isNumber(c = scanner.nextChar())) {
                    buffer[i++] = c;
                }
                if (c == 'e' || c == 'E') {
                    buffer[i++] = 'e';
                    c = scanner.nextChar();
                    if (c == '-' || c == '+' || isNumber(c)) {
                        buffer[i++] = c;
                        while (isNumber(c = scanner.nextChar())) {
                            buffer[i++] = c;
                        }
                    }
                }
            }
            if (scanner.isWhiteChar(c)) {
                scanner.nextImportantChar();
            }
            if (containsDot) {
                return numberConverter.convertFloatingPointNumber(buffer, 0, i);
            }
            return numberConverter.convertWholeNumber(buffer, 0, i);
        } catch (Throwable t) {
            throw new JsonException(scanner.buildExceptionMessage("Wrong number"), t);
        }
    }

    static String consumeString(JsonScanner scanner) {
        char[] buffer = THREAD_BUFFER.get();
        StringBuilder token = null;
        int i = 0;
        char c;
        while ((c = scanner.nextChar()) != END_OF_INPUT) {
            if (c == '"') {
                scanner.nextImportantChar();
                if (token != null) {
                    token.append(buffer, 0, i);
                    return token.toString();
                }
                return new String(buffer, 0, i);
            } else if (c == '\\') {
                c = scanner.nextChar();
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
                    c = parseUnicode(scanner);
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
        throw new JsonException(scanner.buildExceptionMessage("Expected \""));
    }

    private static boolean isNumber(char c) {
        return c > '/' && c < ':'; //0-9
    }

    private static char parseUnicode(JsonScanner scanner) {
        int unicodeChar = 0;
        for (int h = 0; h < 4; h++) {
            char c = scanner.nextChar();
            if ((c > '/' && c < ':') || (c > '@' && c < 'G') || (c > '`' && c < 'g')) { //0-9 a-f A-F
                unicodeChar += (CHAR_TO_INT[c] << HEX_BIT_SHIFT[h]);
            } else {
                throw new JsonException(scanner.buildExceptionMessage("Expected 4 digits hex number"));
            }
        }
        return (char) (unicodeChar);
    }
}
