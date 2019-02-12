package io.mountblue.offlineSurvey.background;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.mountblue.offlineSurvey.R;
import io.mountblue.offlineSurvey.model.QuestionForm;

public class FetchingQuestionAsyncTask extends AsyncTask<Void, Void, List<QuestionForm>> {

    private static final Type QUESTION_FORMATE = new TypeToken<List<QuestionForm>>() {
    }.getType();

    @SuppressLint("StaticFieldLeak")
    private Context context;
    private CompleteFetching onCompleteFetching;

    public FetchingQuestionAsyncTask(Context context, CompleteFetching onCompleteFetching) {
        this.onCompleteFetching = onCompleteFetching;
        this.context = context;
    }

    @Override
    protected List<QuestionForm> doInBackground(Void... params) {

        Gson gson = new Gson();
        List<QuestionForm> questions = new ArrayList<QuestionForm>();

        try {
            String QUESTION_FORM = context.getString(R.string.QUESTION_FORM);
            InputStream stream = context.getAssets().open(QUESTION_FORM);
            JsonReader reader = new JsonReader(new InputStreamReader(stream));
            questions = gson.fromJson(reader, QUESTION_FORMATE);
        } catch (FileNotFoundException e) {
            Log.d(context.getString(R.string.ERROR_IN_FILE_REPORT), e.toString());
        } catch (IOException a) {
            Log.d(context.getString(R.string.ERROR_IN_IO_REPORT), a.toString());
        }

        return questions;
    }

    @Override
    protected void onPostExecute(List<QuestionForm> questions) {
        onCompleteFetching.onComplete(questions);
        super.onPostExecute(questions);
    }

    public interface CompleteFetching {
        void onComplete(List<QuestionForm> questions);
    }
}
