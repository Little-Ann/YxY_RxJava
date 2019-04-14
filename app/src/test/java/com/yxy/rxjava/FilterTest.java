package com.yxy.rxjava;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by YXY
 * on 2019/4/7
 */
public class FilterTest extends ExampleUnitTest{
    /**
     * 1.debounce FIXME
     */
    private void debounce(){
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for(int i=0; i<10; i++){
                    if(!subscriber.isUnsubscribed()){
                        subscriber.onNext(i);
                    }
                    int sleep = 100;
                    if(i%3==0){
                        sleep = 300;
                    }
                    try {
                        Thread.sleep(sleep);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.computation())
                .throttleWithTimeout(200, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        log("throttleWithTimeout: " + integer);
                    }
                });

        Observable.interval(1000, TimeUnit.MILLISECONDS)
                .debounce(new Func1<Long, Observable<Long>>() {
                    @Override
                    public Observable<Long> call(Long aLong) {
                        return Observable.timer(aLong%2*1500, TimeUnit.MILLISECONDS);
                    }
                }).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                log("debounce: " + aLong);
            }
        });
    }

    /**
     * 2. distinct distinctUntilChanged
     */
    private void distinct() {
        Observable.just(1,2,3,4,5,4,3,2,1)
                .distinct()
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        log("distinct: "+ integer);
                    }
                });
        Observable.just(1,2,3,3,3,1,2)
                .distinctUntilChanged()
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        log("untilchanged: "+ integer);
                    }
                });
    }

    /**
     * 3. elementAt
     */
    private void elementAt(){
        Observable.just(2,4,4,2,5)
                .elementAt(2)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        log("elementAt: "+ integer);
                    }
                });
    }

    /**
     * 4.filter
     */
    private void filter(){
        Observable.just(0,1,2,3,4,5)
                .filter(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer<3;
                    }
                }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                log("Filter: "+ integer);
            }
        });
    }

    /**
     * 5. first   last BlockingObservable
     */
    private void first_last(){
        Observable.just(1,2,3,4,5)
                .first(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer > 3;
                    }
                }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                log("First: " + integer);
            }
        });
        int result = Observable.just(3,4,5,6,7)
                .toBlocking()
                .first(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer > 4;
                    }
                });
        log("blocking: "+ result);
    }

    /**
     * 6. skip take
     */
    private void skip_take(){
        Observable.just(1,2,3,4,5)
                .skip(2)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        log("skip: "+ integer);
                    }
                });
        Observable.just(2,3,4,5,6)
                .take(3)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        log("take: "+ integer);
                    }
                });
    }

    /**
     * 7. sample throttleFirst FIXME
     */
    private void sample_throttleFirst(){
        Observable.interval(0,200, TimeUnit.MILLISECONDS)
                .sample(1000, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        log("sample: "+ aLong);
                    }
                });

        Observable.interval(0,200, TimeUnit.MILLISECONDS)
                .throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        log("throttleFirst: "+ aLong);
                    }
                });

    }
    @Test
    public void runTest(){
//        debounce();
//        distinct();
//        elementAt();
//        filter();
//        first_last();
//        skip_take();
        sample_throttleFirst();
    }
}
