package io.mountblue.offlineSurvey.model;

import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

public class Answer implements Parcelable {

    public static final Creator<Answer> CREATOR = new Creator<Answer>() {
        @Override
        public Answer createFromParcel(Parcel in) {
            return new Answer(in);
        }

        @Override
        public Answer[] newArray(int size) {
            return new Answer[size];
        }
    };
    @PrimaryKey
    private String questionid;
    private String answer;

    public Answer() {
    }

    public Answer(String id, String answer) {
        this.questionid = id;
        this.answer = answer;
    }

    private Answer(Parcel in) {
        questionid = in.readString();
        answer = in.readString();
    }

    public String getId() {
        return questionid;
    }

    public void setId(String id) {
        this.questionid = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(questionid);
        parcel.writeString(answer);
    }
}
