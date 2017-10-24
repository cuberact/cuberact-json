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
 * JsonInput from CharSequence (String, StringBuilder etc.)
 *
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public class JsonInputCharSequence extends JsonInputBase {

    private final CharSequence input;
    private final int maxPosition;
    private int position = 0;

    public JsonInputCharSequence(CharSequence input) {
        this.input = Objects.requireNonNull(input, "input");
        this.maxPosition = this.input.length();
    }

    public char nextChar() {
        try {
            actualChar = input.charAt(position++);
        } catch (StringIndexOutOfBoundsException e) {
            actualChar = END_OF_INPUT;
        }
        return actualChar;
    }

    public void readLastCharAgain() {
        position--;
    }

    public String buildExceptionMessage(String error) {
        StringBuilder message = new StringBuilder("Parse error on position ");
        message.append(position).append("\n\n");
        try {
            int errorPosition = position - 1;
            int arrowPosition = specialSubStringFromInput(errorPosition - 40, errorPosition, message);
            specialSubStringFromInput(errorPosition, errorPosition + 40, message);
            message.append("\n");
            for (int i = 0; i < arrowPosition; i++) {
                message.append(".");
            }
            if (errorPosition >= maxPosition) {
                message.append(".");
            }
            message.append("^ ERROR - ").append(error).append("\n");
        } catch (Throwable t) {
            message.append(" [pointer build failed]\n");
        }
        return message.toString();
    }

    private int specialSubStringFromInput(int start, int end, StringBuilder output) {
        int counter = 0;
        for (int i = start; i < end; i++) {
            if (i >= 0 && i < maxPosition) {
                char value = input.charAt(i);
                if (!isWhiteChar(value)) {
                    output.append(value);
                    counter++;
                }
            }
        }
        return counter;
    }
}
