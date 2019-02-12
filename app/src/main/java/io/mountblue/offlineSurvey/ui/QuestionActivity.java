package io.mountblue.offlineSurvey.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.List;

import io.mountblue.offlineSurvey.R;
import io.mountblue.offlineSurvey.database.ResponceDatabase;
import io.mountblue.offlineSurvey.dialog.ClosingDialogFragment;
import io.mountblue.offlineSurvey.model.Answer;
import io.mountblue.offlineSurvey.model.FormResponse;
import io.mountblue.offlineSurvey.viewmodels.AddResponseViewModel;
import io.mountblue.offlineSurvey.viewmodels.AddSurveyResponseViewModelFactory;

public class QuestionActivity extends AppCompatActivity {

    private final static String RESPONSE_KEY = "response_key";
    private final static String RESPONSE_ID = "response_id";
    private int DEFAULT_VALUE = -1;
    private int responseId = DEFAULT_VALUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            responseId = extras.getInt(RESPONSE_KEY);
            setUpViewModel();
        } else {
            QuestionsFragment fragment = new QuestionsFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_question, fragment).commit();
        }

    }

    private void setUpViewModel() {
        ResponceDatabase database = ResponceDatabase.getInstance(this);
        AddSurveyResponseViewModelFactory addSurveyResponseViewModelFactory = new AddSurveyResponseViewModelFactory(database, responseId);
        final AddResponseViewModel addResponseViewModel = ViewModelProviders.of(this, addSurveyResponseViewModelFactory).get(AddResponseViewModel.class);
        addResponseViewModel.getFormResponses().observe(this, new Observer<FormResponse>() {
            @Override
            public void onChanged(@Nullable FormResponse formResponse) {
                addResponseViewModel.getFormResponses().removeObserver(this);

                ArrayList<Answer> answers = new ArrayList<Answer>();

                assert formResponse != null;
                answers = formResponse.getAnswerList();
                QuestionsFragment fragment = new QuestionsFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(RESPONSE_ID, responseId);
                bundle.putParcelableArrayList(RESPONSE_KEY, answers);
                fragment.setArguments(bundle);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_question, fragment).commit();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            hideSoftKeyboard();

            ClosingDialogFragment closingDialogFragment = new ClosingDialogFragment();
            closingDialogFragment.show(getSupportFragmentManager(), null);
        }
        return true;
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment questionFragment : fragments) {
            if (questionFragment != null && questionFragment instanceof QuestionsFragment)
                ((QuestionsFragment) questionFragment).backPressed();
        }
    }
}
