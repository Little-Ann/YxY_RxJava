package com.yxy.rxjava;

import android.support.annotation.NonNull;

import org.junit.Test;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
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
public class SchedulerTest {
    private void scheduler() {
        Observable
                .create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        System.out.println("start:" + Thread.currentThread().getName());
                        subscriber.onNext(1);
                        subscriber.onCompleted();
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .map(new Func1<Integer, Integer>() {
                    @Override
                    public Integer call(Integer integer) {
                        System.out.println(integer + ":" + Thread.currentThread().getName());
                        return integer + 1;
                    }
                })
                .subscribeOn(Schedulers.io())
                .map(new Func1<Integer, Integer>() {
                    @Override
                    public Integer call(Integer integer) {
                        System.out.println(integer + ":" + Thread.currentThread().getName());
                        return integer + 1;
                    }
                })
                .observeOn(Schedulers.computation())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        System.out.println("action: " + Thread.currentThread().getName());
                    }
                });
    }

    /**
     * Scheduler from  FIXME
     */
    class SimpleThreadFactory implements ThreadFactory {

        @Override
        public Thread newThread(@NonNull Runnable runnable) {
            return new Thread(runnable);
        }
    }
    private void from(){
        Executor executor = new ThreadPoolExecutor(
                2,
                2,
                2000L, TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<Runnable>(1000),
                new SimpleThreadFactory()
        );
        Observable.interval(0,1, TimeUnit.SECONDS)
                .take(5)
                .observeOn(Schedulers.from(executor))
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        System.out.println(aLong + "-" + Thread.currentThread().getName());
                    }
                });
    }

    @Test
    public void runTest() {
//        scheduler();
        from();
    }
}
