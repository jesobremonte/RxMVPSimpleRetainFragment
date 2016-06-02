package com.example.rxtesting;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MainPresenter implements MainContract.Presenter {

    private FakeService service = new FakeService();
    private MainContract.ActivityView activityView;
    private MainContract.RetainerView retainerView;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    public MainPresenter(MainContract.ActivityView activityView, MainContract.RetainerView retainerView) {
        this.activityView = activityView;
        this.retainerView = retainerView;
    }

    @Override
    public void doButtonProcess() {
        // Get the observable from service.
        Observable<String> observable = service.getResponse()
                .replay()
                .autoConnect();

        // Retain this long-running observable incase we need to unsubscribe and resubscibe.
        retainerView.retainObservable(observable);

        // Subscribe to the observable
        subscribeTo(observable);
    }

    @Override
    public void viewPaused() {
        if (compositeSubscription.hasSubscriptions()) {
            compositeSubscription.clear();
        }
    }

    @Override
    public void viewResumed() {
        // Check if the retainedFragment is holding on to an observable, resubscribe if it is.
        Observable<String> retainedObservable = retainerView.getRetainedObservable();

        if (retainedObservable != null) {
            subscribeTo(retainedObservable);
        }
    }

    private void subscribeTo(Observable<String> observable) {

        activityView.showLoadingText();

        // Subscribe to the observable.
        Subscription subscription = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String response) {
                        activityView.showResponseText(response);
                        retainerView.clearObservable();
                    }
                });

        // Add observable to collection so it can be unsubscribed when viewPaused.
        compositeSubscription.add(subscription);
    }
}
