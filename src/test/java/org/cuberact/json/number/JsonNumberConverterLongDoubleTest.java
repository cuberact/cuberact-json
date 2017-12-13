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

/**
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public class JsonNumberConverterLongDoubleTest {

    @Test
    public void doubleTest(){
        String number = "-123.345e-12";
        JsonNumber jsonNumber = new JsonNumber(number.toCharArray(), number.length(), true);

        Number converted = JsonNumberConverterLongDouble.REF.convert(jsonNumber);

        Assert.assertEquals(Double.class, converted.getClass());
        Assert.assertEquals("-1.23345E-10", converted.toString());
    }

    @Test
    public void longTest(){
        String number = "-123456789";
        JsonNumber jsonNumber = new JsonNumber(number.toCharArray(), number.length(), false);

        Number converted = JsonNumberConverterLongDouble.REF.convert(jsonNumber);

        Assert.assertEquals(Long.class, converted.getClass());
        Assert.assertEquals(number, converted.toString());
    }

}
