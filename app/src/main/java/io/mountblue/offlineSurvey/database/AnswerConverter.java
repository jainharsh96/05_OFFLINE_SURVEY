package io.mountblue.offlineSurvey.database;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import io.mountblue.offlineSurvey.model.Answer;

public class AnswerConverter {

    @TypeConverter
    public String fromAnswerList(ArrayList<Answer> answerList) {
        if (answerList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Answer>>() {
        }.getType();
        return gson.toJson(answerList, type);
    }

    @TypeConverter
    public ArrayList<Answer> toAnswerList(String answerString) {
        if (answerString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Answer>>() {
        }.getType();
        return gson.fromJson(answerString, type);
    }
}
