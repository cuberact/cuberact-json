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

package org.cuberact.json;

import org.cuberact.json.parser.JsonParser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public class JsonNumberTest {

    @Test
    public void equals() {
        JsonNumber jsonNumber1 = new JsonNumber("12345.67E+13".toCharArray(), 12, true);
        JsonNumber jsonNumber2 = new JsonNumber("12345.67E+13".toCharArray(), 12, true);
        assertEquals(jsonNumber1, jsonNumber2);
    }

    @Test
    public void notEquals() {
        JsonNumber jsonNumber1 = new JsonNumber("12345".toCharArray(), 5, true);
        JsonNumber jsonNumber2 = new JsonNumber("12345".toCharArray(), 5, false);
        assertNotEquals(jsonNumber1, jsonNumber2);
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
    public void convert() {
        JsonNumber jsonNumber = new JsonNumber("12345".toCharArray(), 5, false);
        assertEquals(5, jsonNumber.length());
        assertEquals('3', jsonNumber.charAt(2));
        assertEquals("12345", jsonNumber.toString());
        assertEquals(12345, jsonNumber.asInt());
        assertEquals(12345L, jsonNumber.asLong());
        assertEquals(new Float(12345), (Float) jsonNumber.asFloat());
        assertEquals(new Double(12345), (Double) jsonNumber.asDouble());
        assertEquals(new BigInteger("12345"), jsonNumber.asBigInt());
        assertEquals(new BigDecimal("12345"), jsonNumber.asBigDecimal());
    }

    @Test
    public void convertNegative() {
        JsonNumber jsonNumber = new JsonNumber("-12345".toCharArray(), 6, false);
        assertEquals(-12345, jsonNumber.asInt());
        assertEquals(-12345L, jsonNumber.asLong());
        assertEquals(new Float(-12345), (Float) jsonNumber.asFloat());
        assertEquals(new Double(-12345), (Double) jsonNumber.asDouble());
        assertEquals(new BigInteger("-12345"), jsonNumber.asBigInt());
        assertEquals(new BigDecimal("-12345"), jsonNumber.asBigDecimal());
    }

    @Test
    public void convertFloating1() {
        JsonNumber jsonNumber = new JsonNumber("123.45".toCharArray(), 6, true);
        assertEquals(6, jsonNumber.length());
        assertEquals('3', jsonNumber.charAt(2));
        assertEquals("123.45", jsonNumber.toString());
        assertEquals(123, jsonNumber.asInt());
        assertEquals(123L, jsonNumber.asLong());
        assertEquals(new Float(123.45), (Float) jsonNumber.asFloat());
        assertEquals(new Double(123.45), (Double) jsonNumber.asDouble());
        assertEquals(new BigInteger("123"), jsonNumber.asBigInt());
        assertEquals(new BigDecimal("123.45"), jsonNumber.asBigDecimal());
    }

    @Test
    public void convertFloating2() {
        JsonNumber jsonNumber = new JsonNumber(".45".toCharArray(), 3, true);
        assertEquals(3, jsonNumber.length());
        assertEquals('5', jsonNumber.charAt(2));
        assertEquals(".45", jsonNumber.toString());
        assertEquals(0, jsonNumber.asInt());
        assertEquals(0L, jsonNumber.asLong());
        assertEquals(new Float(.45), (Float) jsonNumber.asFloat());
        assertEquals(new Double(.45), (Double) jsonNumber.asDouble());
        assertEquals(new BigInteger("0"), jsonNumber.asBigInt());
        assertEquals(new BigDecimal(".45"), jsonNumber.asBigDecimal());
    }

    @Test
    public void convertException() {
        assertThrows(JsonException.class, () -> {
            JsonNumber jsonNumber = new JsonNumber("12345".toCharArray(), 5, false);
            jsonNumber.asNumber(AtomicLong.class);
        });
    }

    @Test
    public void convert_simpleCase() {
        String jsonAsString = "[1,2,3,4.1,4.2,4.3]";
        JsonArray jsonArray = new JsonParser().parse(jsonAsString);
        assertEquals(JsonNumber.class, jsonArray.get(0).getClass());
        jsonArray.convertJsonNumbers(JsonNumber::asFloat, true);
        assertEquals(Float.class, jsonArray.get(0).getClass());
    }

    @Test
    public void convert_JsonObject_callOnChildren() {
        String jsonAsString = "{\"n1\": 1, \"n2\":1.1, \"a1\": [1,2,3.1] }";
        JsonObject jsonObject = new JsonParser().parse(jsonAsString);
        JsonArray jsonArray = jsonObject.getArr("a1");
        assertEquals(JsonNumber.class, jsonObject.get("n1").getClass());
        assertEquals(JsonNumber.class, jsonObject.get("n2").getClass());
        assertEquals(JsonNumber.class, jsonArray.get(0).getClass());
        assertEquals(JsonNumber.class, jsonArray.get(1).getClass());
        assertEquals(JsonNumber.class, jsonArray.get(2).getClass());
        jsonObject.convertJsonNumbers(jsonNumber -> {
            if (jsonNumber.isFloatingNumber()) {
                return jsonNumber.asFloat();
            } else {
                return jsonNumber.asInt();
            }
        }, true);
        assertEquals(Integer.class, jsonObject.get("n1").getClass());
        assertEquals(Float.class, jsonObject.get("n2").getClass());
        assertEquals(Integer.class, jsonArray.get(0).getClass());
        assertEquals(Integer.class, jsonArray.get(1).getClass());
        assertEquals(Float.class, jsonArray.get(2).getClass());
    }

    @Test
    public void convert_JsonObject_notCallOnChildren() {
        String jsonAsString = "{\"n1\": 1, \"n2\":1.1, \"a1\": [1,2,3.1] }";
        JsonObject jsonObject = new JsonParser().parse(jsonAsString);
        JsonArray jsonArray = jsonObject.getArr("a1");
        assertEquals(JsonNumber.class, jsonObject.get("n1").getClass());
        assertEquals(JsonNumber.class, jsonObject.get("n2").getClass());
        assertEquals(JsonNumber.class, jsonArray.get(0).getClass());
        assertEquals(JsonNumber.class, jsonArray.get(1).getClass());
        assertEquals(JsonNumber.class, jsonArray.get(2).getClass());
        jsonObject.convertJsonNumbers(jsonNumber -> {
            if (jsonNumber.isFloatingNumber()) {
                return jsonNumber.asFloat();
            }
            return jsonNumber.asInt();
        }, false);
        assertEquals(Integer.class, jsonObject.get("n1").getClass());
        assertEquals(Float.class, jsonObject.get("n2").getClass());
        assertEquals(JsonNumber.class, jsonArray.get(0).getClass());
        assertEquals(JsonNumber.class, jsonArray.get(1).getClass());
        assertEquals(JsonNumber.class, jsonArray.get(2).getClass());
    }

    @Test
    public void convert_JsonArray_callOnChildren() {
        String jsonAsString = "[1,2.1,[10,12.1]]";
        JsonArray jsonArray1 = new JsonParser().parse(jsonAsString);
        JsonArray jsonArray2 = jsonArray1.getArr(2);
        assertEquals(JsonNumber.class, jsonArray1.get(0).getClass());
        assertEquals(JsonNumber.class, jsonArray1.get(1).getClass());
        assertEquals(JsonNumber.class, jsonArray2.get(0).getClass());
        assertEquals(JsonNumber.class, jsonArray2.get(1).getClass());
        jsonArray1.convertJsonNumbers(jsonNumber -> {
            if (jsonNumber.isFloatingNumber()) {
                return jsonNumber.asFloat();
            } else {
                return jsonNumber.asInt();
            }
        }, true);
        assertEquals(Integer.class, jsonArray1.get(0).getClass());
        assertEquals(Float.class, jsonArray1.get(1).getClass());
        assertEquals(Integer.class, jsonArray2.get(0).getClass());
        assertEquals(Float.class, jsonArray2.get(1).getClass());
    }

    @Test
    public void convert_JsonArray_notCallOnChildren() {
        String jsonAsString = "[1,2.1,[10,12.1]]";
        JsonArray jsonArray1 = new JsonParser().parse(jsonAsString);
        JsonArray jsonArray2 = jsonArray1.getArr(2);
        assertEquals(JsonNumber.class, jsonArray1.get(0).getClass());
        assertEquals(JsonNumber.class, jsonArray1.get(1).getClass());
        assertEquals(JsonNumber.class, jsonArray2.get(0).getClass());
        assertEquals(JsonNumber.class, jsonArray2.get(1).getClass());
        jsonArray1.convertJsonNumbers(jsonNumber -> {
            if (jsonNumber.isFloatingNumber()) {
                return jsonNumber.asFloat();
            } else {
                return jsonNumber.asInt();
            }
        }, false);
        assertEquals(Integer.class, jsonArray1.get(0).getClass());
        assertEquals(Float.class, jsonArray1.get(1).getClass());
        assertEquals(JsonNumber.class, jsonArray2.get(0).getClass());
        assertEquals(JsonNumber.class, jsonArray2.get(1).getClass());
    }

    @Test
    public void converterDynamic() {
        String jsonAsString = "[123,99999999999, 1.25, 1.1234567890]";
        JsonArray jsonArray = new JsonParser().parse(jsonAsString);
        assertEquals(JsonNumber.class, jsonArray.get(0).getClass());
        assertEquals(JsonNumber.class, jsonArray.get(1).getClass());
        assertEquals(JsonNumber.class, jsonArray.get(2).getClass());
        assertEquals(JsonNumber.class, jsonArray.get(3).getClass());
        jsonArray.convertJsonNumbers(JsonNumber.CONVERTER_DYNAMIC, true);
        assertEquals(Integer.class, jsonArray.get(0).getClass());
        assertEquals(Long.class, jsonArray.get(1).getClass());
        assertEquals(Float.class, jsonArray.get(2).getClass());
        assertEquals(Double.class, jsonArray.get(3).getClass());
    }

    @Test
    public void converterIntFloat() {
        String jsonAsString = "[123,99999999999, 1.25, 1.1234567890]";
        JsonArray jsonArray = new JsonParser().parse(jsonAsString);
        assertEquals(JsonNumber.class, jsonArray.get(0).getClass());
        assertEquals(JsonNumber.class, jsonArray.get(1).getClass());
        assertEquals(JsonNumber.class, jsonArray.get(2).getClass());
        assertEquals(JsonNumber.class, jsonArray.get(3).getClass());
        jsonArray.convertJsonNumbers(JsonNumber.CONVERTER_INT_FLOAT, true);
        assertEquals(Integer.class, jsonArray.get(0).getClass());
        assertEquals(Integer.class, jsonArray.get(1).getClass());
        assertEquals(Float.class, jsonArray.get(2).getClass());
        assertEquals(Float.class, jsonArray.get(3).getClass());
    }

    @Test
    public void converterLongDouble() {
        String jsonAsString = "[123,99999999999, 1.25, 1.1234567890]";
        JsonArray jsonArray = new JsonParser().parse(jsonAsString);
        assertEquals(JsonNumber.class, jsonArray.get(0).getClass());
        assertEquals(JsonNumber.class, jsonArray.get(1).getClass());
        assertEquals(JsonNumber.class, jsonArray.get(2).getClass());
        assertEquals(JsonNumber.class, jsonArray.get(3).getClass());
        jsonArray.convertJsonNumbers(JsonNumber.CONVERTER_LONG_DOUBLE, true);
        assertEquals(Long.class, jsonArray.get(0).getClass());
        assertEquals(Long.class, jsonArray.get(1).getClass());
        assertEquals(Double.class, jsonArray.get(2).getClass());
        assertEquals(Double.class, jsonArray.get(3).getClass());
    }

}
