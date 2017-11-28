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

import java.util.Arrays;

/**
 * @author Michal Nikodim (michal.nikodim@topmonks.com)
 */
public final class JsonNumber implements CharSequence {

    private final char[] number;
    private final boolean floatingNumber;
    private String strNumber;

    public JsonNumber(char[] number, int count, boolean floatingNumber) {
        this.number = new char[count];
        System.arraycopy(number, 0, this.number, 0, count);
        this.floatingNumber = floatingNumber;
    }

    public boolean isFloatingNumber() {
        return floatingNumber;
    }

    @Override
    public int length() {
        return number.length;
    }

    @Override
    public char charAt(int index) {
        return number[index];
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return new String(number, start, end);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(number) * 31;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JsonNumber that = (JsonNumber) o;
        return number != null ? Arrays.equals(number, that.number) : that.number == null;
    }

    @Override
    public String toString() {
        if (strNumber == null) {
            strNumber = new String(number);
        }
        return strNumber;
    }
}
