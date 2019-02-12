package io.mountblue.offlineSurvey.ui;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import io.mountblue.offlineSurvey.R;
import io.mountblue.offlineSurvey.adapter.ResponceAdapter;
import io.mountblue.offlineSurvey.dialog.AboutAppDialog;
import io.mountblue.offlineSurvey.model.FormResponse;
import io.mountblue.offlineSurvey.viewmodels.SurveyViewModel;

public class FragmentHomePage extends Fragment{
    private TextView emptyMessageView;
    private ResponceAdapter formResponseAdapter;
    private List<FormResponse> responseList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fregment_home_page, container, false);

        Button startSurvey = rootView.findViewById(R.id.button);
        emptyMessageView = rootView.findViewById(R.id.empty_response);
        RecyclerView recyclerView = rootView.findViewById(R.id.surveyrecyclerView);

        setHasOptionsMenu(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        formResponseAdapter = new ResponceAdapter(getActivity());
        recyclerView.setAdapter(formResponseAdapter);

        startSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.startSurvey();
            }
        });

        setUpViewModel();
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.about_app) {
            AboutAppDialog appDialog = new AboutAppDialog(getContext());
            appDialog.show(getActivity().getSupportFragmentManager(), null);

            return true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

            if (responseList == null || responseList.size() < 1) {
                Toast.makeText(getContext(), getString(R.string.FILL_FORM_MSG),
                        Toast.LENGTH_LONG).show();
                return true;
            }

            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.startServiceForCreatingJson(responseList);

            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), getString(R.string.ALLOW_PERMISSION),
                        Toast.LENGTH_LONG).show();
            }
            return super.onOptionsItemSelected(item);
        }
    }

    private void setUpViewModel() {
        SurveyViewModel viewModel = ViewModelProviders.of(this).get(SurveyViewModel.class);
        viewModel.getResponseList().observe(this, new Observer<List<FormResponse>>() {
            @Override
            public void onChanged(@Nullable List<FormResponse> responces) {
                if (responces != null && responces.size() > 0) {
                    emptyMessageView.setVisibility(View.INVISIBLE);
                    responseList = responces;
                    formResponseAdapter.setResponseList(responces);
                } else {
                    emptyMessageView.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
