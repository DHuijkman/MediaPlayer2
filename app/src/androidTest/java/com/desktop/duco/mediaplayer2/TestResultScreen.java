package com.desktop.duco.mediaplayer2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import javax.annotation.Nullable;

import static android.support.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;

public class TestResultScreen extends Fragment {
    private View v;
    private int testsRunAmount;
    private int setsRunAmount;
    private int currentSet;
    private int testPassedAmount;
    private String error;
    TextView timer;

    public TestResultScreen(@Nullable String error, int testsRunAmount, int currentSet, int setsRunAmount, int testPassedAmount) {
        this.error = error;
        this.testsRunAmount = testsRunAmount;
        this.setsRunAmount = setsRunAmount;
        this.currentSet = currentSet + 1;
        this.testPassedAmount = testPassedAmount;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (v == null)
            v = inflater.inflate(R.layout.activity_test_result_screen,container, false);


        TextView testsRun = v.findViewById(R.id.test_run_value);
        TextView setsRun = v.findViewById(R.id.sets_run_value);
        TextView testPassedText = v.findViewById(R.id.test_passed_text);
        TextView testPassedPercentage = v.findViewById(R.id.test_passed_percentage);
        TextView errorMessage = v.findViewById(R.id.test_error_result_text);
        timer = v.findViewById(R.id.test_count_down);
        ProgressBar testPassedBar = v.findViewById(R.id.tests_passed_bar);


        testsRun.setText(String.valueOf(testsRunAmount));
        setsRun.setText(currentSet + "/" + setsRunAmount);
        testPassedText.setText("test passed: " + testPassedAmount + "/" + testsRunAmount);

        int percentage = (100 / testsRunAmount) * testPassedAmount;
        testPassedPercentage.setText(percentage + "%");

        if(error == null || error.isEmpty()) {
            errorMessage.setText("no test failed, congratulations!");
        } else {
            errorMessage.setText(error);
        }

        testPassedBar.setMax(testsRunAmount);
        testPassedBar.setProgress(testPassedAmount);

        return v;
    }

    public void countDown(final int i) {
        try {
            runOnUiThread(() -> timer.setText("Tests resume in : " + i));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }
}
