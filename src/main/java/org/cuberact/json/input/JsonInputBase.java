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

/**
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public abstract class JsonInputBase implements JsonInput {

    protected char actualChar;

    @Override
    public char actualChar() {
        return actualChar;
    }

    @Override
    public char nextImportantChar() {
        for (; ; ) {
            if (!isWhiteChar(nextChar())) {
                return actualChar();
            }
        }
    }

    protected boolean isWhiteChar(char c) {
        return c == ' ' || c == '\n' || c == '\r' || c == '\t';
    }
}
