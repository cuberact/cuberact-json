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

import static org.cuberact.json.optimize.CharTable.toInt;
import static org.cuberact.json.optimize.CharTable.toLong;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.function.Function;

/**
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public final class JsonNumber implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("RedundantIfStatement")
    public static final Function<JsonNumber, Object> CONVERTER_DYNAMIC = jsonNumber -> {
        if (jsonNumber.isFloatingNumber()) {
            double doubleValue = jsonNumber.asDouble();
            if (doubleValue == (double) (float) doubleValue) {
                return (float) doubleValue;
            }
            return doubleValue;
        }
        long longValue = jsonNumber.asLong();
        int intValue = (int) longValue;
        if (intValue == longValue) {
            return intValue;
        }
        return longValue;
    };

    public static final Function<JsonNumber, Object> CONVERTER_INT_FLOAT = jsonNumber -> {
        if (jsonNumber.isFloatingNumber()) {
            return jsonNumber.asFloat();
        }
        return jsonNumber.asInt();
    };

    public static final Function<JsonNumber, Object> CONVERTER_LONG_DOUBLE = jsonNumber -> {
        if (jsonNumber.isFloatingNumber()) {
            return jsonNumber.asDouble();
        }
        return jsonNumber.asLong();
    };

    private final char[] charNumber;
    private final boolean floatingNumber;
    private transient String strNumber;

    public JsonNumber(char[] charNumber, int count, boolean floatingNumber) {
        this.charNumber = new char[count];
        System.arraycopy(charNumber, 0, this.charNumber, 0, count);
        this.floatingNumber = floatingNumber;
    }

    public boolean isFloatingNumber() {
        return floatingNumber;
    }

    public Number asNumber(Class<? extends Number> type) {
        if (Integer.class.equals(type)) {
            return asInt();
        } else if (Long.class.equals(type)) {
            return asLong();
        } else if (Float.class.equals(type)) {
            return asFloat();
        } else if (Double.class.equals(type)) {
            return asDouble();
        } else if (BigInteger.class.equals(type)) {
            return asBigInt();
        } else if (BigDecimal.class.equals(type)) {
            return asBigDecimal();
        }
        throw new JsonException("Unknown number type " + type);
    }

    public int asInt() {
        int result = 0;
        int sign = 1;
        char c = charNumber[0];
        if (c == '-') {
            sign = -1;
        } else if (c == '.') {
            return 0;
        } else {
            result = toInt(c);
        }
        for (int i = 1; i < charNumber.length; i++) {
            if (charNumber[i] == '.') break;
            result = result * 10 + toInt(charNumber[i]);
        }
        return sign * result;
    }

    public long asLong() {
        long result = 0L;
        long sign = 1L;
        char c = charNumber[0];
        if (c == '-') {
            sign = -1L;
        } else if (c == '.') {
            return 0;
        } else {
            result = toLong(c);
        }
        for (int i = 1; i < charNumber.length; i++) {
            if (charNumber[i] == '.') break;
            result = result * 10L + toLong(charNumber[i]);
        }
        return sign * result;
    }

    public float asFloat() {
        return Float.parseFloat(toString());
    }

    public double asDouble() {
        return Double.parseDouble(toString());
    }

    public BigInteger asBigInt() {
        if (floatingNumber) {
            int indexOfDot = toString().indexOf(".");
            if (indexOfDot != -1) {
                String number = toString().substring(0, indexOfDot);
                return number.length() == 0 ? BigInteger.ZERO : new BigInteger(number);
            }
        }
        return new BigInteger(toString());
    }

    public BigDecimal asBigDecimal() {
        return new BigDecimal(toString());
    }

    public int length() {
        return charNumber.length;
    }

    public char charAt(int index) {
        return charNumber[index];
    }

    @Override
    public int hashCode() {
        return 31 * Boolean.hashCode(floatingNumber) + Arrays.hashCode(charNumber);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JsonNumber that = (JsonNumber) o;
        return floatingNumber == that.floatingNumber && (charNumber != null ? Arrays.equals(charNumber, that.charNumber) : that.charNumber == null);
    }

    @Override
    public String toString() {
        if (strNumber == null) {
            strNumber = new String(charNumber);
        }
        return strNumber;
    }
}
