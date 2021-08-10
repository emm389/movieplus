package com.emmanuel.movieplus.database.converters;

import com.emmanuel.movieplus.moviedetail.model.BelongsToCollection;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import androidx.room.TypeConverter;

/**
 * Created by Emmanuel on 8/7/2021.
 */

public class BelongsToCollectionConverter {

    @TypeConverter
    public static BelongsToCollection fromString(String value) {
        Type type = new TypeToken<BelongsToCollection>() {}.getType();
        return new Gson().fromJson(value, type);
    }

    @TypeConverter
    public static String fromObject(BelongsToCollection belongsToCollection) {
        Gson gson = new Gson();
        return gson.toJson(belongsToCollection);
    }
}
