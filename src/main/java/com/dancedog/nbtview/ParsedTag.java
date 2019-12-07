package com.dancedog.nbtview;

import com.google.gson.JsonObject;

class ParsedTag {

    final Tag type;
    final String name;
    final JsonObject element;

    ParsedTag(Tag type, String name, JsonObject element) {
        this.type = type;
        this.name = name;
        this.element = element;
    }
}
