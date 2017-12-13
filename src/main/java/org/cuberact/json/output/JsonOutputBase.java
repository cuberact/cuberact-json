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

/**
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public abstract class JsonOutputBase<E> implements JsonOutput<E> {

    private final int bufferSize = 4096;
    private final char[] buffer = new char[bufferSize];
    private int position;

    @Override
    public final void write(CharSequence data) {
        for (int i = 0; i < data.length(); i++) {
            write(data.charAt(i));
        }
    }

    @Override
    public final void write(char data) {
        buffer[position++] = data;
        if (position == bufferSize) flushBuffer();
    }

    @Override
    public void flushBuffer() {
        if (position != 0) {
            writeChars(buffer, position);
            position = 0;
        }
    }

    protected abstract void writeChars(char[] chars, int len);

    @Override
    public abstract E getResult();
}
