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

package org.cuberact.json.builder;

import org.cuberact.json.Json;
import org.cuberact.json.formatter.JsonFormatter;
import org.cuberact.json.output.JsonOutputStringBuilder;
import org.cuberact.json.parser.JsonParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Michal Nikodim (michal.nikodim@topmonks.com)
 */
public class JsonBuilderOutputTest {

    @Test
    public void example1() {
        String jsonAsString = "{'rect':[486,'HelloWorld',{'data':'ěščřžýáíé'},-23.54],'perspectiveSelector':{'perspectives':[true,false],'selected':null,'some':[1,2,3.2]}}"
                .replace('\'', '"');

        assertEqualsDomAndBuilder(jsonAsString, JsonFormatter.PRETTY());
        assertEqualsDomAndBuilder(jsonAsString, JsonFormatter.PACKED());
    }

    @Test
    public void example2() {
        String jsonAsString = "{'id':'abcdef1234567890','apiKey':'abcdef-ghijkl','name':'Main_key','validFrom':1505858400000,'softQuota':10000000,'hardQuota':10.12345,'useSignature':null,'state':'ENABLED','mocking':true,'clientIpUsed':false,'clientIpEnforced':false}"
                .replace('\'', '"');
        assertEqualsDomAndBuilder(jsonAsString, JsonFormatter.PRETTY());
        assertEqualsDomAndBuilder(jsonAsString, JsonFormatter.PACKED());
    }

    @Test
    public void example3() {
        String jsonAsString = "[1,[2,[3,[4,[5],4.4],3.3],2.2],1.1]";
        assertEqualsDomAndBuilder(jsonAsString, JsonFormatter.PRETTY());
        assertEqualsDomAndBuilder(jsonAsString, JsonFormatter.PACKED());
    }

    @Test
    public void example4() {
        String jsonAsString = "[[[[[[[[[[1]]]]]]]]]]";
        assertEqualsDomAndBuilder(jsonAsString, JsonFormatter.PRETTY());
        assertEqualsDomAndBuilder(jsonAsString, JsonFormatter.PACKED());
    }

    @Test
    public void example5() {
        String jsonAsString = "[1.1, -1.1, 2.2e10, 2.2E10, -2.2e10, -2.2E-10, 2.2e-10, 2.2E-10, -2.2e-10, -2.2E-10, 12345.12345e8, 12345.12345e-8, -12345.12345e8, -12345.12345e-8]";
        Assert.assertEquals(jsonAsString.replace('E', 'e'), formattedByBuilder(jsonAsString, JsonFormatter.PRETTY()));
        Assert.assertEquals(jsonAsString.replace('E', 'e').replaceAll(" ", ""), formattedByBuilder(jsonAsString, JsonFormatter.PACKED()));
    }

    @Test
    public void example7() {
        String jsonAsString = "{'1':{'2':{'3':{'4':{'5':{'6':{'7':{'8':{'9':{'name':'jack'}}}}},'age':15}}}}}"
                .replace('\'', '"');
        assertEqualsDomAndBuilder(jsonAsString, JsonFormatter.PRETTY());
        assertEqualsDomAndBuilder(jsonAsString, JsonFormatter.PACKED());
    }

    @Test
    public void example8() {
        String jsonAsString = "{'\\t\\b\\r\\b\\f\\n':12}"
                .replace('\'', '"');
        assertEqualsDomAndBuilder(jsonAsString, JsonFormatter.PRETTY());
        assertEqualsDomAndBuilder(jsonAsString, JsonFormatter.PACKED());
    }

    @Test
    public void constructorsTest() {
        String jsonAsString = "{'1':{'2':{'3':{'4':{'5':{'6':{'7':{'8':{'9':{'name':'jack'}}}}},'age':15}}}}}"
                .replace('\'', '"');

        String json1 = new JsonParser(new JsonBuilderOutput<>(new JsonOutputStringBuilder())).parse(jsonAsString).toString();
        String json2 = new JsonParser(new JsonBuilderOutput<>(new JsonOutputStringBuilder(), JsonFormatter.PRETTY())).parse(jsonAsString).toString();

        Assert.assertEquals(json1, json2);
    }

    private void assertEqualsDomAndBuilder(String input, JsonFormatter formatter) {
        String jsonFormattedByDom = formattedByDom(input, formatter);
        String jsonFormattedByBuilder = formattedByBuilder(input, formatter);
        Assert.assertEquals(jsonFormattedByDom, jsonFormattedByBuilder);
    }

    private String formattedByDom(String input, JsonFormatter formatter) {
        Json json = new JsonParser().parse(input);
        return json.toString(formatter);
    }

    private String formattedByBuilder(String input, JsonFormatter formatter) {
        JsonBuilder builder = new JsonBuilderOutput<>(new JsonOutputStringBuilder(), formatter);
        StringBuilder outputResult = new JsonParser(builder).parse(input);
        return outputResult.toString();
    }
}