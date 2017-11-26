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

import org.cuberact.json.JsonException;
import org.junit.Assert;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

/**
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public class JsonParseErrorTest {

    @Test
    public void testError01() {
        String errorJson = "a{\"name\" : \"jack\", \"array\" : [1, 2.34, \"text\", null], \"enabled\" : true, \"disabled\" : false, \"house\" : null}";
        expectedErrorOnPosition(errorJson, 1);
    }

    @Test
    public void testError02() {
        String errorJson = "{name\" : \"jack\", \"array\" : [1, 2.34, \"text\", null], \"enabled\" : true, \"disabled\" : false, \"house\" : null}";
        expectedErrorOnPosition(errorJson, 2);
    }

    @Test
    public void testError03() {
        String errorJson = "{\"name : \"jack\", \"array\" : [1, 2.34, \"text\", null], \"enabled\" : true, \"disabled\" : false, \"house\" : null}";
        expectedErrorOnPosition(errorJson, 11);
    }

    @Test
    public void testError04() {
        String errorJson = "{\"name\" \"jack\", \"array\" : [1, 2.34, \"text\", null], \"enabled\" : true, \"disabled\" : false, \"house\" : null}";
        expectedErrorOnPosition(errorJson, 9);
    }

    @Test
    public void testError05() {
        String errorJson = "{\"name\" : jack\", \"array\" : [1, 2.34, \"text\", null], \"enabled\" : true, \"disabled\" : false, \"house\" : null}";
        expectedErrorOnPosition(errorJson, 11);
    }

    @Test
    public void testError06() {
        String errorJson = "{\"name\" : \"jack\", \"array\" : 1, 2.34, \"text\", null], \"enabled\" : true, \"disabled\" : false, \"house\" : null}";
        expectedErrorOnPosition(errorJson, 32);
    }

    @Test
    public void testError07() {
        String errorJson = "{\"name\" : \"jack\", \"array\" : [1a, 2.34, \"text\", null], \"enabled\" : true, \"disabled\" : false, \"house\" : null}";
        expectedErrorOnPosition(errorJson, 31);
    }

    @Test
    public void testError08() {
        String errorJson = "{\"name\" : \"jack\", \"array\" : [1 2.34, \"text\", null], \"enabled\" : true, \"disabled\" : false, \"house\" : null}";
        expectedErrorOnPosition(errorJson, 32);
    }

    @Test
    public void testError09() {
        String errorJson = "{\"name\" : \"jack\", \"array\" : [1, 2.a34, \"text\", null], \"enabled\" : true, \"disabled\" : false, \"house\" : null}";
        expectedErrorOnPosition(errorJson, 35);
    }

    @Test
    public void testError10() {
        String errorJson = "{\"name\" : \"jack\", \"array\" : [1, 2.34 \"text\", null], \"enabled\" : true, \"disabled\" : false, \"house\" : null}";
        expectedErrorOnPosition(errorJson, 38);
    }

    @Test
    public void testError11() {
        String errorJson = "{\"name\" : \"jack\", \"array\" : [1, 2.34, \"text\", Null], \"enabled\" : true, \"disabled\" : false, \"house\" : null}";
        expectedErrorOnPosition(errorJson, 47);
    }

    @Test
    public void testError12() {
        String errorJson = "{\"name\" : \"jack\", \"array\" : [1, 2.34, \"text\", nUll], \"enabled\" : true, \"disabled\" : false, \"house\" : null}";
        expectedErrorOnPosition(errorJson, 48);
    }

    @Test
    public void testError13() {
        String errorJson = "{\"name\" : \"jack\", \"array\" : [1, 2.34, \"text\", null, \"enabled\" : true, \"disabled\" : false, \"house\" : null}";
        expectedErrorOnPosition(errorJson, 63);
    }

    @Test
    public void testError14() {
        String errorJson = "{\"name\" : \"jack\", \"array\" : [1, 2.34, \"text\", null], \"enabled\" : tRue, \"disabled\" : false, \"house\" : null}";
        expectedErrorOnPosition(errorJson, 67);
    }

    @Test
    public void testError15() {
        String errorJson = "{\"name\" : \"jack\", \"array\" : [1, 2.34, \"text\", null], \"enabled\" : true, \"disabled\" : falSe, \"house\" : null}";
        expectedErrorOnPosition(errorJson, 88);
    }

    @Test
    public void testError16() {
        String errorJson = "{\"name\" : \"jack\", \"array\" : [1, 2.34, \"text\", null], \"enabled\" : true, \"disabled\" : falsea, \"house\" : null}";
        expectedErrorOnPosition(errorJson, 90);
    }

    @Test
    public void testError17() {
        String errorJson = "{\"name\" : \"jack\", \"array\" : [1, 2.34, \"text\", null], \"enabled\" : true, \"disabled\" : false, \"house\" : }";
        expectedErrorOnPosition(errorJson, 102);
    }

    @Test
    public void testError18() {
        String errorJson = "{\"name\" : \"jack\", \"array\" : [1, 2.34, \"text\", null], \"enabled\" : true, \"disabled\" : false, \"house\" : null ";
        expectedErrorOnPosition(errorJson, 107);
    }

    @Test
    public void testError19() {
        String errorJson = "{\"number\" : - 12}";
        expectedErrorOnPosition(errorJson, 14);
    }

    @Test
    public void testError20() {
        String errorJson = "{\"number\" : +12}";
        expectedErrorOnPosition(errorJson, 13);
    }

    @Test
    public void testError21() {
        String errorJson = "{\"number\" : -12.23f+32}";
        expectedErrorOnPosition(errorJson, 19);
    }

    private void expectedErrorOnPosition(String errorJson, int expectedErrorOnPos) {
        try {
            useJsonInputCharSequence(errorJson);
            Assert.fail("Expected JsonException");
        } catch (JsonException e) {
            assertErrorInJsonException(e, expectedErrorOnPos);
        }
    }

    private void assertErrorInJsonException(JsonException jsonException, int expectedErrorOnPos) {
        //System.out.println(jsonException.getMessage());
        String prefix = "Parse error on position ";
        Pattern p = Pattern.compile(prefix + "[0-9]+");
        Matcher m = p.matcher(jsonException.getMessage());
        if (!m.find()) {
            Assert.fail("JsonException message doesn't contains expected text - " + prefix + expectedErrorOnPos);
        }
        String text = m.group();
        assertEquals(prefix + expectedErrorOnPos, text);
    }

    private void useJsonInputCharSequence(String jsonAsString) {
        new JsonParser().parse(jsonAsString);
    }
}
