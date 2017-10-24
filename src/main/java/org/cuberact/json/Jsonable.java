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

/**
 * Iterface for POJOs
 *
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public interface Jsonable {

    /**
     * Serialize POJO to json
     *
     * @param <E> - {@link JsonObject} or {@link JsonArray}
     * @return JsonObject or JsonArray
     */
    <E extends Json> E toJson();

    /**
     * Deserialize POJO inner states an attributes from json
     *
     * @param json - JsonObject or JsonArray
     */
    void fromJson(Json json);
}
