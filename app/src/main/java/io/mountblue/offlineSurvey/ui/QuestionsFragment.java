package io.mountblue.offlineSurvey.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

import io.mountblue.offlineSurvey.R;
import io.mountblue.offlineSurvey.background.AppExecuter;
import io.mountblue.offlineSurvey.background.FetchingQuestionAsyncTask;
import io.mountblue.offlineSurvey.database.ResponceDatabase;
import io.mountblue.offlineSurvey.dialog.ClosingDialogFragment;
import io.mountblue.offlineSurvey.model.Answer;
import io.mountblue.offlineSurvey.model.FormResponse;
import io.mountblue.offlineSurvey.model.Options;
import io.mountblue.offlineSurvey.model.QuestionForm;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionsFragment extends Fragment implements FetchingQuestionAsyncTask.CompleteFetching, BackPressed {

    private final static String INPUT_TYPE_INPUT = "input";
    private final static String INPUT_TYPE_SELECT = "select";
    private final static String COMMA_SEPARATOR = ",";
    private final static String RESPONSE_KEY = "response_key";
    private final static String RESPONSE_ID = "response_id";
    private final static String COUNT_QUESTION_KEY = "count_question";
    private List<QuestionForm> questions;
    private EditText answerTypeInputEt;
    private int countQuestion = 0;
    private TextView questionsTv;
    private LinearLayout dynamicViewsLayout;
    private String inputType;
    private View view;
    private String radioButtonAnswer;
    private LinkedHashSet<String> checkedValues = new LinkedHashSet<>();
    private boolean setValue = false;
    private FormResponse formResponse;
    private boolean saved = false;
    private int DEFAULT_RESPONSE_ID = -1;
    private int responseId = DEFAULT_RESPONSE_ID;
    private ResponceDatabase database;
    private TextView nextQuestion, previousQuestion;
    private FrameLayout frameLayout;
    private boolean isFilled;
    private Toast showEmptyMsg;

    public QuestionsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_questions, container, false);

        if (savedInstanceState != null) {
            saved = true;
        }

        initButtons(view);

        database = ResponceDatabase.getInstance(getContext());
        new FetchingQuestionAsyncTask(getActivity(), this).execute();

        dynamicViewsLayout = view.findViewById(R.id.dynamic_views_layout);
        questionsTv = view.findViewById(R.id.tv_questions);

        return view;
    }

    private void initButtons(View view) {
        nextQuestion = view.findViewById(R.id.tv_next);
        previousQuestion = view.findViewById(R.id.tv_previous);
        final Button saveButton = view.findViewById(R.id.btn_save);
        frameLayout = view.findViewById(R.id.finish_view);

        nextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAnswer();

                if (countQuestion == questions.size() - 1) {
                    showExitMassege();
                    return;
                }
                if (questions.get(countQuestion).getRequired()) {
                    if (setValue) {
                        if (showEmptyMsg != null) {
                            showEmptyMsg.cancel();
                        }

                        previousQuestion.setVisibility(View.VISIBLE);
                        if (countQuestion <= questions.size() - 1) {
                            countQuestion++;
                            setViewsData();
                        }
                        setValue = false;
                    } else {
                        if (inputType.equals(INPUT_TYPE_INPUT)) {
                            answerTypeInputEt.setError(getString(R.string.required_msg));
                        } else {
                            if (showEmptyMsg != null) {
                                showEmptyMsg.cancel();
                            }
                            showEmptyMsg = Toast.makeText(getActivity(), getString(R.string.required_msg), Toast.LENGTH_SHORT);
                            showEmptyMsg.show();
                        }
                    }
                } else {
                    if (countQuestion <= questions.size() - 1) {
                        countQuestion++;
                        setViewsData();
                    }
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saved = true;
                isFilled = true;

                addDataToDataBase();
                Objects.requireNonNull(getActivity()).finish();
            }
        });

        previousQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPreviousQuestion();
            }
        });
    }

    private void showExitMassege() {
        final InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

        countQuestion++;
        questionsTv.setVisibility(View.GONE);

        dynamicViewsLayout.setVisibility(View.GONE);

        frameLayout.setVisibility(View.VISIBLE);
        nextQuestion.setVisibility(View.INVISIBLE);
    }

    private void getPreviousQuestion() {
        dynamicViewsLayout.setVisibility(View.VISIBLE);
        if (countQuestion <= questions.size() - 1) {
            setAnswer();
        }

        questionsTv.setVisibility(View.VISIBLE);
        nextQuestion.setVisibility(View.VISIBLE);

        countQuestion--;

        frameLayout.setVisibility(View.GONE);
        nextQuestion.setVisibility(View.VISIBLE);

        if (countQuestion == 0) {
            previousQuestion.setVisibility(View.INVISIBLE);
        }
        setViewsData();
    }

    private void setAnswer() throws IndexOutOfBoundsException {
        if (countQuestion >= 0 && countQuestion <= questions.size() - 1) {
            switch (inputType) {
                case INPUT_TYPE_INPUT:
                    String answer = answerTypeInputEt.getText().toString();

                    if (!answer.equals(questions.get(countQuestion).getAnswer())) {
                        questions.get(countQuestion).setAnswer(answer);
                    }

                    setValue = answer.length() != 0;
                    break;
                case INPUT_TYPE_SELECT:
                    if (radioButtonAnswer != null && !radioButtonAnswer.equals(questions.get(countQuestion).getAnswer())) {
                        questions.get(countQuestion).setAnswer(radioButtonAnswer);
                    }

                    assert radioButtonAnswer != null;
                    if (radioButtonAnswer.length() == 0) {
                        setValue = false;
                    } else {
                        setValue = true;
                    }
                    break;
                default:
                    setValue = checkedValues.size() > 0;
                    String checkBoxValues = TextUtils.join(COMMA_SEPARATOR, checkedValues);

                    if (!checkBoxValues.equals(questions.get(countQuestion).getAnswer())) {
                        questions.get(countQuestion).setAnswer(checkBoxValues);
                    } else if (checkBoxValues.length() == 0) {
                        questions.get(countQuestion).setAnswer(null);
                    }
                    break;
            }
        }
    }

    @Override
    public void onComplete(List<QuestionForm> questions) {
        this.questions = questions;

        if (getArguments() != null) {
            ArrayList<Answer> answers = getArguments().getParcelableArrayList(RESPONSE_KEY);
            responseId = getArguments().getInt(RESPONSE_ID);

            int position = 0;
            assert answers != null;
            for (Answer answer : answers) {
                String questionId = answer.getId();
                if (this.questions.get(position).getId().equals(questionId)) {
                    this.questions.get(position).setAnswer(answer.getAnswer());
                }
                position++;
            }

            if (countQuestion == questions.size() - 1) {
                showExitMassege();
            } else {
                setViewsData();
            }
        } else {
            setViewsData();
        }
    }

    private void setViewsData() {
        questionsTv.setText(questions.get(countQuestion).getText());

        final InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputType = questions.get(countQuestion).getType();

        switch (inputType) {
            case INPUT_TYPE_INPUT:
                addEditText();
                break;
            case INPUT_TYPE_SELECT:
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                addRadioButtons();
                break;
            default:
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                addCheckBoxes();
                break;
        }
    }

    private void addEditText() {
        dynamicViewsLayout.removeAllViews();

        answerTypeInputEt = new EditText(view.getContext());
        answerTypeInputEt.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        answerTypeInputEt.setHint(getString(R.string.hint_ans));

        if (questions.get(countQuestion).getAnswer() != null) {
            answerTypeInputEt.setText(questions.get(countQuestion).getAnswer());
        }

        answerTypeInputEt.setBackgroundResource(R.drawable.rectangale_shape);
        answerTypeInputEt.setTextAppearance(R.style.TextAppearance_AppCompat_Medium);

        dynamicViewsLayout.addView(answerTypeInputEt);

        answerTypeInputEt.requestFocus();
        ((InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    private void addCheckBoxes() {
        List<Options> allOptions = questions.get(countQuestion).getOptions();
        dynamicViewsLayout.removeAllViews();
        checkedValues.clear();

        for (int i = 0; i < allOptions.size(); i++) {
            final CheckBox checkBoxOptions = new CheckBox(view.getContext());
            checkBoxOptions.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            checkBoxOptions.setText(allOptions.get(i).getLabel());
            checkBoxOptions.setTextAppearance(R.style.TextAppearance_AppCompat_Medium);
            checkBoxOptions.setTag(allOptions.get(i).getValue());
            dynamicViewsLayout.addView(checkBoxOptions);

            if (questions.get(countQuestion).getAnswer() != null) {
                String selectedAnswer = questions.get(countQuestion).getAnswer();
                int startIndex = 0;
                int endIndex = selectedAnswer.indexOf(COMMA_SEPARATOR);

                if (endIndex != -1) {
                    while (endIndex != -1) {
                        String item = selectedAnswer.substring(startIndex, endIndex);
                        checkedValues.add(item);

                        if (checkBoxOptions.getTag().toString().equals(item)) {
                            checkBoxOptions.setChecked(true);
                        }

                        startIndex = endIndex + 1;
                        endIndex = selectedAnswer.indexOf(COMMA_SEPARATOR, startIndex);

                        if (endIndex == -1) {
                            item = selectedAnswer.substring(startIndex, selectedAnswer.length());
                            checkedValues.add(item);

                            if (checkBoxOptions.getTag().toString().equals(item)) {
                                checkBoxOptions.setChecked(true);
                            }
                        }
                    }
                } else {
                    if (checkBoxOptions.getTag().toString().equals(selectedAnswer)) {
                        checkBoxOptions.setChecked(true);
                        checkedValues.add(selectedAnswer);
                    }
                }
            }
            checkBoxOptions.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    String value = checkBoxOptions.getTag().toString();
                    if (checkedValues.contains(value)) {
                        checkedValues.remove(value);
                    } else {
                        checkedValues.add(checkBoxOptions.getTag().toString());
                    }
                }
            });
        }

        dynamicViewsLayout.setVisibility(View.VISIBLE);
    }

    private void addRadioButtons() {
        final List<Options> allOptions = questions.get(countQuestion).getOptions();

        dynamicViewsLayout.removeAllViews();
        RadioGroup optionsRadioGroup = new RadioGroup(view.getContext());
        optionsRadioGroup.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        optionsRadioGroup.setOrientation(LinearLayout.VERTICAL);
        radioButtonAnswer = "";

        for (int i = 0; i < allOptions.size(); i++) {
            final RadioButton options = new RadioButton(view.getContext());
            options.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            options.setId(View.generateViewId());
            options.setText(allOptions.get(i).getLabel());
            options.setTextAppearance(R.style.TextAppearance_AppCompat_Medium);
            options.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            options.setTag(allOptions.get(i).getValue());

            optionsRadioGroup.addView(options);

            if (questions.get(countQuestion).getAnswer() != null) {
                if (options.getTag().toString().equals(questions.get(countQuestion).getAnswer())) {
                    optionsRadioGroup.check(options.getId());
                    radioButtonAnswer = questions.get(countQuestion).getAnswer();
                }
            }

            options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    radioButtonAnswer = options.getTag().toString();
                }
            });
        }

        dynamicViewsLayout.addView(optionsRadioGroup);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (!saved) {
            setAnswer();
            addDataToDataBase();
        }
    }

    private void addDataToDataBase() {
        ArrayList<Answer> answers = new ArrayList<Answer>();
        for (QuestionForm question : questions) {
            Answer answer = new Answer(question.getId(), question.getAnswer());
            answers.add(answer);
        }

        formResponse = new FormResponse(getActivity());
        formResponse.setResposeName(getString(R.string.form_response));
        formResponse.setLastUpdate(new Date());
        formResponse.setAnswerList(answers);

        if (isFilled) {
            formResponse.setFilled(true);
        }

        AppExecuter.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (responseId == DEFAULT_RESPONSE_ID) {
                    database.responceDao().insertResponce(formResponse);
                } else {
                    formResponse.setResponceId(responseId);
                    database.responceDao().updateResponceById(formResponse);
                }
            }
        });
    }

    @Override
    public void backPressed() {
        if (countQuestion > 0) {
            getPreviousQuestion();
        } else {
            ClosingDialogFragment closingDialogFragment = new ClosingDialogFragment();
            closingDialogFragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), null);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(COUNT_QUESTION_KEY, countQuestion);
        super.onSaveInstanceState(outState);
    }
}
