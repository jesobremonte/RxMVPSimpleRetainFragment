package com.example.rxtesting;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements MainContract.ActivityView {

    private TextView textView;
    private Button button;

    private MainContract.Presenter presenter;
    private RetainedFragment retainedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        retainedFragment = (RetainedFragment) fm.findFragmentByTag(RetainedFragment.class.getName());

        if (retainedFragment == null) {
            retainedFragment = new RetainedFragment();
            fm.beginTransaction().add(retainedFragment, RetainedFragment.class.getName()).commit();
        } else {
            Log.d(getClass().getName(), "Reusing existing retained fragment.");
        }

        presenter = new MainPresenter(this, retainedFragment);

        textView = (TextView) findViewById(R.id.textView);
        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.doButtonProcess();
            }
        });
    }

    @Override
    public void showLoadingText() {
        textView.setText("Loading...");
    }

    @Override
    public void showResponseText(String responseText) {
        textView.setText(responseText);
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.clearSubscriptions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.resubscribeIfNeeded();
    }
}
