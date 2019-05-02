package com.yxy.rxjava;


import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by YXY
 * on 2019/4/14
 * 错误处理操作符
 */
public class ErrorOperatorTest extends ExampleUnitTest {
    /**
     * onErrorReturn
     */
    private Observable<String> createObserver() {
        return Observable.create(new Observable.OnSubscribe<String>(){

            @Override
            public void call(Subscriber<? super String> subscriber) {
                for(int i= 1; i<=6; i++){
                    if(i<3){
                        subscriber.onNext("onNext:" + i);
                    } else {
                        subscriber.onError(new Throwable("Throw error"));
                    }
                }
            }
        });
    }
    private Observable<String> onErrorReturnObserver(){
        return createObserver().onErrorReturn(new Func1<Throwable, String>() {
            @Override
            public String call(Throwable throwable) {
                return "onErrorReturn";
            }
        });
    }
    private void onErrorReturnTest(){
        onErrorReturnObserver().subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                log("onErrorReturn-onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                log("onErrorReturn-onError:" + e.getMessage());
            }

            @Override
            public void onNext(String s) {
                log("onErrorReturn-onNext:" + s);
            }
        });
    }

    /**
     * onErrorResumeNext
     */
    private Observable<String> onErrorResumeNextObserver(){
        return createObserver().onErrorResumeNext(Observable.just("7", "8", "9"));
    }
    private void onErrorResumeNextTest(){
        onErrorResumeNextObserver().subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                log("onErrorResumeNext-onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                log("onErrorResumeNext-onError:" + e.getMessage());
            }

            @Override
            public void onNext(String s) {
                log("onErrorResumeNext-onNext:" + s);
            }
        });
    }

    /**
     * onExceptionResumeNext
     */
    private Observable<String> createObserver(final Boolean createException) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                for(int i=1; i<=6; i++){
                    if(i<3){
                        subscriber.onNext("onNext:" + i);
                    } else if(createException){
                        subscriber.onError(new Exception("Exception"));
                    } else {
                        subscriber.onError(new Throwable("Throw error"));
                    }
                }
            }
        });
    }
    private Observable<String> onExceptionResumeObserver(boolean isException) {
        return createObserver(isException).onExceptionResumeNext(Observable.just("7","8","9"));
    }
    private void onExceptionResumeNextTest(){
        onExceptionResumeObserver(true).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                log("onException-true-onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                log("onException-true-onError:" + e.getMessage());
            }

            @Override
            public void onNext(String s) {
                log("onException-true-onNext:" + s);
            }
        });

        onExceptionResumeObserver(false).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                log("onException-false-onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                log("onException-false-onError:" + e.getMessage());
            }

            @Override
            public void onNext(String s) {
                log("onException-false-onNext:" + s);
            }
        });
    }

    /**
     * retry
     */
    private Observable<Integer> createRetryObserver(){
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                log("subscribe");
                for(int i=0; i<3; i++){
                    if(i==2){
                        subscriber.onError(new Exception("Exception"));
                    } else {
                        subscriber.onNext(i);
                    }
                }
            }
        });
    }
    private void retryTest(){
        createRetryObserver().retry(2).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                log("retry-onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                log("retry-onError:"+ e.getMessage());
            }

            @Override
            public void onNext(Integer integer) {
                log("retry-onNext:" + integer);
            }
        });
    }
    @Override
    public void runTest() {
//        onErrorReturnTest();
//        onErrorResumeNextTest();
//        onExceptionResumeNextTest();
        retryTest();
    }
}
