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

package org.cuberact.json.builder.number;

import org.cuberact.json.JsonException;

/**
 * Char array to Number converter
 *
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public interface NumberConverter {

    /**
     * Convert char array to whole number
     *
     * @param number - char array containing digits as char
     * @param offset - index of first digit
     * @param count  - digits count
     * @return number - usually Integer or Long, but can be BigInteger or else number representation
     * @throws Throwable - throw anything, will be translated to {@link JsonException} automatically
     */
    Number convertWholeNumber(char[] number, int offset, int count) throws Throwable;

    /**
     * Convert char array to floating-point number
     *
     * @param number - char array containing digits as char
     * @param offset - index of first digit
     * @param count  - digits count
     * @return number - usually Float or Double, but can be BigDecimal or else number representation
     * @throws Throwable - throw anything, will be translated to {@link JsonException} automatically
     */
    Number convertFloatingPointNumber(char[] number, int offset, int count) throws Throwable;
}
