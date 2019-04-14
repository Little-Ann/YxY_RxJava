package com.yxy.rxjava;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.Func3;
import rx.functions.FuncN;


/**
 * Created by YXY
 * on 2019/4/14
 */
public class CombineTest extends ExampleUnitTest{

    /**
     * combineLatest FIXME
     *
     * @param index
     * @return
     */
    private Observable<Long> createObserver(int index) {
        return Observable.interval(0, 500 * index, TimeUnit.MILLISECONDS);
    }

    private Observable<String> combineLatestObserver() {
        return Observable.combineLatest(createObserver(1), createObserver(2),
                new Func2<Long, Long, String>() {
                    @Override
                    public String call(Long aLong, Long aLong2) {
                        return ("left: " + aLong + "right: " + aLong2);
                    }
                });
    }

    private Observable<String> combineListObserver() {
        List<Observable<Long>> list = new ArrayList<Observable<Long>>();
        for (int i = 1; i < 3; i++) {
            list.add(createObserver(i));
        }
        return Observable.combineLatest(list, new FuncN<String>() {
            @Override
            public String call(Object... args) {
                String temp = "";
                for (Object i : args) {
                    temp = temp + ":" + i;
                }
                return temp;
            }
        });
    }

    private void combineLatest() {
        combineLatestObserver().subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                log("Combinelatest: " + s);
            }
        });
        combineListObserver().subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                log("Combinelist:" + s);
            }
        });

    }

    /**
     * join && groupJoin
     */
    private Observable<String> getLeftObservable() {
        return Observable.just("a", "b", "c");
    }

    private Observable<Long> getRightObservable() {
        return Observable.just(1l, 2l, 3l);
    }

    private Observable<String> joinObserver() {
        return getLeftObservable()
                .join(getRightObservable(),
                        new Func1<String, Observable<Long>>() {
                            @Override
                            public Observable<Long> call(String s) {
                                return Observable.timer(1000, TimeUnit.MILLISECONDS);
                            }
                        }, new Func1<Long, Observable<Long>>() {
                            @Override
                            public Observable<Long> call(Long aLong) {
                                return Observable.timer(1000, TimeUnit.MILLISECONDS);
                            }
                        }, new Func2<String, Long, String>() {
                            @Override
                            public String call(String s, Long aLong) {
                                return s + ":" + aLong;
                            }
                        });
    }

    private Observable<Observable<String>> groupJoinObserver() {
        return getLeftObservable().groupJoin(getRightObservable()
                , new Func1<String, Observable<Long>>() {
                    @Override
                    public Observable<Long> call(String s) {
                        return Observable.timer(1000, TimeUnit.MILLISECONDS);
                    }
                }, new Func1<Long, Observable<Long>>() {
                    @Override
                    public Observable<Long> call(Long aLong) {
                        return Observable.timer(1000, TimeUnit.MILLISECONDS);
                    }
                }, new Func2<String, Observable<Long>, Observable<String>>() {
                    @Override
                    public Observable<String> call(final String s, Observable<Long> longObservable) {
                        return longObservable.map(new Func1<Long, String>() {
                            @Override
                            public String call(Long aLong) {
                                return s + ":" + aLong;
                            }
                        });
                    }
                });
    }

    private void join_groupJoin() {
        joinObserver().subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                log("join:" + s);
            }
        });
        groupJoinObserver().first().subscribe(new Action1<Observable<String>>() {
            @Override
            public void call(Observable<String> stringObservable) {
                log("groupJoin: " + stringObservable);
            }
        });
    }

    /**
     * merge && mergeDelayError
     */
    private void merge_mergeDelayError() {
        Observable.merge(Observable.just(1, 2, 3), Observable.just(4, 5, 6))
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        log("Merge: " + integer);
                    }
                });

        Observable.mergeDelayError(Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 0; i < 5; i++) {
                    if (i == 3) {
                        subscriber.onError(new Throwable("error"));
                    }
                    subscriber.onNext(i);
                }
            }
        }), Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 0; i < 5; i++) {
                    subscriber.onNext(5 + i);
                }
                subscriber.onCompleted();
            }
        })).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                log("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                log("mergeDelayError:" + e);
            }

            @Override
            public void onNext(Integer integer) {
                log("mergeDelayNext: " + integer);
            }
        });
    }

    /**
     * startWith
     */
    private void startWith() {
        Observable.just(1, 2, 3).startWith(-1, 0)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        log("startWith: " + integer);
                    }
                });
    }

    /**
     * switch
     */
    private Observable<String> createObserver(final Long index) {
        return Observable.interval(1000, 1000, TimeUnit.MILLISECONDS)
                .take(5)
                .map(new Func1<Long, String>() {
                    @Override
                    public String call(Long aLong) {
                        return index + "-" + aLong;
                    }
                });
    }

    private Observable<String> switchObserver() {
        return Observable.switchOnNext(Observable
                .interval(0, 3000, TimeUnit.MILLISECONDS)
                .take(3)
                .map(new Func1<Long, Observable<String>>() {
                    @Override
                    public Observable<String> call(Long aLong) {
                        return createObserver(aLong);
                    }
                }));
    }

    private void switchOperator() {
        switchObserver().subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                log("switch: " + s);
            }
        });
    }

    /**
     * zip && zipWith  FIXME
     */
    private Observable<String> zipWithObserver(){
        return createObserverZip(2).zipWith(createObserverZip(3),
                new Func2<String, String, String>() {
                    @Override
                    public String call(String s, String s2) {
                        return s + "-" + s2;
                    }
                });
    }
    private Observable<String> zipWithIterableObserver(){
        return Observable.zip(createObserverZip(2),
                createObserverZip(3),
                createObserverZip(4),
                new Func3<String, String, String, String>() {
                    @Override
                    public String call(String aLong, String aLong2, String aLong3) {
                        return aLong + "-" + aLong2 + "_" + aLong3;
                     }
                });
    }
    private Observable<String> createObserverZip(final int index){
        return Observable.interval(100, TimeUnit.MILLISECONDS).take(index)
                .map(new Func1<Long, String>() {
                    @Override
                    public String call(Long aLong) {
                        return index + ":" + aLong;
                    }
                });
    }
    private void zip_zipWith(){
        zipWithObserver().subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                log("zipWith: "+ s);
            }
        });
        zipWithIterableObserver().subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                log("zip: " + s);
            }
        });
    }

    @Test
    public void runTest() {
//        combineLatest();
//        join_groupJoin();
//        merge_mergeDelayError();
//        startWith();
//        switchOperator();
        zip_zipWith();
    }
}
