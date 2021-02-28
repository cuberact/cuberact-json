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

package org.cuberact.json.formatter;

import org.cuberact.json.Json;
import org.cuberact.json.JsonException;
import org.cuberact.json.builder.JsonBuilderOutput;
import org.cuberact.json.output.JsonOutput;
import org.cuberact.json.output.JsonOutputStringBuilder;
import org.cuberact.json.parser.JsonParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public class JsonFormatterPrettyTest {

    @Test
    public void customConfig() {
        String jsonAsString = "{'one':[1,2,3,{'two':2},[[]],{}],'three':null}"
                .replace('\'', '"');
        Json json = new JsonParser().parse(jsonAsString);
        JsonFormatterPretty customFormatter = new JsonFormatterPretty(){

            @Override
            public String getObjectStart() {
                return "[obj_start]";
            }

            @Override
            public String getObjectEnd() {
                return "[obj_end]";
            }

            @Override
            public String getArrayStart() {
                return "[arr_start]";
            }

            @Override
            public String getArrayEnd() {
                return "[arr_end]";
            }

            @Override
            public String getStringStart() {
                return "[qm]";
            }

            @Override
            public String getStringEnd() {
                return "[qm]";
            }

            @Override
            public String getObjectColon() {
                return "[obj_colon]";
            }

            @Override
            public String getObjectComma() {
                return "[obj_comma]";
            }

            @Override
            public String getArrayComma() {
                return "[arr_comma]";
            }

            @Override
            protected String getIndent() {
                return "[indent]";
            }

            @Override
            public String getLineBreak() {
                return "[line_break]";
            }

            @Override
            protected boolean flat() {
                return false;
            }
        };

        System.out.println(json.toString(new JsonFormatterPretty()));

        String expected = "[obj_start][line_break][indent][qm]one[qm][obj_colon][arr_start][line_break][indent][indent]1[arr_comma][line_break][indent][indent]2[arr_comma][line_break][indent][indent]3[arr_comma][line_break][indent][indent][obj_start][line_break][indent][indent][indent][qm]two[qm][obj_colon]2[line_break][indent][indent][obj_end][arr_comma][line_break][indent][indent][arr_start][line_break][indent][indent][indent][arr_start][line_break][indent][indent][indent][arr_end][line_break][indent][indent][arr_end][arr_comma][line_break][indent][indent][obj_start][line_break][indent][indent][obj_end][line_break][indent][arr_end][obj_comma][line_break][indent][qm]three[qm][obj_colon]null[line_break][obj_end]";
        assertEquals(expected, json.toString(customFormatter)); //from dom
    }

}
