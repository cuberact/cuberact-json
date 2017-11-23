package org.cuberact.json.optimize;

/**
 * @author Michal Nikodim (michal.nikodim@topmonks.com)
 */
public final class CharTable {

    private static final int[] CHAR_TO_INT = new int[103];
    private static final long[] CHAR_TO_LONG = new long[103];
    
    private static final int[] HEX_BIT_SHIFT = new int[]{12, 8, 4, 0};

    static {
        CHAR_TO_INT['0'] = 0;
        CHAR_TO_INT['1'] = 1;
        CHAR_TO_INT['2'] = 2;
        CHAR_TO_INT['3'] = 3;
        CHAR_TO_INT['4'] = 4;
        CHAR_TO_INT['5'] = 5;
        CHAR_TO_INT['6'] = 6;
        CHAR_TO_INT['7'] = 7;
        CHAR_TO_INT['8'] = 8;
        CHAR_TO_INT['9'] = 9;
        CHAR_TO_INT['a'] = 10;
        CHAR_TO_INT['b'] = 11;
        CHAR_TO_INT['c'] = 12;
        CHAR_TO_INT['d'] = 13;
        CHAR_TO_INT['e'] = 14;
        CHAR_TO_INT['f'] = 15;
        CHAR_TO_INT['A'] = 10;
        CHAR_TO_INT['B'] = 11;
        CHAR_TO_INT['C'] = 12;
        CHAR_TO_INT['D'] = 13;
        CHAR_TO_INT['E'] = 14;
        CHAR_TO_INT['F'] = 15;

        CHAR_TO_LONG['0'] = 0;
        CHAR_TO_LONG['1'] = 1;
        CHAR_TO_LONG['2'] = 2;
        CHAR_TO_LONG['3'] = 3;
        CHAR_TO_LONG['4'] = 4;
        CHAR_TO_LONG['5'] = 5;
        CHAR_TO_LONG['6'] = 6;
        CHAR_TO_LONG['7'] = 7;
        CHAR_TO_LONG['8'] = 8;
        CHAR_TO_LONG['9'] = 9;
        CHAR_TO_LONG['a'] = 10;
        CHAR_TO_LONG['b'] = 11;
        CHAR_TO_LONG['c'] = 12;
        CHAR_TO_LONG['d'] = 13;
        CHAR_TO_LONG['e'] = 14;
        CHAR_TO_LONG['f'] = 15;
        CHAR_TO_LONG['A'] = 10;
        CHAR_TO_LONG['B'] = 11;
        CHAR_TO_LONG['C'] = 12;
        CHAR_TO_LONG['D'] = 13;
        CHAR_TO_LONG['E'] = 14;
        CHAR_TO_LONG['F'] = 15;
    }

    private CharTable() {
        //utility class
    }

    public static int toInt(char c) {
        return CHAR_TO_INT[c];
    }

    public static long toLong(char c) {
        return CHAR_TO_LONG[c];
    }

    public static int hexBitShift(int order) {
        return HEX_BIT_SHIFT[order];
    }
}
