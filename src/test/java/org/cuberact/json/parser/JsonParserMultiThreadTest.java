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
import org.cuberact.json.formatter.JsonFormatter;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public class JsonParserMultiThreadTest {

    @Test
    public void multiThread() throws InterruptedException {
        String longJson = prepareLongJson();
        String expected = ((Json) new JsonParser().parse(longJson)).toString(JsonFormatter.PACKED());
        final JsonParser singleInstanceOfJsonParser = new JsonParser();
        final List<String> parsed = new ArrayList<>();
        Runnable runnable = () -> {
            Json json = singleInstanceOfJsonParser.parse(longJson);
            parsed.add(json.toString(JsonFormatter.PACKED()));
        };
        Thread[] threads = new Thread[10];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(runnable);
            threads[i].start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        for (String json : parsed) {
            Assert.assertEquals(expected, json);
        }
    }

    private String prepareLongJson() {
        String jsonAsString = "{\n" +
                "  \"rect\": [486,\"\\u0048\\u0065\\u006c\\u006C\\u006FWorld\",{\"data\" : \"\\u011B\\u0161\\u010D\\u0159\\u017E\\u00FD\\u00E1\\u00ED\\u00E9\"},-23.54],\n" +
                "  \"perspectiveSelector\": {\n" +
                "    \"perspectives\": [ true, false],\n" +
                "    \"selected\": null,\n" +
                "    \"some\": [1,2,3.2]\n" +
                "  }\n" +
                "}";
        StringBuilder longJsonArray = new StringBuilder("[");
        for (int i = 0; i < 10000; i++) {
            longJsonArray.append(jsonAsString);
            longJsonArray.append(",");
        }
        longJsonArray.setLength(longJsonArray.length() - 1); //cut last comma
        longJsonArray.append("]");
        return longJsonArray.toString();
    }
}
