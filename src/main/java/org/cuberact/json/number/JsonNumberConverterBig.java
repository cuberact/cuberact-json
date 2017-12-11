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

package org.cuberact.json.number;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * JsonNumber converter to BigInteger or BigDecimal
 *
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public final class JsonNumberConverterBig implements JsonNumberConverter {

    public static final JsonNumberConverterBig REF = new JsonNumberConverterBig();

    private JsonNumberConverterBig() {
        //use REF instead
    }

    /**
     * @return JsonNumber as BigInteger or BigDecimal
     */
    @Override
    public Number convert(JsonNumber jsonNumber) {
        return jsonNumber.isFloatingNumber() ? new BigDecimal(jsonNumber.toString()) : new BigInteger(jsonNumber.toString());
    }
}
