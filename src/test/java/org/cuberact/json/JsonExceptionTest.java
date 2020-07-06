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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public class JsonExceptionTest {

    @Test
    public void constructor() {
        RuntimeException runtimeException = new RuntimeException("text1");
        JsonException jsonException = new JsonException("text2", runtimeException);
        assertEquals("text2", jsonException.getMessage());
        assertEquals(runtimeException, jsonException.getCause());
    }
}
