package com.dancedog.nbtview;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;

public class Parser {

    /**
     * The bytes that make up the original NBT data
     */
    private final byte[] bytes;

    /**
     * The current index of this parser index.
     * Only increments in a positive direction.
     */
    private int index = 0;

    /**
     * An NBT to JSON parser
     * @param bytes The pre-unzipped bytes to parse as NBT
     */
    public Parser(byte[] bytes) {
        this.bytes = bytes;
    }

    /**
     * Parse the byte array as JSON data
     * @return JsonObject The parsed JSON data
     */
    public JsonObject parse() {
        ParsedTag parsed = this.parseTagAtIndex();
        return parsed.element;
    }

    /**
     * Parse the NBT tag at the parser's current index
     * @return ParsedTag An object representing a parsed NBT tag
     */
    private ParsedTag parseTagAtIndex() {
        JsonObject output = new JsonObject();

        String name = "";
        Tag tag = Tag.fromID(bytes[index]);

        // If the given tag is preceded by a name, parse it
        if (tag.hasName()) {

            // Get the length of the tag's name
            index += 1;
            int nameLength = getShortFromBytes();

            // Get and parse the bytes of the tag's name
            byte[] nameBytes = Arrays.copyOfRange(bytes, index, index + nameLength);
            name = new String(nameBytes);
            index += nameLength;
        }
        if (name.length() == 0) name = "_blank";

        switch (tag) {
            // End of compound / "empty"
            case END:
                index += tag.getPayloadSize();
                break;

            // Individual byte
            case BYTE:
                output.addProperty(name, bytes[index]);
                index += tag.getPayloadSize();
                break;

            // Numbers
            case SHORT:
            case INT:
            case LONG:
            case FLOAT:
            case DOUBLE:
                output.addProperty(name, parseNumber(tag));
                break;

            // String
            case STRING:
                output.addProperty(name, parseString());
                break;

            // Arrays
            case BYTE_ARRAY:
            case INT_ARRAY:
            case LONG_ARRAY:
                output.add(name, parseArray(tag));
                break;

            // Lists
            case LIST:
                output.add(name, parseList());
                break;

            // Compounds
            case COMPOUND:
                boolean closed = false;
                output.add(name, new JsonObject());
                // Loop until an END tag is hit
                while (!closed) {
                    ParsedTag child = parseTagAtIndex();
                    if (child.type == Tag.END) {
                        closed = true;
                    } else {
                        output.getAsJsonObject(name).add(child.name, child.element.get(child.name));
                    }
                }
                break;

            default:
                break;
        }

        // System.out.println("Returning, " + output.toString());
        return new ParsedTag(tag, name, output);
    }

    /**
     * Parse the payload of an Int, Long, Short, Float or Double tag
     * @return Number The parsed number
     */
    private Number parseNumber(Tag tag) {
        byte[] numberBytes = Arrays.copyOfRange(bytes, index, index + tag.getPayloadSize());
        ByteBuffer number = ByteBuffer.wrap(numberBytes).order(ByteOrder.BIG_ENDIAN);
        index += tag.getPayloadSize();

        if (tag == Tag.SHORT) {
            return number.getShort();
        } else if (tag == Tag.INT) {
            return number.getInt();
        } else if (tag == Tag.LONG) {
            return number.getLong();
        } else if (tag == Tag.FLOAT) {
            return number.getFloat();
        } else {
            return number.getDouble();
        }
    }

    /**
     * Parse the payload of a string tag
     * @return String The parsed value
     */
    private String parseString() {
        // Get the length of the string
        int stringLength = getShortFromBytes();

        // Get and parse the bytes of the string
        byte[] stringBytes = Arrays.copyOfRange(bytes, index, index + stringLength);
        index += stringLength;

        return new String(stringBytes);
    }

    /**
     * Parse the payload of a ByteArray, IntArray or LongArray tag
     * @return JsonArray An ordered array representing the NBT array
     */
    private JsonArray parseArray(Tag tag) {
        ArrayList array = new ArrayList();
        int arrayLength = getIntFromBytes();

        Tag arrayType;
        switch (tag) {
            case BYTE_ARRAY:
                arrayType = Tag.BYTE;
                break;
            case INT_ARRAY:
                arrayType = Tag.INT;
                break;
            case LONG_ARRAY:
                arrayType = Tag.LONG;
                break;
            default:
                return NBTView.gson.toJsonTree(array).getAsJsonArray();
        }

        for (int i = 0; i < arrayLength; i++) {
            if (tag == Tag.BYTE_ARRAY) {
                array.add(bytes[index]);
                index += arrayType.getPayloadSize();
            } else {
                array.add(parseNumber(arrayType));
            }

        }
        return NBTView.gson.toJsonTree(array).getAsJsonArray();
    }

    /**
     * Parse the payload of a List tag
     * @return JsonArray An array representing the NBT list and its contents
     */
    private JsonArray parseList() {
        JsonArray list = new JsonArray();
        Tag tag = Tag.fromID(bytes[index]);
        index += 1;

        int listLength = getIntFromBytes();

        if (listLength < 1) tag = Tag.END;

        for (int i = 0; i < listLength; i++) {
            switch (tag) {
                // Empty Tags
                case END:
                    return list;

                // Bytes
                case BYTE:
                    list.add(new JsonPrimitive(bytes[index]));
                    index += tag.getPayloadSize();
                    break;

                // Numbers
                case SHORT:
                case INT:
                case LONG:
                case FLOAT:
                case DOUBLE:
                    list.add(new JsonPrimitive(parseNumber(tag)));
                    break;

                // String
                case STRING:
                    list.add(new JsonPrimitive(parseString()));
                    break;

                // Arrays
                case BYTE_ARRAY:
                case INT_ARRAY:
                case LONG_ARRAY:
                    list.add(parseArray(tag));
                    break;

                // Lists (inception much?)
                case LIST:
                    list.add(parseList());
                    break;

                // Compounds
                case COMPOUND:
                    boolean closed = false;
                    JsonObject objectItem = new JsonObject();
                    // Loop until an END tag is hit
                    while (!closed) {
                        ParsedTag child = parseTagAtIndex();
                        if (child.type == Tag.END) {
                            closed = true;
                        } else {
                            objectItem.add(child.name, child.element.get(child.name));
                        }
                    }
                    list.add(objectItem);
                    break;
            }
        }

        return list;
    }

    /**
     * Parse an integer starting at the parser's current index
     * @return int The integer at the current index
     */
    private int getIntFromBytes() {
        ByteBuffer buffer = ByteBuffer.wrap(bytes, index, 4).order(ByteOrder.BIG_ENDIAN);
        index += 4;

        return buffer.getInt();
    }

    /**
     * Parse a short starting at the parser's current index
     * @return short The short at the current index
     */
    private short getShortFromBytes() {
        ByteBuffer buffer = ByteBuffer.wrap(bytes, index, 2).order(ByteOrder.BIG_ENDIAN);
        index += 2;

        return buffer.getShort();
    }
}
