package com.yxy.rxjava;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by YXY
 * on 2019/4/6
 */
public class MainActivityTest {

    @Test
    public void onCreate() {
        buffer();
    }
    /**********************转化Observable操作符**********************/
    private void buffer(){
        Observable.just(1,2,3,4,5,6,7,8,9)
                .buffer(2,3)
                .subscribe(new Action1<List<Integer>>() {
                    @Override
                    public void call(List<Integer> integers) {
                        System.out.println("buffer: "+ integers);
                    }
                });
        Observable.interval(1, TimeUnit.SECONDS)
                .buffer(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Long>>() {
                    @Override
                    public void call(List<Long> longs) {
                        System.out.println("bufferTime: "+ longs);
                    }
                });
    }
}