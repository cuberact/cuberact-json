package org.cuberact.json.input;

import java.io.IOException;
import java.io.Reader;

/**
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public class JsonInputReader extends JsonInputBase {

    private final Reader input;
    private boolean readLastCharAgain = false;
    private int position = 0;
    private char[] errorBuffer = new char[40];
    private int errorBufferIndex = 0;

    /**
     * @param input - {@link Reader}
     */
    public JsonInputReader(Reader input) {
        this.input = input;
    }

    @Override
    public char nextChar() {
        if (readLastCharAgain) {
            readLastCharAgain = false;
            return actualChar;
        }
        try {
            actualChar = (char) input.read();
            position++;
            errorBuffer[errorBufferIndex] = actualChar;
            errorBufferIndex = (errorBufferIndex + 1) % 40;
        } catch (IOException e) {
            actualChar = END_OF_INPUT;
        }
        return actualChar;
    }

    @Override
    public void readLastCharAgain() {
        readLastCharAgain = true;
    }

    public String buildExceptionMessage(String error) {
        StringBuilder message = new StringBuilder("Parse error on position ");
        message.append(position).append("\n\n");
        try {
            int arrowPosition = -1;
            for (int i = 0; i < errorBuffer.length; i++) {
                char c = errorBuffer[errorBufferIndex];
                errorBufferIndex = (errorBufferIndex + 1) % 40;
                if (!isWhiteChar(c) && c != 0 && c != END_OF_INPUT) {
                    message.append(c);
                    arrowPosition++;
                }
            }
            for (int i = 0; i < 40; i++) {
                char c = nextChar();
                if (!isWhiteChar(c)) {
                    if (c == END_OF_INPUT) {
                        break;
                    }
                    message.append(c);
                }
            }
            message.append("\n");
            for (int i = 0; i < arrowPosition; i++) {
                message.append(".");
            }
            message.append("^ ERROR - ").append(error).append("\n");
        } catch (Throwable t) {
            message.append(" [pointer build failed]\n");
        }
        return message.toString();
    }
}
