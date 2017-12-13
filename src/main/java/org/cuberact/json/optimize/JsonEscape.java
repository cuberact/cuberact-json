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

package org.cuberact.json.optimize;

import org.cuberact.json.output.JsonOutput;

/**
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public class JsonEscape {

    private static final String[] CHAR_ESC = new String[128];

    static {
        for (int i = 0; i < 32; i++) {
            String hex = Integer.toHexString(i);
            if (hex.length() == 1) {
                CHAR_ESC[i] = "\\u000" + hex;
            } else {
                CHAR_ESC[i] = "\\u00" + hex;
            }
        }
        CHAR_ESC['"'] = "\\\"";
        CHAR_ESC['\\'] = "\\\\";
        CHAR_ESC['\b'] = "\\b";
        CHAR_ESC['\f'] = "\\f";
        CHAR_ESC['\n'] = "\\n";
        CHAR_ESC['\r'] = "\\r";
        CHAR_ESC['\t'] = "\\t";
    }

    public static void escape(CharSequence input, JsonOutput output) {
        for (int i = 0, len = input.length(); i < len; i++) {
            char c = input.charAt(i);
            if (c < 128 && CHAR_ESC[c] != null) {
                output.write(CHAR_ESC[c]);
            } else {
                output.write(c);
            }
        }
    }
}
