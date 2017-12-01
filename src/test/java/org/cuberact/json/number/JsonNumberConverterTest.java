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

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public class JsonNumberConverterTest {

    @Test
    public void floatExample01() {
        char[] input = "-12.345e-30".toCharArray();
        JsonNumber jsonNumber = new JsonNumber(input, input.length, true);
        Number number = JsonNumberConverterIntFloat.REF.convert(jsonNumber);
        assertEquals(Float.class, number.getClass());
        assertEquals("-1.2345E-29", number.toString());
    }

    @Test
    public void doubleExample01() {
        char[] input = "-12.345e+30".toCharArray();
        JsonNumber jsonNumber = new JsonNumber(input, input.length, true);
        Number number = JsonNumberConverterLongDouble.REF.convert(jsonNumber);
        assertEquals(Double.class, number.getClass());
        assertEquals("-1.2345E31", number.toString());
    }

    @Test
    public void intExample01() {
        char[] input = "-12".toCharArray();
        JsonNumber jsonNumber = new JsonNumber(input, input.length, false);
        Number number = JsonNumberConverterIntFloat.REF.convert(jsonNumber);
        assertEquals(Integer.class, number.getClass());
        assertEquals("-12", number.toString());
    }

    @Test
    public void longExample01() {
        char[] input = "123456".toCharArray();
        JsonNumber jsonNumber = new JsonNumber(input, input.length, false);
        Number number = JsonNumberConverterLongDouble.REF.convert(jsonNumber);
        assertEquals(Long.class, number.getClass());
        assertEquals("123456", number.toString());
    }
}
