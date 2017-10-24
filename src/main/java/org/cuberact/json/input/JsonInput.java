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

package org.cuberact.json.input;

/**
 * Json input abstraction
 *
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public interface JsonInput {

    char END_OF_INPUT = '\uFFFF';

    /**
     * @return actual (last read) char
     */
    char actualChar();

    /**
     * @return next char of input
     */
    char nextChar();

    /**
     * @return next non-white char
     */
    char nextImportantChar();

    /**
     * ensure that next call of {@link JsonInput#nextChar()} read actualChar instead of real next char
     */
    void readLastCharAgain();

    /**
     * Build a detailed exception message
     *
     * @param error - short error message
     * @return error or detailed error message
     */
    String buildExceptionMessage(String error);
}
