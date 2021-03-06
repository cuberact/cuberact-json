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

import org.cuberact.json.parser.JsonParser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public class SerializableTest {

    @Test
    public void serializable() throws IOException, ClassNotFoundException {
        String jsonAsString = "{\n" +
                "  \"rect\": [486,\"\\u0048\\u0065\\u006c\\u006C\\u006FWorld\",{\"data\" : \"\\u011B\\u0161\\u010D\\u0159\\u017E\\u00FD\\u00E1\\u00ED\\u00E9\"},-23.54],\n" +
                "  \"perspectiveSelector\": {\n" +
                "    \"perspectives\": [ true, false],\n" +
                "    \"selected\": null,\n" +
                "    \"some\": [1,2,3.2]\n" +
                "  }\n" +
                "}";
        Json json = new JsonParser().parse(jsonAsString);

        //serialize
        ByteArrayOutputStream serializedJson = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(serializedJson);
        oos.writeObject(json);
        oos.close();

        //deserialize
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(serializedJson.toByteArray()));
        Json deserializedJson = (Json) ois.readObject();

        assertEquals(json.toString(), deserializedJson.toString());
    }
}
