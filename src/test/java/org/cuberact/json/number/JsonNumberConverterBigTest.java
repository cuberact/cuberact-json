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

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public class JsonNumberConverterBigTest {

    @Test
    public void bigDecimal(){
        String number = "-123456789123456789123456789123456789123456789123456789.123e-123456789";
        JsonNumber jsonNumber = new JsonNumber(number.toCharArray(), number.length(), true);

        Number converted = JsonNumberConverterBig.REF.convert(jsonNumber);

        Assert.assertEquals(BigDecimal.class, converted.getClass());
        Assert.assertEquals("-1.23456789123456789123456789123456789123456789123456789123E-123456736", converted.toString());
    }

    @Test
    public void bigInteger(){
        String number = "-123456789123456789123456789123456789123456789123456789";
        JsonNumber jsonNumber = new JsonNumber(number.toCharArray(), number.length(), false);

        Number converted = JsonNumberConverterBig.REF.convert(jsonNumber);

        Assert.assertEquals(BigInteger.class, converted.getClass());
        Assert.assertEquals(number, converted.toString());
    }

}
