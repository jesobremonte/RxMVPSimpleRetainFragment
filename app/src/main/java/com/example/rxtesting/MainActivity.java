package com.example.rxtesting;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import rx.Observable;

public class MainActivity extends AppCompatActivity implements MainContract.View {

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

        presenter = new MainPresenter(this);

        textView = (TextView) findViewById(R.id.textView);
        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.doProcess();
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
        retainedFragment.setObservable(null);
    }

    @Override
    public void retainObservable(Observable<String> observable) {
        retainedFragment.setObservable(observable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Check if the retainedFragment is holding on to an observable, resubscribe if it is.
        if (retainedFragment.getObservable() != null) {
            presenter.subscribeTo(retainedFragment.getObservable());
        }
    }
}
