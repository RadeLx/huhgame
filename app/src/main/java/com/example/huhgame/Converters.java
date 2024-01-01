package com.example.huhgame;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class Converters {
    private static Gson gson = new Gson();

    @TypeConverter
    public static Cat[] fromString(String value) {
        if (value == null) {
            return null;
        }
        Type listType = new TypeToken<Cat[]>() {}.getType();
        return gson.fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArray(Cat[] cats) {
        return gson.toJson(cats);
    }
}
