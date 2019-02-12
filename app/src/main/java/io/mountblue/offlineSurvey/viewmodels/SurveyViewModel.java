package io.mountblue.offlineSurvey.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import io.mountblue.offlineSurvey.database.ResponceDatabase;
import io.mountblue.offlineSurvey.model.FormResponse;

public class SurveyViewModel extends AndroidViewModel {

    private LiveData<List<FormResponse>> responseList;

    public SurveyViewModel(Application application) {
        super(application);
        ResponceDatabase database = ResponceDatabase.getInstance(this.getApplication());
        responseList = database.responceDao().loadAllResponses();
    }

    public LiveData<List<FormResponse>> getResponseList() {
        return responseList;
    }
}
