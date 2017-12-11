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

import org.cuberact.json.JsonException;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Michal Nikodim (michal.nikodim@topmonks.com)
 */
public class JsonOutputStreamTest {

    @Test
    public void write() {
        JsonOutputStream<ByteArrayOutputStream> jsonOutputStream = new JsonOutputStream<>(new ByteArrayOutputStream());
        jsonOutputStream.write("hello ");
        jsonOutputStream.write('w');
        jsonOutputStream.write('o');
        jsonOutputStream.write('r');
        jsonOutputStream.write('l');
        jsonOutputStream.write('d');
        jsonOutputStream.write(" !!!");
        Assert.assertEquals("hello world !!!", new String(jsonOutputStream.getResult().toByteArray()));
    }

    @Test
    public void writeBig() {
        JsonOutputStream<ByteArrayOutputStream> jsonOutputStream = new JsonOutputStream<>(new ByteArrayOutputStream());
        StringBuilder expected = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            jsonOutputStream.write("123456789");
            expected.append("123456789");
            jsonOutputStream.write('-');
            expected.append('-');
        }
        Assert.assertEquals(expected.toString(), new String(jsonOutputStream.getResult().toByteArray()));
    }

    @Test(expected = JsonException.class)
    public void streamWithSimulatedException() {
        OutputStream stream = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                throw new IOException("simulated exception");
            }
        };
        JsonOutputStream jsonOutputStream = new JsonOutputStream<>(stream);
        jsonOutputStream.write("write to buffer");
        jsonOutputStream.flushBuffer(); // write from buffer to stream -> cause simulated exception
    }
}
