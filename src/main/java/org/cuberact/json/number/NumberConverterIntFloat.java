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

import static org.cuberact.json.optimize.CharTable.toInt;

/**
 * Number converter to Integer and Float
 *
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public class NumberConverterIntFloat implements NumberConverter {

    public static final NumberConverterIntFloat REF = new NumberConverterIntFloat();

    /**
     * @return number as Integer
     * {@link NumberConverter#convertWholeNumber(char[], int, int)}
     */
    @Override
    public Number convertWholeNumber(char[] number, int offset, int count) throws Throwable {
        int result = 0;
        int sign = 1;
        char c = number[offset];
        if (c == '-') {
            sign = -1;
        } else {
            result = toInt(c);
        }
        for (int i = offset + 1; i < count; i++) {
            result = result * 10 + toInt(number[i]);
        }
        return sign * result;
    }

    /**
     * @return number as Float
     * {@link NumberConverter#convertFloatingPointNumber(char[], int, int)}
     */
    @Override
    public Number convertFloatingPointNumber(char[] number, int offset, int count) throws Throwable {
        return Float.parseFloat(new String(number, offset, count));
    }
}
