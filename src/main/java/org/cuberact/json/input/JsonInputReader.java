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

import java.io.IOException;
import java.io.Reader;
import java.util.Objects;

/**
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public class JsonInputReader implements JsonInput {

    private final Reader input;

    /**
     * @param input - {@link Reader}
     */
    public JsonInputReader(Reader input) {
        this.input = Objects.requireNonNull(input, "input");
    }

    @Override
    public char nextChar() {
        try {
            return (char) input.read();
        } catch (IOException e) {
            return END_OF_INPUT;
        }
    }
}
