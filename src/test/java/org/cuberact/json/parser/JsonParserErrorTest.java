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
import org.cuberact.json.input.JsonInputCharArray;
import org.cuberact.json.input.JsonInputReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public class JsonParserErrorTest {

    @Test
    public void testError01() {
        String errorJson = "a{\"name\" : \"jack\", \"array\" : [1, 2.34, \"text\", null], \"enabled\" : true, \"disabled\" : false, \"house\" : null}";
        tryParseJsonAndCheckExceptionMessage(errorJson, "Parse error on position 1 - Expected { or [");
    }

    @Test
    public void testError02() {
        String errorJson = "{name\" : \"jack\", \"array\" : [1, 2.34, \"text\", null], \"enabled\" : true, \"disabled\" : false, \"house\" : null}";
        tryParseJsonAndCheckExceptionMessage(errorJson, "Parse error on position 2 - Expected \"");
    }

    @Test
    public void testError03() {
        String errorJson = "{\"name : \"jack\", \"array\" : [1, 2.34, \"text\", null], \"enabled\" : true, \"disabled\" : false, \"house\" : null}";
        tryParseJsonAndCheckExceptionMessage(errorJson, "Parse error on position 11 - Expected :");
    }

    @Test
    public void testError04() {
        String errorJson = "{\"name\" \"jack\", \"array\" : [1, 2.34, \"text\", null], \"enabled\" : true, \"disabled\" : false, \"house\" : null}";
        tryParseJsonAndCheckExceptionMessage(errorJson, "Parse error on position 9 - Expected :");
    }

    @Test
    public void testError05() {
        String errorJson = "{\"name\" : jack\", \"array\" : [1, 2.34, \"text\", null], \"enabled\" : true, \"disabled\" : false, \"house\" : null}";
        tryParseJsonAndCheckExceptionMessage(errorJson, "Parse error on position 11 - Expected \" or number or boolean or null");
    }

    @Test
    public void testError06() {
        String errorJson = "{\"name\" : \"jack\", \"array\" : 1, 2.34, \"text\", null], \"enabled\" : true, \"disabled\" : false, \"house\" : null}";
        tryParseJsonAndCheckExceptionMessage(errorJson, "Parse error on position 32 - Expected \"");
    }

    @Test
    public void testError07() {
        String errorJson = "{\"name\" : \"jack\", \"array\" : [1a, 2.34, \"text\", null], \"enabled\" : true, \"disabled\" : false, \"house\" : null}";
        tryParseJsonAndCheckExceptionMessage(errorJson, "Parse error on position 31 - Expected ] or ,");
    }

    @Test
    public void testError08() {
        String errorJson = "{\"name\" : \"jack\", \"array\" : [1 2.34, \"text\", null], \"enabled\" : true, \"disabled\" : false, \"house\" : null}";
        tryParseJsonAndCheckExceptionMessage(errorJson, "Parse error on position 32 - Expected ] or ,");
    }

    @Test
    public void testError09() {
        String errorJson = "{\"name\" : \"jack\", \"array\" : [1, 2.a34, \"text\", null], \"enabled\" : true, \"disabled\" : false, \"house\" : null}";
        tryParseJsonAndCheckExceptionMessage(errorJson, "Parse error on position 35 - Expected ] or ,");
    }

    @Test
    public void testError10() {
        String errorJson = "{\"name\" : \"jack\", \"array\" : [1, 2.34 \"text\", null], \"enabled\" : true, \"disabled\" : false, \"house\" : null}";
        tryParseJsonAndCheckExceptionMessage(errorJson, "Parse error on position 38 - Expected ] or ,");
    }

    @Test
    public void testError11() {
        String errorJson = "{\"name\" : \"jack\", \"array\" : [1, 2.34, \"text\", Null], \"enabled\" : true, \"disabled\" : false, \"house\" : null}";
        tryParseJsonAndCheckExceptionMessage(errorJson, "Parse error on position 47 - Expected \" or ] or number or boolean or null");
    }

    @Test
    public void testError12() {
        String errorJson = "{\"name\" : \"jack\", \"array\" : [1, 2.34, \"text\", nUll], \"enabled\" : true, \"disabled\" : false, \"house\" : null}";
        tryParseJsonAndCheckExceptionMessage(errorJson, "Parse error on position 48 - Expected null");
    }

    @Test
    public void testError13() {
        String errorJson = "{\"name\" : \"jack\", \"array\" : [1, 2.34, \"text\", null, \"enabled\" : true, \"disabled\" : false, \"house\" : null}";
        tryParseJsonAndCheckExceptionMessage(errorJson, "Parse error on position 63 - Expected ] or ,");
    }

    @Test
    public void testError14() {
        String errorJson = "{\"name\" : \"jack\", \"array\" : [1, 2.34, \"text\", null], \"enabled\" : tRue, \"disabled\" : false, \"house\" : null}";
        tryParseJsonAndCheckExceptionMessage(errorJson, "Parse error on position 67 - Expected true");
    }

    @Test
    public void testError15() {
        String errorJson = "{\"name\" : \"jack\", \"array\" : [1, 2.34, \"text\", null], \"enabled\" : true, \"disabled\" : falSe, \"house\" : null}";
        tryParseJsonAndCheckExceptionMessage(errorJson, "Parse error on position 88 - Expected false");
    }

    @Test
    public void testError16() {
        String errorJson = "{\"name\" : \"jack\", \"array\" : [1, 2.34, \"text\", null], \"enabled\" : true, \"disabled\" : falsea, \"house\" : null}";
        tryParseJsonAndCheckExceptionMessage(errorJson, "Parse error on position 90 - Expected } or ,");
    }

    @Test
    public void testError17() {
        String errorJson = "{\"name\" : \"jack\", \"array\" : [1, 2.34, \"text\", null], \"enabled\" : true, \"disabled\" : false, \"house\" : }";
        tryParseJsonAndCheckExceptionMessage(errorJson, "Parse error on position 102 - Expected \" or number or boolean or null");
    }

    @Test
    public void testError18() {
        String errorJson = "{\"name\" : \"jack\", \"array\" : [1, 2.34, \"text\", null], \"enabled\" : true, \"disabled\" : false, \"house\" : null ";
        tryParseJsonAndCheckExceptionMessage(errorJson, "Parse error on position 106 - Expected } or ,");
    }

    @Test
    public void testError19() {
        String errorJson = "{\"number\" : - 12}";
        tryParseJsonAndCheckExceptionMessage(errorJson, "Parse error on position 14 - Expected correct number");
    }

    @Test
    public void testError20() {
        String errorJson = "{\"number\" : +12}";
        tryParseJsonAndCheckExceptionMessage(errorJson, "Parse error on position 13 - Expected \" or number or boolean or null");
    }

    @Test
    public void testError21() {
        String errorJson = "{\"number\" : -12.23f+32}";
        tryParseJsonAndCheckExceptionMessage(errorJson, "Parse error on position 19 - Expected } or ,");
    }

    @Test
    public void testError22() {
        String errorJson = "{\"number";
        tryParseJsonAndCheckExceptionMessage(errorJson, "Parse error on position 8 - Expected \"");
    }

    @Test
    public void testError23() {
        String errorJson = "{\"num \\u012Z ber\":12}";
        tryParseJsonAndCheckExceptionMessage(errorJson, "Parse error on position 12 - Expected 4 digits hex number");
    }

    private void tryParseJsonAndCheckExceptionMessage(String errorJson, String expectedExceptionMessage) {
        try {
            new JsonParser().parse(errorJson);
            Assert.fail("Expected JsonException");
        } catch (JsonException e) {
            Assert.assertEquals(expectedExceptionMessage, e.getMessage());
        }
        try {
            new JsonParser().parse(new JsonInputReader(new StringReader(errorJson)));
            Assert.fail("Expected JsonException");
        } catch (JsonException e) {
            Assert.assertEquals(expectedExceptionMessage, e.getMessage());
        }
        try {
            new JsonParser().parse(new JsonInputCharArray(errorJson.toCharArray()));
            Assert.fail("Expected JsonException");
        } catch (JsonException e) {
            Assert.assertEquals(expectedExceptionMessage, e.getMessage());
        }
    }
}
