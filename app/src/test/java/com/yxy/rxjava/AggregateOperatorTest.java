package com.yxy.rxjava;


import java.util.ArrayList;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func2;

/**
 * Created by YXY
 * on 2019/5/2
 * 聚合操作符
 */
public class AggregateOperatorTest extends ExampleUnitTest {
    /**
     * concat
     */
    private void concatTest(){
        Observable<Integer> observable1 = Observable.just(1,2,3);
        Observable<Integer> observable2 = Observable.just(4,5,6);
        Observable<Integer> observable3 = Observable.just(7,8,9);
        Observable.concat(observable1, observable3, observable2)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        log("concat: " + integer);
                    }
                });
    }

    /**
     * count
     */
    private void countTest(){
        Observable.just(1,2,3).count()
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        log("count: " + integer);
                    }
                });
    }

    /**
     * reduce
     */
    private void reduceTest(){
        ArrayList<Integer> list = new ArrayList<>();
        for(int i=0; i<10;i++){
            list.add(2);
        }
        Observable.from(list).reduce(new Func2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer integer, Integer integer2) {
                return integer * integer2;
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                log("reduce: " + integer);
            }
        });
    }

    /**
     * collect
     */
    private void collectTest() {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        Observable.from(list).collect(new Func0<ArrayList<Integer>>() {
            @Override
            public ArrayList<Integer> call() {
                return new ArrayList<>();
            }
        }, new Action2<ArrayList<Integer>, Integer>() {
            @Override
            public void call(ArrayList<Integer> integers, Integer integer) {
                integers.add(integer);
            }
        }).subscribe(new Action1<ArrayList<Integer>>() {
            @Override
            public void call(ArrayList<Integer> integers) {
                log("collect: " + integers);
            }
        });
    }
    @Override
    public void runTest() {
//        concatTest();
//        countTest();
//        reduceTest();
        collectTest();
    }
}
