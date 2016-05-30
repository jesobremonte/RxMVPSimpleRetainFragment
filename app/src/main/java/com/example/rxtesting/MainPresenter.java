package com.example.rxtesting;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MainPresenter implements MainContract.Presenter {

    private FakeService service = new FakeService();
    private MainContract.View view;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    public MainPresenter(MainContract.View view) {
        this.view = view;
    }

    @Override
    public void doProcess() {

        // Get the observable from service.
        Observable<String> observable = service.getResponse()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache();

        // Tell the view(which tells the retainFragment) to keep the observable, before subscribing.
        view.retainObservable(observable);

        subscribeTo(observable);
    }

    @Override
    public void onPause() {
        if (compositeSubscription.hasSubscriptions()) {
            compositeSubscription.clear();
        }
    }

    @Override
    public void subscribeTo(Observable<String> observable) {

        view.showLoadingText();

        // Subscribe to the observable.
        Subscription subscription = observable.subscribe(new Action1<String>() {
            @Override
            public void call(String response) {
                view.showResponseText(response);
            }
        });

        // Add observable to collection so it can be unsubscribed onPause.
        compositeSubscription.add(subscription);
    }
}
