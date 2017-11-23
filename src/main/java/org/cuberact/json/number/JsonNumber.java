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

/**
 * @author Michal Nikodim (michal.nikodim@topmonks.com)
 */
public final class JsonNumber implements CharSequence {

    private final String number;
    private final boolean floatingNumber;

    public JsonNumber(String number, boolean floatingNumber) {
        this.number = number;
        this.floatingNumber = floatingNumber;
    }

    public boolean isFloatingNumber() {
        return floatingNumber;
    }

    @Override
    public int length() {
        return number.length();
    }

    @Override
    public char charAt(int index) {
        return number.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return number.subSequence(start, end);
    }

    @Override
    public int hashCode() {
        return number.hashCode() * 31;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JsonNumber that = (JsonNumber) o;
        return number != null ? number.equals(that.number) : that.number == null;
    }

    @Override
    public String toString() {
        return number;
    }
}
