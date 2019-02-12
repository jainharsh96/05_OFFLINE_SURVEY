package io.mountblue.offlineSurvey.background;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import io.mountblue.offlineSurvey.BuildConfig;
import io.mountblue.offlineSurvey.R;
import io.mountblue.offlineSurvey.model.Answer;
import io.mountblue.offlineSurvey.model.FormResponse;
import io.mountblue.offlineSurvey.model.JsonFormResponse;

public class CreateResponsesFileService extends Service {

    private final static String ERROR_TAG = "ERROR";
    private static String RESPONSES_FILE_PATH = "";
    private Context context;
    private List<FormResponse> formResponseList;
    private NotificationManager nManager;
    private String PRIMARY_CHANNEL_ID = "Json creation notify";

    public CreateResponsesFileService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException(context.getResources().getString(R.string.NOT_IMPLEMENTED));
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public void setData(List<FormResponse> formResponseList, Context context) {
        this.formResponseList = formResponseList;
        this.context = context;
        new CreateJsonFileAsyncTask().execute();

    }

    @SuppressLint("StaticFieldLeak")
    class CreateJsonFileAsyncTask extends AsyncTask<Void, Void, Integer> {
        private String RESPONSES_FILE_NAME = "/responses.json";
        private Integer fill_form_i = 1;
        private Integer saved_i = 2;

        CreateJsonFileAsyncTask() {
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            String QUE_FORM_ID = context.getResources().getString(R.string.QUESTION_FORM);
            QUE_FORM_ID = QUE_FORM_ID.replace(context.getResources().getString(R.string.FILE_EXTENSION), "");

            List<JsonFormResponse> responsesForJson = new ArrayList<>();
            for (int i = 0; i < formResponseList.size(); i++) {

                JsonFormResponse response = new JsonFormResponse();
                response.setqueFormId(QUE_FORM_ID);
                response.setLastUpdate(formResponseList.get(i).getLastUpdate());
                response.setUUID(formResponseList.get(i).getUUID());

                LinkedHashMap<String, String> answerList = new LinkedHashMap<String, String>();
                List<Answer> answers = formResponseList.get(i).getAnswerList();

                for (int j = 0; j < formResponseList.get(i).getAnswerList().size(); j++) {
                    answerList.put(answers.get(j).getId(), answers.get(j).getAnswer());
                }

                response.setAnswers(answerList);
                responsesForJson.add(response);
            }
            if (responsesForJson.size() < 1) {
                return fill_form_i;
            }

            Gson gson = new GsonBuilder().create();
            FileWriter fileWriter;
            RESPONSES_FILE_PATH = Environment.getExternalStorageDirectory().getPath().toString()
                    + "/" + context.getPackageName().toString();
            File createFile = new File(RESPONSES_FILE_PATH);

            if (!createFile.exists()) {
                createFile.mkdir();
            }
            try {
                fileWriter = new FileWriter(RESPONSES_FILE_PATH + RESPONSES_FILE_NAME);
                fileWriter.write(gson.toJson(responsesForJson));
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                Log.e(ERROR_TAG, context.getResources().getString(R.string.error_msg) + e.getLocalizedMessage());
            }

            RESPONSES_FILE_PATH += RESPONSES_FILE_NAME;
            return saved_i;
        }

        @Override
        protected void onPostExecute(Integer value) {
            super.onPostExecute(value);
            if (value.equals(fill_form_i)) {
                Toast.makeText(context, context.getResources().getString(R.string.FILL_FORM_MSG),
                        Toast.LENGTH_LONG).show();
            } else if (value.equals(saved_i)) {
                Toast.makeText(context, context.getResources().getString(R.string.SAVED) + RESPONSES_FILE_PATH,
                        Toast.LENGTH_LONG).show();

                createNotificationChannel();
                createNotification();
            }
            stopSelf();
        }

        void createNotificationChannel() {

            nManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

            if (android.os.Build.VERSION.SDK_INT >=
                    android.os.Build.VERSION_CODES.O) {

                NotificationChannel notificationChannel = new NotificationChannel
                        (PRIMARY_CHANNEL_ID,
                                context.getResources().getString(R.string.file_create_notification),
                                NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationChannel.enableVibration(true);
                notificationChannel.setDescription
                        (context.getResources().getString(R.string.notify_file_path));

                nManager.createNotificationChannel(notificationChannel);
            }
        }

        private void createNotification() {

            NotificationCompat.Builder nBuilder = new NotificationCompat.Builder
                    (context, PRIMARY_CHANNEL_ID)
                    .setContentTitle(context.getResources().getString(R.string.FILE_SAVE_MSG))
                    .setContentText(context.getResources().getString(R.string.open) + RESPONSES_FILE_PATH)
                    .setSmallIcon(R.drawable.ic_save_black_24dp)
                    .setContentIntent(getPendingIntent())
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setAutoCancel(true);

            nManager.notify(0, nBuilder.build());
        }

        private PendingIntent getPendingIntent() {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setAction(android.content.Intent.ACTION_VIEW);

            File responseFile = new File(RESPONSES_FILE_PATH);
            Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID
                    + context.getResources().getString(R.string.provider), responseFile);
            intent.setData(uri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        }
    }
}