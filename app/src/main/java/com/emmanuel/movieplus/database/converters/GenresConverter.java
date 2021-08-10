package com.emmanuel.movieplus.database.converters;

import com.emmanuel.movieplus.moviedetail.model.Genre;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import androidx.room.TypeConverter;

/**
 * Created by Emmanuel on 8/7/2021.
 */

public class GenresConverter {

    @TypeConverter
    public static List<Genre> fromString(String value) {
        Type listType = new TypeToken<List<Genre>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromList(List<Genre> genres) {
        Gson gson = new Gson();
        return gson.toJson(genres);
    }
}
