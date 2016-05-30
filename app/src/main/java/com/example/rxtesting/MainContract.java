package com.example.rxtesting;

import rx.Observable;

public interface MainContract {

    interface View {
        void showLoadingText();
        void showResponseText(String responseText);
        void retainObservable(Observable<String> observable);
    }

    interface Presenter {
        void doProcess();
        void onPause();
        void subscribeTo(Observable<String> observable);
    }
}
