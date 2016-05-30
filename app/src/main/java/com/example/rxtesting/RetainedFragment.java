package com.example.rxtesting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import rx.Observable;

public class RetainedFragment extends Fragment {

    private Observable<String> observable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public Observable<String> getObservable() {
        return observable;
    }

    public void setObservable(Observable<String> observable) {
        this.observable = observable;
    }
}
