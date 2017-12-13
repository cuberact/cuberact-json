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

package org.cuberact.json.output;

import org.cuberact.json.JsonException;

import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Output with OutputStream
 *
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public class JsonOutputStream<E extends OutputStream> extends JsonOutputBase<E> {

    private final OutputStream stream;
    private final Charset charset;

    public JsonOutputStream(E stream) {
        this(stream, StandardCharsets.UTF_8);
    }

    public JsonOutputStream(OutputStream stream, Charset charset) {
        this.stream = Objects.requireNonNull(stream, "stream");
        this.charset = Objects.requireNonNull(charset, "charset");
    }

    @Override
    protected void writeChars(char[] chars, int len) {
        ByteBuffer encode = charset.encode(CharBuffer.wrap(chars, 0, len));
        try {
            stream.write(encode.array(), 0, encode.remaining());
        } catch (Throwable e) {
            throw new JsonException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public E getResult() {
        flushBuffer();
        return (E) stream;
    }
}
