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
        JsonFormatterPretty.Config cfg = new JsonFormatterPretty.Config();
        cfg.indent = "[indent]";
        cfg.objectStart = "[obj_start]";
        cfg.objectEnd = "[obj_end]";
        cfg.arrayStart = "[arr_start]";
        cfg.arrayEnd = "[arr_end]";
        cfg.objectColon = "[obj_colon]";
        cfg.objectComma = "[obj_comma]";
        cfg.arrayComma = "[arr_comma]";
        cfg.quotationMark = "[qm]";
        cfg.lineBreak = "[line_break]";
        String expected = "[obj_start][line_break][indent][qm]one[qm][obj_colon][arr_start]1[arr_comma]2[arr_comma]3[arr_comma][obj_start][line_break][indent][indent][qm]two[qm][obj_colon]2[line_break][indent][obj_end][arr_comma][arr_start][arr_start][arr_end][arr_end][arr_comma][obj_start][obj_end][arr_end][obj_comma][line_break][indent][qm]three[qm][obj_colon]null[line_break][obj_end]";
        assertEquals(expected, json.toString(new JsonFormatterPretty(cfg))); //from dom
        assertEquals(expected, new JsonParser(
                new JsonBuilderOutput(
                        new JsonOutputStringBuilder(),
                        new JsonFormatterPretty(cfg)))
                .<JsonOutput>parse(jsonAsString).getResult().toString()); //directly from input to string
    }

    @Test
    public void cloneException() {
        assertThrows(JsonException.class, () -> {
            JsonFormatterPretty.Config cfg = new JsonFormatterPretty.Config() {
                @Override
                protected Object clone() throws CloneNotSupportedException {
                    throw new CloneNotSupportedException("simulated exception");
                }
            };
            cfg.copy();
        });
    }
}
