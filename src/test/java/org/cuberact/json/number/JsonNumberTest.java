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

import org.cuberact.json.JsonException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public class JsonNumberTest {

    @Test
    public void equals() {
        JsonNumber jsonNumber1 = new JsonNumber("12345.67E+13".toCharArray(), 12, true);
        JsonNumber jsonNumber2 = new JsonNumber("12345.67E+13".toCharArray(), 12, true);
        assertTrue(jsonNumber1.equals(jsonNumber2));
    }

    @Test
    public void notEquals() {
        JsonNumber jsonNumber1 = new JsonNumber("12345".toCharArray(), 5, true);
        JsonNumber jsonNumber2 = new JsonNumber("12345".toCharArray(), 5, false);
        Assert.assertFalse(jsonNumber1.equals(jsonNumber2));
    }

    @Test
    public void hashCodeEquals() {
        JsonNumber jsonNumber1 = new JsonNumber("12345.67E+13".toCharArray(), 12, true);
        JsonNumber jsonNumber2 = new JsonNumber("12345.67E+13".toCharArray(), 12, true);
        assertEquals(jsonNumber1.hashCode(), jsonNumber2.hashCode());
    }

    @Test
    public void hashCodeNotEquals() {
        JsonNumber jsonNumber1 = new JsonNumber("12345".toCharArray(), 5, true);
        JsonNumber jsonNumber2 = new JsonNumber("12345".toCharArray(), 5, false);
        assertNotEquals(jsonNumber1.hashCode(), jsonNumber2.hashCode());
    }

    @Test
    public void subSequence() {
        JsonNumber jsonNumber = new JsonNumber("12345".toCharArray(), 5, true);
        CharSequence subSequence = jsonNumber.subSequence(1, 3);
        assertEquals("23", subSequence.toString());
        assertEquals("12345".subSequence(1, 3), subSequence);
    }

    @Test(expected = JsonException.class)
    public void subSequenceError1() {
        JsonNumber jsonNumber = new JsonNumber("12345".toCharArray(), 5, true);
        jsonNumber.subSequence(-1, 3);
    }

    @Test(expected = JsonException.class)
    public void subSequenceError2() {
        JsonNumber jsonNumber = new JsonNumber("12345".toCharArray(), 5, true);
        jsonNumber.subSequence(0, 99);
    }

    @Test(expected = JsonException.class)
    public void subSequenceError3() {
        JsonNumber jsonNumber = new JsonNumber("12345".toCharArray(), 5, true);
        jsonNumber.subSequence(3, 1);
    }
}
