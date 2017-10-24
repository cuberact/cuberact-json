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
import org.cuberact.json.builder.JsonBuilder;
import org.cuberact.json.builder.JsonBuilderTree;
import org.cuberact.json.input.JsonInput;
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
public class JsonParser {

    private final JsonBuilder builder;

    public JsonParser() {
        this(JsonBuilder.DEFAULT);
    }

    public JsonParser(JsonBuilder builder) {
        this.builder = Objects.requireNonNull(builder, "builder");
        Objects.requireNonNull(builder.getNumberConverter(), "numberConverter");
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
        Object root;
        switch (input.nextImportantChar()) {
            case '{':
                root = builder.createJsonObject();
                parseObject(input, root);
                break;
            case '[':
                root = builder.createJsonArray();
                parseArray(input, root);
                break;
            default:
                throw new JsonException(input.buildExceptionMessage("Expected { or ["));
        }
        return (E) root;
    }

    private void parseObject(JsonInput input, Object jsonObject) {
        for (; ; ) {
            switch (input.nextImportantChar()) {
                case '"':
                    final String attr = Token.consumeString(input);
                    if (input.nextImportantChar() != ':') {
                        throw new JsonException(input.buildExceptionMessage("Expected :"));
                    }
                    switch (input.nextImportantChar()) {
                        case '"':
                            builder.addToJsonObject(jsonObject, attr, Token.consumeString(input));
                            break;
                        case '{':
                            final Object jsonSubObject = builder.createJsonObject();
                            builder.addToJsonObject(jsonObject, attr, jsonSubObject);
                            parseObject(input, jsonSubObject);
                            break;
                        case '[':
                            final Object jsonSubArray = builder.createJsonArray();
                            builder.addToJsonObject(jsonObject, attr, jsonSubArray);
                            parseArray(input, jsonSubArray);
                            break;
                        case 't':
                            Token.consumeTrue(input);
                            builder.addToJsonObject(jsonObject, attr, Boolean.TRUE);
                            break;
                        case 'f':
                            Token.consumeFalse(input);
                            builder.addToJsonObject(jsonObject, attr, Boolean.FALSE);
                            break;
                        case 'n':
                            Token.consumeNull(input);
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
                            builder.addToJsonObject(jsonObject, attr, Token.consumeNumber(input, builder.getNumberConverter()));
                            break;
                        default:
                            throw new JsonException(input.buildExceptionMessage("Expected \" or ] or number or boolean or null"));
                    }
                    break;
                case '}':
                    return;
                default:
                    throw new JsonException(input.buildExceptionMessage("Expected \""));
            }
            switch (input.nextImportantChar()) {
                case ',':
                    continue;
                case '}':
                    return;
                default:
                    throw new JsonException(input.buildExceptionMessage("Expected } or ,"));
            }
        }
    }

    private void parseArray(JsonInput input, Object jsonArray) {
        for (; ; ) {
            switch (input.nextImportantChar()) {
                case '"':
                    builder.addToJsonArray(jsonArray, Token.consumeString(input));
                    break;
                case '{':
                    final Object jsonSubObject = builder.createJsonObject();
                    builder.addToJsonArray(jsonArray, jsonSubObject);
                    parseObject(input, jsonSubObject);
                    break;
                case '[':
                    final Object jsonSubArray = builder.createJsonArray();
                    builder.addToJsonArray(jsonArray, jsonSubArray);
                    parseArray(input, jsonSubArray);
                    break;
                case 't':
                    Token.consumeTrue(input);
                    builder.addToJsonArray(jsonArray, Boolean.TRUE);
                    break;
                case 'f':
                    Token.consumeFalse(input);
                    builder.addToJsonArray(jsonArray, Boolean.FALSE);
                    break;
                case 'n':
                    Token.consumeNull(input);
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
                    builder.addToJsonArray(jsonArray, Token.consumeNumber(input, builder.getNumberConverter()));
                    break;
                case ']':
                    return;
                default:
                    throw new JsonException(input.buildExceptionMessage("Expected \" or ] or number or boolean or null"));
            }
            switch (input.nextImportantChar()) {
                case ',':
                    continue;
                case ']':
                    return;
                default:
                    throw new JsonException(input.buildExceptionMessage("Expected ] or ,"));
            }
        }
    }
}
