package com.emmanuel.movieplus.database.converters;

import com.emmanuel.movieplus.moviedetail.model.Videos;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import androidx.room.TypeConverter;

/**
 * Created by Emmanuel on 8/7/2021.
 */

public class VideosConverter {

    @TypeConverter
    public static Videos fromString(String value) {
        Type type = new TypeToken<Videos>() {}.getType();
        return new Gson().fromJson(value, type);
    }

    @TypeConverter
    public static String fromObject(Videos videos) {
        Gson gson = new Gson();
        return gson.toJson(videos);
    }
}
