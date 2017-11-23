package org.cuberact.json.input;

import java.io.IOException;
import java.io.Reader;
import java.util.Objects;

/**
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public class JsonInputReader implements JsonInput {

    private final Reader input;

    /**
     * @param input - {@link Reader}
     */
    public JsonInputReader(Reader input) {
        this.input = Objects.requireNonNull(input, "input");
    }

    @Override
    public char nextChar() {
        try {
            return (char) input.read();
        } catch (IOException e) {
            return END_OF_INPUT;
        }
    }
}
