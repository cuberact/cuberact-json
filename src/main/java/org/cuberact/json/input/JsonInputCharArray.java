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

package org.cuberact.json.input;

import java.util.Objects;

/**
 * JsonInput from char array
 *
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public final class JsonInputCharArray implements JsonInput {

    private final char[] input;
    private int position;

    public JsonInputCharArray(char[] input) {
        this.input = Objects.requireNonNull(input, "input");
    }

    public char nextChar() {
        try {
            char c = input[position];
            position++;
            return c;
        } catch (ArrayIndexOutOfBoundsException e) {
            return END_OF_INPUT;
        }
    }

    @Override
    public int position() {
        return position;
    }
}
