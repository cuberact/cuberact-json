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

package org.cuberact.json.parser;

import org.junit.Test;
import org.cuberact.json.number.NumberConverterLongDouble;
import org.cuberact.json.input.JsonInputCharSequence;

import static org.junit.Assert.assertEquals;

/**
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public class TokenNumberTest {

    @Test
    public void validDouble1() {
        String doubleAsString = "-12.345e30";
        Number number = convertByTokenNumber(doubleAsString);
        assertEquals(Double.class, number.getClass());
        assertEquals("-1.2345E31", number.toString());
    }

    @Test
    public void validDouble2() {
        String doubleAsString = "-12.345e+30";
        Number number = convertByTokenNumber(doubleAsString);
        assertEquals(Double.class, number.getClass());
        assertEquals("-1.2345E31", number.toString());
    }

    @Test
    public void validDouble3() {
        String doubleAsString = "-12.345e-30";
        Number number = convertByTokenNumber(doubleAsString);
        assertEquals(Double.class, number.getClass());
        assertEquals("-1.2345E-29", number.toString());
    }

    @Test
    public void validDouble4() {
        String doubleAsString = "-12.1";
        Number number = convertByTokenNumber(doubleAsString);
        assertEquals(Double.class, number.getClass());
        assertEquals("-12.1", number.toString());
    }

    @Test
    public void validDouble5() {
        String doubleAsString = "-12.0";
        Number number = convertByTokenNumber(doubleAsString);
        assertEquals(Double.class, number.getClass());
        assertEquals("-12.0", number.toString());
    }

    @Test
    public void validDouble6() {
        String doubleAsString = "5.2";
        Number number = convertByTokenNumber(doubleAsString);
        assertEquals(Double.class, number.getClass());
        assertEquals("5.2", number.toString());
    }

    private Number convertByTokenNumber(String json) {
        JsonInputCharSequence input = new JsonInputCharSequence(json);
        JsonScanner scanner = new JsonScanner(input);
        scanner.nextImportantChar();
        return Token.consumeNumber(scanner, NumberConverterLongDouble.REF);
    }
}
