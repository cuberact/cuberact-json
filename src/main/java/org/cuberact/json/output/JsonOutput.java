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

import org.cuberact.json.Json;
import org.cuberact.json.formatter.JsonFormatter;

/**
 * Common interface for JsonObject and JsonArray output {@link Json#toOutput(JsonFormatter, JsonOutput)}
 *
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public interface JsonOutput<RESULT> {

    /**
     * Write data to output
     *
     * @param data - input
     */
    void write(CharSequence data);

    /**
     * Write data to output
     *
     * @param data - input
     */
    void write(char data);

    /**
     * @return output result
     */
    RESULT result();
}
