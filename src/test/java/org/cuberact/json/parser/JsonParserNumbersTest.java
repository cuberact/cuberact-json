package org.cuberact.json.parser;

import org.cuberact.json.builder.JsonBuilder;
import org.cuberact.json.builder.JsonBuilderTree;
import org.cuberact.json.builder.number.NumberConverter;
import org.cuberact.json.builder.number.NumberConverterIntFloat;
import org.junit.Test;
import org.cuberact.json.JsonArray;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

/**
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public class JsonParserNumbersTest {

    @Test
    public void numberAsLongAndDouble() {
        String jsonAsString = "[1,2,3,1.1,2.2,3.3E-6]";
        JsonParser jsonParser = new JsonParser();
        JsonArray jsonArray = jsonParser.parse(jsonAsString);
        assertEquals(1L, jsonArray.get(0));
        assertEquals(2L, jsonArray.get(1));
        assertEquals(3L, jsonArray.get(2));
        assertEquals(1.1D, jsonArray.get(3));
        assertEquals(2.2D, jsonArray.get(4));
        assertEquals(3.3E-6D, jsonArray.get(5));
    }

    @Test
    public void numberAsIntAndFloat() {
        String jsonAsString = "[1,2,3,1.1,2.2,3.3E-6]";
        JsonBuilder builder = new JsonBuilderTree(NumberConverterIntFloat.REF);
        JsonParser jsonParser = new JsonParser(builder);
        JsonArray jsonArray = jsonParser.parse(jsonAsString);
        assertEquals(1, jsonArray.get(0));
        assertEquals(2, jsonArray.get(1));
        assertEquals(3, jsonArray.get(2));
        assertEquals(1.1f, jsonArray.get(3));
        assertEquals(2.2f, jsonArray.get(4));
        assertEquals(3.3E-6f, jsonArray.get(5));
    }

    @Test
    public void numberAsBigIntegerAndBigDecimal() {
        String jsonAsString = "[1,2,3,1.1,2.2,3.3E-6]";
        NumberConverter numberConverter = new NumberConverter() {
            @Override
            public Number convertWholeNumber(char[] number, int offset, int count) throws Throwable {
                return new BigInteger(new String(number, offset, count));
            }

            @Override
            public Number convertFloatingPointNumber(char[] number, int offset, int count) throws Throwable {
                return new BigDecimal(new String(number, offset, count));
            }
        };
        JsonBuilder builder = new JsonBuilderTree(numberConverter);
        JsonParser jsonParser = new JsonParser(builder);
        JsonArray jsonArray = jsonParser.parse(jsonAsString);
        assertEquals(new BigInteger("1"), jsonArray.get(0));
        assertEquals(new BigInteger("2"), jsonArray.get(1));
        assertEquals(new BigInteger("3"), jsonArray.get(2));
        assertEquals(new BigDecimal("1.1"), jsonArray.get(3));
        assertEquals(new BigDecimal("2.2"), jsonArray.get(4));
        assertEquals(new BigDecimal("3.3E-6"), jsonArray.get(5));
    }
}
