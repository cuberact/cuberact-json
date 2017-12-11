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

package org.cuberact.json.number;

import static org.cuberact.json.optimize.CharTable.toLong;

/**
 * JsonNumber converter to Long or Double
 *
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public final class JsonNumberConverterLongDouble implements JsonNumberConverter {

    public static final JsonNumberConverterLongDouble REF = new JsonNumberConverterLongDouble();

    private JsonNumberConverterLongDouble() {
        //use REF instead
    }

    /**
     * @return JsonNumber as Long or Double
     */
    @Override
    public Number convert(JsonNumber jsonNumber) {
        if (jsonNumber.isFloatingNumber()) {
            return Double.parseDouble(jsonNumber.toString());
        }
        long result = 0L;
        long sign = 1L;
        char c = jsonNumber.charAt(0);
        if (c == '-') {
            sign = -1L;
        } else {
            result = toLong(c);
        }
        for (int i = 1; i < jsonNumber.length(); i++) {
            result = result * 10L + toLong(jsonNumber.charAt(i));
        }
        return sign * result;
    }
}
