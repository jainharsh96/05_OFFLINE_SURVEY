package io.mountblue.offlineSurvey.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import io.mountblue.offlineSurvey.R;
import io.mountblue.offlineSurvey.background.CreateResponsesFileService;
import io.mountblue.offlineSurvey.model.FormResponse;

public class MainActivity extends AppCompatActivity {

    private final static String RESPONSE_KEY = "response_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startHomePage();
    }

    public void startSurvey() {
        Intent intent = new Intent(this, QuestionActivity.class);
        startActivity(intent);
    }

    public void updateSurveyResponse(int responseId) {
        Intent intent = new Intent(this, QuestionActivity.class);
        intent.putExtra(RESPONSE_KEY, responseId);
        startActivity(intent);
    }

    public void startHomePage() {
        FragmentHomePage fragment = new FragmentHomePage();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment).commit();
    }

    public void startServiceForCreatingJson(List<FormResponse> responseList) {
        Intent intent = new Intent(this, CreateResponsesFileService.class);
        startService(intent);
        CreateResponsesFileService createJsonService = new CreateResponsesFileService();
        createJsonService.setData(responseList, this);
    }
}
