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

import org.cuberact.json.optimize.CharTable;

import static org.cuberact.json.optimize.CharTable.toLong;

/**
 * Number converter to Long and Double
 *
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public class NumberConverterLongDouble implements NumberConverter {

    public static final NumberConverterLongDouble REF = new NumberConverterLongDouble();

    /**
     * @return number as Long
     * {@link NumberConverter#convertWholeNumber(char[], int, int)}
     */
    @Override
    public Number convertWholeNumber(char[] number, int offset, int count) throws Throwable {
        long result = 0;
        long sign = 1;
        char c = number[offset];
        if (c == '-') {
            sign = -1;
        } else {
            result = toLong(c);
        }
        for (int i = offset + 1; i < count; i++) {
            result = result * 10 + toLong(number[i]);
        }
        return sign * result;
    }

    /**
     * @return number as Double
     * {@link NumberConverter#convertFloatingPointNumber(char[], int, int)}
     */
    @Override
    public Number convertFloatingPointNumber(char[] number, int offset, int count) throws Throwable {
        return Double.parseDouble(new String(number, offset, count));
    }
}
