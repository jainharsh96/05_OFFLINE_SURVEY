package io.mountblue.offlineSurvey.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import io.mountblue.offlineSurvey.database.ResponceDatabase;
import io.mountblue.offlineSurvey.model.FormResponse;

public class AddResponseViewModel extends ViewModel {

    private LiveData<FormResponse> formResponses;

    AddResponseViewModel(ResponceDatabase responceDatabase, int mResponseId) {
        formResponses = responceDatabase.responceDao().loadResponseById(mResponseId);
    }

    public LiveData<FormResponse> getFormResponses() {
        return formResponses;
    }
}
