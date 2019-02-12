package io.mountblue.offlineSurvey.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import io.mountblue.offlineSurvey.R;
import io.mountblue.offlineSurvey.model.FormResponse;

public class ResponceAdapter extends RecyclerView.Adapter<ResponceAdapter.ResponseViewHolder> {
    private Context context;
    private List<FormResponse> formResponseList;
    private ArrayList<String> FORMSTATUS = new ArrayList<>();

    public ResponceAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ResponseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.response_view_holder, viewGroup, false);
        return new ResponceAdapter.ResponseViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ResponseViewHolder responseViewHolder, int position) {
        FormResponse formResponse = formResponseList.get(position);
        FORMSTATUS.add(" ");
        FORMSTATUS.add(context.getString(R.string.FILLED));

        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd  HH:mm a");
        String Date = simpleDateFormat.format(formResponse.getLastUpdate());

        responseViewHolder.formName.setText(formResponse.getResposeName() + formResponse.getResponceId());
        responseViewHolder.formLastUpdate.setText(Date);
    }

    @Override
    public int getItemCount() {
        if (formResponseList == null) {
            return 0;
        }
        return formResponseList.size();
    }

    public List<FormResponse> getResponseList() {
        return formResponseList;
    }

    public void setResponseList(List<FormResponse> responseList) {
        this.formResponseList = responseList;
        notifyDataSetChanged();
    }

    class ResponseViewHolder extends RecyclerView.ViewHolder {
        TextView formStatus;
        TextView formName;
        TextView formLastUpdate;

        ResponseViewHolder(View itemview) {
            super(itemview);
            formName = itemview.findViewById(R.id.response_name);
            formLastUpdate = itemview.findViewById(R.id.response_last_update);
        }
    }
}
