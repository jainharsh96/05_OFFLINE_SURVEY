package io.mountblue.offlineSurvey.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import java.util.ArrayList;
import java.util.Date;

import io.mountblue.offlineSurvey.database.AnswerConverter;
import io.mountblue.offlineSurvey.database.DateConverter;
import io.mountblue.offlineSurvey.database.DeviceUUIDGenerator;

@Entity(tableName = "responcelist")
public class FormResponse {

    @PrimaryKey(autoGenerate = true)
    private int responceId;

    private String resposeName;

    @TypeConverters(DateConverter.class)
    private Date lastUpdate;

    @TypeConverters(AnswerConverter.class)
    private ArrayList<Answer> answerList = null;

    private String UUID;

    private boolean isFilled;

    public FormResponse() {

    }

    @Ignore
    public FormResponse(Context context) {
        this.UUID = DeviceUUIDGenerator.id(context);
    }

    public boolean isFilled() {
        return isFilled;
    }

    public void setFilled(boolean filled) {
        isFilled = filled;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public ArrayList<Answer> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(ArrayList<Answer> answerList) {
        this.answerList = answerList;
    }

    public String getResposeName() {
        return resposeName;
    }

    public void setResposeName(String resposeName) {
        this.resposeName = resposeName;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public int getResponceId() {
        return responceId;
    }

    public void setResponceId(int responceId) {
        this.responceId = responceId;
    }
}


