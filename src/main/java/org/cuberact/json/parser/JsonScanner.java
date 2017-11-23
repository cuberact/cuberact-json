package org.cuberact.json.parser;

import org.cuberact.json.input.JsonInput;

/**
 * @author Michal Nikodim (michal.nikodim@topmonks.com)
 */
final class JsonScanner {

    private JsonInput input;
    private char lastReadChar;
    private int position;
    private char[] errorBuffer = new char[32];

    JsonScanner(JsonInput input) {
        this.input = input;
    }

    char nextChar() {
        position++;
        lastReadChar = errorBuffer[position & 31] = input.nextChar();
        return lastReadChar;
    }

    char nextImportantChar() {
        for (; ; ) {
            nextChar();
            if (!isWhiteChar(lastReadChar)) {
                return lastReadChar;
            }
        }
    }

    char lastReadChar() {
        return lastReadChar;
    }

    boolean isWhiteChar(char c) {
        return c == ' ' || c == '\n' || c == '\r' || c == '\t';
    }

    private boolean isLineBreak(char c){
        return c == '\n' || c == '\r';
    }

    /**
     * Build a detailed exception message
     *
     * @param error - short error message
     * @return error or detailed error message
     */
    String buildExceptionMessage(String error){
        StringBuilder message = new StringBuilder("Parse error on position ");
        message.append(position).append("\n\n");
        try {
            int arrowPosition = -1;
            position++;
            for (int i = 0; i < errorBuffer.length; i++) {
                char c = errorBuffer[position & 31];
                position++;
                if (!isLineBreak(c) && c != 0 && c != JsonInput.END_OF_INPUT) {
                    message.append(c);
                    arrowPosition++;
                }
            }
            for (int i = 0; i < 31; i++) {
                char c = nextChar();
                if (!isLineBreak(c)) {
                    if (c == JsonInput.END_OF_INPUT) {
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
