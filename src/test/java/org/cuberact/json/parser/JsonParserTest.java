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

import org.cuberact.json.Json;
import org.cuberact.json.JsonArray;
import org.cuberact.json.JsonObject;
import org.cuberact.json.builder.JsonBuilderDom;
import org.cuberact.json.formatter.JsonFormatter;
import org.cuberact.json.number.JsonNumberConverter;
import org.cuberact.json.number.JsonNumberConverterIntFloat;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */

public class JsonParserTest {

    @Test
    public void parseExample1() {
        String jsonAsString = "{\n" +
                "  'rect': [486,'\\u0048\\u0065\\u006c\\u006C\\u006FWorld',{'data' : '\\u011B\\u0161\\u010D\\u0159\\u017E\\u00FD\\u00E1\\u00ED\\u00E9'},-23.54],\n" +
                "  'perspectiveSelector': {\n" +
                "    'perspectives': [ true, false],\n" +
                "    'selected': null,\n" +
                "    'some': [1,2,3.2]\n" +
                "  }\n" +
                "}";
        jsonAsString = jsonAsString.replace('\'', '"');
        String expected = "{'rect':[486,'HelloWorld',{'data':'ěščřžýáíé'},-23.54],'perspectiveSelector':{'perspectives':[true,false],'selected':null,'some':[1,2,3.2]}}"
                .replace('\'', '"');

        Json json = new JsonParser().parse(jsonAsString);
        assertEquals(expected, json.toString(JsonFormatter.PACKED()));
    }

    @Test
    public void parseExample2() {
        String jsonAsString = "{\n" +
                "\t'id' : 'abcdef1234567890',\n" +
                "\t'apiKey' : 'abcdef-ghijkl',\n" +
                "\t'name' : 'Main_key',\n" +
                "\t'validFrom' : 1505858400000,\n" +
                "\t'softQuota' : 10000000,\n" +
                "\t'hardQuota' : 10.12345,\n" +
                "\t'useSignature' : null,\n" +
                "\t'state' : 'ENABLED',\n" +
                "\t'mocking' : true,\n" +
                "\t'clientIpUsed' : false,\n" +
                "\t'clientIpEnforced' : false\n" +
                "}";
        jsonAsString = jsonAsString.replace('\'', '"');

        String expected = "{'id':'abcdef1234567890','apiKey':'abcdef-ghijkl','name':'Main_key','validFrom':1505858400000,'softQuota':10000000,'hardQuota':10.12345,'useSignature':null,'state':'ENABLED','mocking':true,'clientIpUsed':false,'clientIpEnforced':false}"
                .replace('\'', '"');

        Json json = new JsonParser().parse(jsonAsString);
        assertEquals(expected, json.toString(JsonFormatter.PACKED()));
    }

    @Test
    public void parseExample3() {
        String jsonAsString = "[1,\n" +
                "[2, \n" +
                "[3, \n" +
                "[4, \n" +
                "[5], \n" +
                "4.4], \n" +
                "3.3], \n" +
                "2.2], \n" +
                "1.1]";
        String expected = "[1,[2,[3,[4,[5],4.4],3.3],2.2],1.1]";
        Json json = new JsonParser().parse(jsonAsString);
        assertEquals(expected, json.toString(JsonFormatter.PACKED()));
    }

    @Test
    public void parseExample4() {
        String jsonAsString = "[[[[[[[[[[1]]]]]]]]]]";
        Json json = new JsonParser().parse(jsonAsString);
        assertEquals(jsonAsString, json.toString(JsonFormatter.PACKED()));
    }

    @Test
    public void parseExample5() {
        String jsonAsString = "[1,-2,'text',true,false,null,1.1,-1.1,{},[]]"
                .replace('\'', '"');
        Json json = new JsonParser().parse(jsonAsString);
        assertEquals(jsonAsString, json.toString(JsonFormatter.PACKED()));
    }

    @Test
    public void parseExample6() {
        String jsonAsString = "[1.1, -1.1, " +
                "2.2e10, 2.2E10, -2.2e10, -2.2E-10, 2.2e-10, 2.2E-10, -2.2e-10, -2.2E-10, " +
                "12345.12345e8, 12345.12345e-8, -12345.12345e8, -12345.12345e-8" +
                "]";
        String expected = "[1.1,-1.1,2.2E10,2.2E10,-2.2E10,-2.2E-10,2.2E-10,2.2E-10,-2.2E-10,-2.2E-10,1.234512345E12,1.234512345E-4,-1.234512345E12,-1.234512345E-4]";
        Json json = new JsonParser().parse(jsonAsString);
        assertEquals(expected, json.toString(JsonFormatter.PACKED()));
    }

    @Test
    public void parseExample7() {
        String jsonAsString = "{'1':{'2':{'3':{'4':{'5':{'6':{'7':{'8':{'9':{'name':'jack'}}}}},'age':15}}}}}"
                .replace('\'', '"');
        Json json = new JsonParser().parse(jsonAsString);
        assertEquals(jsonAsString, json.toString(JsonFormatter.PACKED()));
    }

    @Test
    public void parseExample8() {
        String jsonAsString = "{'\\t\\b\\r\\b\\f\\n':12}"
                .replace('\'', '"');
        Json json = new JsonParser().parse(jsonAsString);
        assertEquals(jsonAsString, json.toString(JsonFormatter.PACKED()));
    }

    @Test
    public void parseExampleVeryLongStrings() {
        StringBuilder jsonAsString = new StringBuilder("{\"");
        for (int i = 0; i < 10000; i++) {
            jsonAsString.append((char) ((i & 15) + 65));
        }
        jsonAsString.append("\":\"");
        for (int i = 0; i < 100000; i++) {
            jsonAsString.append((char) ((i & 15) + 65));
        }
        jsonAsString.append("\"}");
        Json json = new JsonParser().parse(jsonAsString);
        assertEquals(jsonAsString.toString(), json.toString(JsonFormatter.PACKED()));
    }

    @Test
    public void testArray() {
        String jsonAsString = "{'arr':[1,2,3,4,2147483647,-2147483648]}"
                .replace('\'', '"');

        JsonObject json = new JsonParser().parse(jsonAsString);
        assertEquals(JsonArray.class, json.getArr("arr").getClass());
        JsonArray jsonArray = json.getArr("arr");
        for (Object o : jsonArray.iterable()) {
            assertEquals(Long.class, o.getClass());
        }
        assertEquals(jsonAsString, json.toString(JsonFormatter.PACKED()));
    }

    @Test
    public void testArrayMixed() {
        String JsonAsString = "{'arr':[1.3234234,'hello',false,null,32]}"
                .replace('\'', '"');
        JsonObject json = new JsonParser().parse(JsonAsString);
        assertEquals(JsonArray.class, json.getArr("arr").getClass());
        JsonArray jsonArray = json.getArr("arr");
        assertEquals(Double.class, jsonArray.get(0).getClass());
        assertEquals(String.class, jsonArray.get(1).getClass());
        assertEquals(Boolean.class, jsonArray.get(2).getClass());
        assertNull(jsonArray.get(3));
        assertEquals(Long.class, jsonArray.get(4).getClass());
        assertEquals(JsonAsString, json.toString(JsonFormatter.PACKED()));
    }

    @Test
    public void numberAsLongAndDouble() {
        String jsonAsString = "[1,2,3,1.1,2.2,3.3E-6]";
        JsonParser jsonParser = new JsonParser();
        JsonArray jsonArray = jsonParser.parse(jsonAsString);
        assertEquals(1L, jsonArray.get(0));
        assertEquals(2L, jsonArray.get(1));
        assertEquals(3L, jsonArray.get(2));
        assertEquals(1.1D, jsonArray.get(3));
        assertEquals(2.2D, jsonArray.get(4));
        assertEquals(3.3E-6D, jsonArray.get(5));
    }

    @Test
    public void numberAsIntAndFloat() {
        String jsonAsString = "[1,2,3,1.1,2.2,3.3E-6]";
        JsonParser jsonParser = new JsonParser(new JsonBuilderDom(JsonNumberConverterIntFloat.REF));
        JsonArray jsonArray = jsonParser.parse(jsonAsString);
        assertEquals(1, jsonArray.get(0));
        assertEquals(2, jsonArray.get(1));
        assertEquals(3, jsonArray.get(2));
        assertEquals(1.1f, jsonArray.get(3));
        assertEquals(2.2f, jsonArray.get(4));
        assertEquals(3.3E-6f, jsonArray.get(5));
    }

    @Test
    public void numberAsBigIntegerAndBigDecimal() {
        String jsonAsString = "[1,2,3,1.1,2.2,3.3E-6]";
        JsonNumberConverter jsonNumberConverter = number -> {
            if (number.isFloatingNumber()) {
                return new BigDecimal(number.toString());
            }
            return new BigInteger(number.toString());
        };
        JsonParser jsonParser = new JsonParser(new JsonBuilderDom(jsonNumberConverter));
        JsonArray jsonArray = jsonParser.parse(jsonAsString);
        assertEquals(new BigInteger("1"), jsonArray.get(0));
        assertEquals(new BigInteger("2"), jsonArray.get(1));
        assertEquals(new BigInteger("3"), jsonArray.get(2));
        assertEquals(new BigDecimal("1.1"), jsonArray.get(3));
        assertEquals(new BigDecimal("2.2"), jsonArray.get(4));
        assertEquals(new BigDecimal("3.3E-6"), jsonArray.get(5));
    }
}
