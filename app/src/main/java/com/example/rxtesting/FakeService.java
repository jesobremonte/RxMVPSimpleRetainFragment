package com.example.rxtesting;


import java.util.Date;
import java.util.concurrent.TimeUnit;

import rx.Observable;

public class FakeService {

    public Observable<String> getResponse() {
        return Observable
                .just("Process response text! " + new Date().toString())
                .delay(5, TimeUnit.SECONDS);
    }

}
