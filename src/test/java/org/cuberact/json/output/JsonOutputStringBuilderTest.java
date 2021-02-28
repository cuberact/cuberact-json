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

package org.cuberact.json.output;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;


/**
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public class JsonOutputStringBuilderTest {

    @Test
    public void write() {
        JsonOutputStringBuilder jsonOutputStream = new JsonOutputStringBuilder();
        jsonOutputStream.write("hello ");
        jsonOutputStream.write('w');
        jsonOutputStream.write('o');
        jsonOutputStream.write('r');
        jsonOutputStream.write('l');
        jsonOutputStream.write('d');
        jsonOutputStream.write(" !!!");
        assertEquals("hello world !!!", jsonOutputStream.getResult().toString());
    }

    @Test
    public void writeBig() {
        JsonOutputStringBuilder jsonOutputStream = new JsonOutputStringBuilder();
        StringBuilder expected = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            jsonOutputStream.write("abcdefghijklmn");
            expected.append("abcdefghijklmn");
            jsonOutputStream.write('-');
            expected.append('-');
        }
        assertEquals(expected.toString(), jsonOutputStream.getResult().toString());
    }
}
