package com.yxy.rxjava;


import java.util.concurrent.TimeUnit;

import rx.Notification;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.schedulers.TimeInterval;
import rx.schedulers.Timestamped;

/**
 * Created by YXY
 * on 2019/5/1
 * 辅助操作符
 */
public class AssitOperatorTest extends ExampleUnitTest {
    /**
     * delay
     */
    private Observable<Long> createObserver(final int index){
        return Observable.create(new Observable.OnSubscribe<Long>() {
            @Override
            public void call(Subscriber<? super Long> subscriber) {
                log("subscribe:" + getCurrentTime());
                for(int i=1; i<=index; i++){
                    subscriber.onNext(getCurrentTime());
                }
            }
        }).subscribeOn(Schedulers.newThread());
    }
    private Observable<Long> delayObserver() {
        return createObserver(2).delay(2000, TimeUnit.MILLISECONDS, Schedulers.trampoline());
    }
    private Observable<Long> delaySubscriptionObserver() {
        return createObserver(2).delaySubscription(2000, TimeUnit.MILLISECONDS, Schedulers.trampoline());
    }
    private void delayTest(){
        log("start subscribe: "+ getCurrentTime());
        delayObserver().subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                log("delay:" + (getCurrentTime()-aLong));
            }
        });
        log("start subscribe: "+ getCurrentTime());
        delaySubscriptionObserver().subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                log("delaySubscription:" + (getCurrentTime()+aLong));
            }
        });
    }

    /**
     * do操作符
     */
    private Observable<Integer> createObserver(){
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for(int i=1;i<=5;i++){
                    if(i<=3){
                        subscriber.onNext(i);
                    } else {
                        subscriber.onError(new Throwable("num > 3"));
                    }
                }
            }
        });
    }
    private Observable<Integer> doOnEachObserver(){
        return Observable.just(1,2,3)
                .doOnEach(new Action1<Notification<? super Integer>>() {
                    @Override
                    public void call(Notification<? super Integer> notification) {
                        log("doOnEach send " + notification.getValue() + " type:" + notification.getKind());
                    }
                }).doOnNext(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        log("doOnNext send " + integer);
                    }
                }).doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        log("on Subscribe");
                    }
                }).doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        log("on unSubscribe");
                    }
                }).doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        log("onCompleted");
                    }
                });

    }
    private Observable<Integer> doOnErrorObserver(){
        return createObserver()
                .doOnEach(new Action1<Notification<? super Integer>>() {
                    @Override
                    public void call(Notification<? super Integer> notification) {
                        log("doOnErrorEach send " + notification.getValue() + " type:" + notification.getKind());
                    }
                }).doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        log("onError: " + throwable.getMessage());
                    }
                }).doOnTerminate(new Action0() {
                    @Override
                    public void call() {
                        log("OnTerminate");
                    }
                }).doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        log("doAfterTerminate");
                    }
                });
    }
    private void doTest(){
        doOnEachObserver().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                log("do: " + integer);
            }
        });
        doOnErrorObserver().subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                log("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                log("subscriber onError: " + e.getMessage());
            }

            @Override
            public void onNext(Integer integer) {
                log("subscriber onNext: " + integer);
            }
        });
    }

    /**
     * materizlize && dematerialize
     */
    private Observable<Notification<Integer>> materizlizeObserver(){
        return Observable.just(1,2,3).materialize();
    }
    private Observable<Integer> deMaterializeObserver(){
        return materizlizeObserver().dematerialize();
    }
    private void materizlize_dematerialize(){
        materizlizeObserver().subscribe(new Action1<Notification<Integer>>() {
            @Override
            public void call(Notification<Integer> integerNotification) {
                log("materizlize: "+ integerNotification.getValue() + " type" + integerNotification.getKind());
            }
        });
        deMaterializeObserver().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                log("deMeterizlize: "+ integer);
            }
        });
    }

    /**
     * subscribeOn && observeOn
     */
    private Observable<Integer> createObserver1(){
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                log("on subscribe: " + Thread.currentThread().getName());
                subscriber.onNext(1);
                subscriber.onCompleted();
            }
        });
    }
    private Observable<Integer> observeOnObserver(){
        return createObserver1()
                .observeOn(Schedulers.trampoline())
                .subscribeOn(Schedulers.newThread());
    }
    private Observable<Integer> subscribeOnObserver() {
        return createObserver1()
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.immediate());
    }
    private void subscribeOn_observeOn(){
        observeOnObserver().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                log("ObserveOn: "+ Thread.currentThread().getName());
            }
        });
        subscribeOnObserver().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                log("subscribeOn: "+ Thread.currentThread().getName());
            }
        });
    }

    /**
     * timeInterval && timeStamp
     */
    private void timeInterval_TimeStamp(){
        Observable.interval(1, TimeUnit.SECONDS, Schedulers.trampoline()).take(3).timeInterval()
                .subscribe(new Action1<TimeInterval<Long>>() {
                    @Override
                    public void call(TimeInterval<Long> longTimeInterval) {
                        log("timeInterval: "+ longTimeInterval.getValue() + "-" + longTimeInterval.getIntervalInMilliseconds());
                    }
                });
        Observable.interval(1, TimeUnit.SECONDS, Schedulers.trampoline()).take(3).timestamp()
                .subscribe(new Action1<Timestamped<Long>>() {
                    @Override
                    public void call(Timestamped<Long> longTimestamped) {
                        log("timeStamp: "+ longTimestamped.getValue() + "-" + longTimestamped.getTimestampMillis());
                    }
                });
    }

    /**
     * timeout
     */
    private Observable<Integer> createTimeoutObserver(){
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for(int i=0; i<=3; i++){
                    try{
                        Thread.sleep(i*100);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    subscriber.onNext(i);
                }
                subscriber.onCompleted();
            }
        });
    }
    private void timeoutTest(){
        createTimeoutObserver().timeout(200, TimeUnit.MILLISECONDS)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        log(e.toString());
                    }

                    @Override
                    public void onNext(Integer integer) {
                        log("timeout: "+ integer);
                    }
                });

        createTimeoutObserver().timeout(200, TimeUnit.MILLISECONDS, Observable.just(5,6))
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        log(integer);
                    }
                });
    }

    /**
     * using
     */
    private class Animal{
        Subscriber subscriber = new Subscriber() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {
                log("animal eat");
            }
        };
        public Animal(){
            log("create animal");
            Observable.interval(1000, TimeUnit.MILLISECONDS).subscribe(subscriber);
        }
        public void release(){
            log("animal released");
            subscriber.unsubscribe();
        }
    }
    private Observable<Long> usingObservable(){
        return Observable.using(new Func0<Animal>() {
            @Override
            public Animal call() {
                return new Animal();
            }
        }, new Func1<Animal, Observable<? extends Long>>() {
            @Override
            public Observable<? extends Long> call(Animal animal) {
                return Observable.timer(5000, TimeUnit.MILLISECONDS, Schedulers.trampoline());
            }
        }, new Action1<Animal>() {
            @Override
            public void call(Animal animal) {
                animal.release();
            }
        });
    }
    private void usingTest(){
        Subscriber subscriber = new Subscriber() {
            @Override
            public void onCompleted() {
                log("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                log("onError" + e.toString());
            }

            @Override
            public void onNext(Object o) {
                log("onNext: " + o);
            }
        };
        usingObservable().subscribe(subscriber);
    }
    @Override
    public void runTest() {
//        delayTest();
//        doTest();
//        materizlize_dematerialize();
//        subscribeOn_observeOn();
//        timeInterval_TimeStamp();
//        timeoutTest();
        usingTest();
    }
}
