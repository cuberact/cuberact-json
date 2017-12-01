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

import org.cuberact.json.builder.JsonBuilder;
import org.cuberact.json.builder.JsonBuilderTree;
import org.cuberact.json.input.JsonInput;
import org.cuberact.json.input.JsonInputCharArray;
import org.cuberact.json.input.JsonInputCharSequence;
import org.cuberact.json.input.JsonInputReader;

import java.io.Reader;
import java.util.Objects;

/**
 * Parse {@link JsonInput} and build result with {@link JsonBuilder}
 * <p>
 * JsonParser is thread-safe with {@link JsonBuilderTree}
 *
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public final class JsonParser {

    private final JsonBuilder builder;

    public JsonParser() {
        this(JsonBuilderTree.DEFAULT);
    }

    public JsonParser(JsonBuilder builder) {
        this.builder = Objects.requireNonNull(builder, "builder");
    }

    /**
     * @param input - {@link CharSequence}
     * @param <E>   - {@link JsonBuilder#createObject()} or {@link JsonBuilder#createArray()}
     * @return JsonBuilder result - {@link JsonBuilder#createObject()} or {@link JsonBuilder#createArray()}
     */
    public <E> E parse(CharSequence input) {
        return parse(new JsonInputCharSequence(input));
    }

    /**
     * @param input - char array
     * @param <E>   - {@link JsonBuilder#createObject()} or {@link JsonBuilder#createArray()}
     * @return JsonBuilder result - {@link JsonBuilder#createObject()} or {@link JsonBuilder#createArray()}
     */
    public <E> E parse(char[] input) {
        return parse(new JsonInputCharArray(input));
    }

    /**
     * @param input - {@link Reader} - JsonParser doesn't close input reader
     * @param <E>   - {@link JsonBuilder#createObject()} or {@link JsonBuilder#createArray()}
     * @return JsonBuilder result - {@link JsonBuilder#createObject()} or {@link JsonBuilder#createArray()}
     */
    public <E> E parse(Reader input) {
        return parse(new JsonInputReader(input));
    }

    /**
     * @param input - {@link JsonInput}
     * @param <E>   - {@link JsonBuilder#createObject()} or {@link JsonBuilder#createArray()}
     * @return JsonBuilder result - {@link JsonBuilder#createObject()} or {@link JsonBuilder#createArray()}
     */
    @SuppressWarnings("unchecked")
    public <E> E parse(JsonInput input) {
        JsonScanner scanner = new JsonScanner(input);
        Object root;
        scanner.nextImportantChar();
        switch (scanner.lastReadChar) {
            case '{':
                root = builder.createObject();
                parseObject(scanner, root);
                break;
            case '[':
                root = builder.createArray();
                parseArray(scanner, root);
                break;
            default:
                throw scanner.jsonException("Expected { or [");
        }
        return (E) root;
    }

    @SuppressWarnings("unchecked")
    private void parseObject(JsonScanner scanner, Object object) {
        for (; ; ) {
            switch (scanner.nextImportantChar()) {
                case '"':
                    final String attr = scanner.consumeString();
                    if (scanner.lastReadChar != ':') {
                        throw scanner.jsonException("Expected :");
                    }
                    switch (scanner.nextImportantChar()) {
                        case '"':
                            builder.addStringToObject(object, attr, scanner.consumeString());
                            break;
                        case '{':
                            final Object subObject = builder.createObject();
                            parseObject(scanner, subObject);
                            builder.addObjectToObject(object, attr, subObject);
                            scanner.nextImportantChar();
                            break;
                        case '[':
                            final Object subArray = builder.createArray();
                            parseArray(scanner, subArray);
                            builder.addArrayToObject(object, attr, subArray);
                            scanner.nextImportantChar();
                            break;
                        case 't':
                            scanner.consumeTrue();
                            builder.addBooleanToObject(object, attr, Boolean.TRUE);
                            break;
                        case 'f':
                            scanner.consumeFalse();
                            builder.addBooleanToObject(object, attr, Boolean.FALSE);
                            break;
                        case 'n':
                            scanner.consumeNull();
                            builder.addNullToObject(object, attr);
                            break;
                        case '-':
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            builder.addNumberToObject(object, attr, scanner.consumeNumber());
                            break;
                        default:
                            throw scanner.jsonException("Expected \" or number or boolean or null");
                    }
                    break;
                case '}':
                    return;
                default:
                    throw scanner.jsonException("Expected \"");
            }
            switch (scanner.lastReadChar) {
                case ',':
                    continue;
                case '}':
                    return;
                default:
                    throw scanner.jsonException("Expected } or ,");
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void parseArray(JsonScanner scanner, Object array) {
        for (; ; ) {
            switch (scanner.nextImportantChar()) {
                case '"':
                    builder.addStringToArray(array, scanner.consumeString());
                    break;
                case '{':
                    final Object subObject = builder.createObject();
                    parseObject(scanner, subObject);
                    builder.addObjectToArray(array, subObject);
                    scanner.nextImportantChar();
                    break;
                case '[':
                    final Object jsonSubArray = builder.createArray();
                    parseArray(scanner, jsonSubArray);
                    builder.addArrayToArray(array, jsonSubArray);
                    scanner.nextImportantChar();
                    break;
                case 't':
                    scanner.consumeTrue();
                    builder.addBooleanToArray(array, Boolean.TRUE);
                    break;
                case 'f':
                    scanner.consumeFalse();
                    builder.addBooleanToArray(array, Boolean.FALSE);
                    break;
                case 'n':
                    scanner.consumeNull();
                    builder.addNullToArray(array);
                    break;
                case '-':
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    builder.addNumberToArray(array, scanner.consumeNumber());
                    break;
                case ']':
                    return;
                default:
                    throw scanner.jsonException("Expected \" or ] or number or boolean or null");
            }
            switch (scanner.lastReadChar) {
                case ',':
                    continue;
                case ']':
                    return;
                default:
                    throw scanner.jsonException("Expected ] or ,");
            }
        }
    }
}
