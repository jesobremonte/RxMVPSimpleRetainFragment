package com.example.rxtesting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import rx.Observable;

public class RetainedFragment extends Fragment implements MainContract.RetainerView {

    private Observable<String> observable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public Observable<String> getRetainedObservable() {
        return observable;
    }

    @Override
    public void retainObservable(Observable<String> observable) {
        this.observable = observable;
    }

    @Override
    public void clearObservable() {
        observable = null;
    }
}
