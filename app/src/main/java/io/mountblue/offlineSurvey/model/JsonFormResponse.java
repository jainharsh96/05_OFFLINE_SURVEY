package io.mountblue.offlineSurvey.model;

import java.util.Date;
import java.util.LinkedHashMap;

public class JsonFormResponse {

    private String formId;

    private Date lastUpdate;

    private LinkedHashMap<String, String> answers = null;

    private String UUID;

    public JsonFormResponse() {

    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getqueFormId() {
        return formId;
    }

    public void setqueFormId(String queFormId) {
        this.formId = queFormId;
    }

    public LinkedHashMap<String, String> getAnswers() {
        return answers;
    }

    public void setAnswers(LinkedHashMap<String, String> answers) {
        this.answers = answers;
    }
}
