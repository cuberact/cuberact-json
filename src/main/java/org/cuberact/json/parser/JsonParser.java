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
import org.cuberact.json.JsonGlobalConfig;
import org.cuberact.json.builder.JsonBuilder;
import org.cuberact.json.builder.JsonBuilderTree;
import org.cuberact.json.input.JsonInput;
import org.cuberact.json.input.JsonInputCharSequence;
import org.cuberact.json.input.JsonInputReader;
import org.cuberact.json.number.NumberConverter;

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
    private final NumberConverter numberConverter;

    public JsonParser() {
        this(JsonGlobalConfig.DEFAULT_BUILDER, JsonGlobalConfig.DEFAULT_NUMBER_CONVERTER);
    }

    public JsonParser(JsonBuilder builder, NumberConverter numberConverter) {
        this.builder = Objects.requireNonNull(builder, "builder");
        this.numberConverter = Objects.requireNonNull(numberConverter, "numberConverter");
    }

    /**
     * @param input - {@link CharSequence}
     * @param <E>   - {@link JsonBuilder#createJsonObject()} or {@link JsonBuilder#createJsonArray()}
     * @return JsonBuilder result - {@link JsonBuilder#createJsonObject()} or {@link JsonBuilder#createJsonArray()}
     */
    public <E> E parse(CharSequence input) {
        return parse(new JsonInputCharSequence(input));
    }

    /**
     * @param input - {@link Reader} - JsonParser doesn't close input reader
     * @param <E>   - {@link JsonBuilder#createJsonObject()} or {@link JsonBuilder#createJsonArray()}
     * @return JsonBuilder result - {@link JsonBuilder#createJsonObject()} or {@link JsonBuilder#createJsonArray()}
     */
    public <E> E parse(Reader input) {
        return parse(new JsonInputReader(input));
    }

    /**
     * @param input - {@link JsonInput}
     * @param <E>   - {@link JsonBuilder#createJsonObject()} or {@link JsonBuilder#createJsonArray()}
     * @return JsonBuilder result - {@link JsonBuilder#createJsonObject()} or {@link JsonBuilder#createJsonArray()}
     */
    @SuppressWarnings("unchecked")
    public <E> E parse(JsonInput input) {
        JsonScanner scanner = new JsonScanner(input);
        Object root;
        switch (scanner.nextImportantChar()) {
            case '{':
                root = builder.createJsonObject();
                parseObject(scanner, root);
                break;
            case '[':
                root = builder.createJsonArray();
                parseArray(scanner, root);
                break;
            default:
                throw new JsonException(scanner.error("Expected { or ["));
        }
        return (E) root;
    }

    private void parseObject(JsonScanner scanner, Object jsonObject) {
        for (; ; ) {
            switch (scanner.nextImportantChar()) {
                case '"':
                    final String attr = scanner.consumeString();
                    if (scanner.lastReadChar() != ':') {
                        throw new JsonException(scanner.error("Expected :"));
                    }
                    switch (scanner.nextImportantChar()) {
                        case '"':
                            builder.addToJsonObject(jsonObject, attr, scanner.consumeString());
                            break;
                        case '{':
                            final Object jsonSubObject = builder.createJsonObject();
                            builder.addToJsonObject(jsonObject, attr, jsonSubObject);
                            parseObject(scanner, jsonSubObject);
                            scanner.nextImportantChar();
                            break;
                        case '[':
                            final Object jsonSubArray = builder.createJsonArray();
                            builder.addToJsonObject(jsonObject, attr, jsonSubArray);
                            parseArray(scanner, jsonSubArray);
                            scanner.nextImportantChar();
                            break;
                        case 't':
                            scanner.consumeTrue();
                            builder.addToJsonObject(jsonObject, attr, Boolean.TRUE);
                            break;
                        case 'f':
                            scanner.consumeFalse();
                            builder.addToJsonObject(jsonObject, attr, Boolean.FALSE);
                            break;
                        case 'n':
                            scanner.consumeNull();
                            builder.addToJsonObject(jsonObject, attr, null);
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
                            builder.addToJsonObject(jsonObject, attr, scanner.consumeNumber(numberConverter));
                            break;
                        default:
                            throw new JsonException(scanner.error("Expected \" or number or boolean or null"));
                    }
                    break;
                case '}':
                    return;
                default:
                    throw new JsonException(scanner.error("Expected \""));
            }
            switch (scanner.lastReadChar()) {
                case ',':
                    continue;
                case '}':
                    return;
                default:
                    throw new JsonException(scanner.error("Expected } or ,"));
            }
        }
    }

    private void parseArray(JsonScanner scanner, Object jsonArray) {
        for (; ; ) {
            switch (scanner.nextImportantChar()) {
                case '"':
                    builder.addToJsonArray(jsonArray, scanner.consumeString());
                    break;
                case '{':
                    final Object jsonSubObject = builder.createJsonObject();
                    builder.addToJsonArray(jsonArray, jsonSubObject);
                    parseObject(scanner, jsonSubObject);
                    scanner.nextImportantChar();
                    break;
                case '[':
                    final Object jsonSubArray = builder.createJsonArray();
                    builder.addToJsonArray(jsonArray, jsonSubArray);
                    parseArray(scanner, jsonSubArray);
                    scanner.nextImportantChar();
                    break;
                case 't':
                    scanner.consumeTrue();
                    builder.addToJsonArray(jsonArray, Boolean.TRUE);
                    break;
                case 'f':
                    scanner.consumeFalse();
                    builder.addToJsonArray(jsonArray, Boolean.FALSE);
                    break;
                case 'n':
                    scanner.consumeNull();
                    builder.addToJsonArray(jsonArray, null);
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
                    builder.addToJsonArray(jsonArray, scanner.consumeNumber(numberConverter));
                    break;
                case ']':
                    return;
                default:
                    throw new JsonException(scanner.error("Expected \" or ] or number or boolean or null"));
            }
            switch (scanner.lastReadChar()) {
                case ',':
                    continue;
                case ']':
                    return;
                default:
                    throw new JsonException(scanner.error("Expected ] or ,"));
            }
        }
    }


}
