package com.dancedog.nbtview;

public enum Tag {
    END(false,1),
    BYTE(true,1),
    SHORT(true,2),
    INT(true,4),
    LONG(true,8),
    FLOAT(true,4),
    DOUBLE(true,8),
    BYTE_ARRAY(true,-1),
    STRING(true,-1),
    LIST(true,-1),
    COMPOUND(true,-1),
    INT_ARRAY(true,-1),
    LONG_ARRAY(true,-1);

    private final boolean hasName;
    private final int payloadSize;

    Tag(boolean hasName, int payloadSize) {
        this.hasName = hasName;
        this.payloadSize = payloadSize;
    }

    public boolean hasName() {
        return hasName;
    }

    public int getPayloadSize() {
        return payloadSize;
    }

    public static Tag fromID(int id) {
        switch (id) {
            case 1:
                return BYTE;
            case 2:
                return SHORT;
            case 3:
                return INT;
            case 4:
                return LONG;
            case 5:
                return FLOAT;
            case 6:
                return DOUBLE;
            case 7:
                return BYTE_ARRAY;
            case 8:
                return STRING;
            case 9:
                return LIST;
            case 10:
                return COMPOUND;
            case 11:
                return INT_ARRAY;
            case 12:
                return LONG_ARRAY;
            default:
                return END;
        }
    }
}
