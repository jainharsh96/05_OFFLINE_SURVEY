package io.mountblue.offlineSurvey.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import io.mountblue.offlineSurvey.database.ResponceDatabase;

public class AddSurveyResponseViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private ResponceDatabase responseDatabase;
    private int mResponseId;

    public AddSurveyResponseViewModelFactory(ResponceDatabase responceDatabase, int mResponseId) {
        this.responseDatabase = responceDatabase;
        this.mResponseId = mResponseId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AddResponseViewModel(responseDatabase, mResponseId);
    }
}
