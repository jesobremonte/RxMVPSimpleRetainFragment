package com.example.rxtesting;

import rx.Observable;

public interface MainContract {

    interface RetainerView {
        Observable<String> getRetainedObservable();
        void retainObservable(Observable<String> observable);
        void clearObservable();
    }

    interface ActivityView {
        void showLoadingText();
        void showResponseText(String responseText);
    }

    interface Presenter {
        void doButtonProcess();
        void viewPaused();
        void viewResumed();
    }
}
