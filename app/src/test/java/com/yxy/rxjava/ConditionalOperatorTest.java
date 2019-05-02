package com.yxy.rxjava;


import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by YXY
 * on 2019/5/2
 * 条件操作符
 */
public class ConditionalOperatorTest extends ExampleUnitTest {
    /**
     * all
     */
    private Observable<Boolean> allObserver() {
        Observable<Integer> just = Observable.just(1, 2, 3, 4, 5);
        return just.all(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer integer) {
                return integer < 6;
            }
        });
    }

    private Observable<Boolean> notAllObserver() {
        Observable<Integer> just = Observable.just(1, 2, 3, 4, 5, 6);
        return just.all(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer integer) {
                return integer < 6;
            }
        });
    }

    private void allTest() {
        allObserver().subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                log("all:" + aBoolean);
            }
        });
        notAllObserver().subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                log("not all: " + aBoolean);
            }
        });
    }

    /**
     * amb
     */
    private Observable<Integer> ambObserver() {
        Observable<Integer> delay3 = Observable.just(7, 8, 9).delay(3000, TimeUnit.MILLISECONDS, Schedulers.trampoline());
        Observable<Integer> delay2 = Observable.just(4, 5, 6).delay(2000, TimeUnit.MILLISECONDS, Schedulers.trampoline());
        Observable<Integer> delay1 = Observable.just(1, 2, 3).delay(1000, TimeUnit.MILLISECONDS, Schedulers.trampoline());
        return Observable.amb(delay1, delay2, delay3);
    }

    private void ambTest() {
        ambObserver().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                log("amb: " + integer);
            }
        });
    }

    /**
     * contains
     */
    private void containsTest() {
        Observable.just(1, 2, 3).contains(3).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                log("contains: " + aBoolean);
            }
        });
        Observable.just(1, 2, 3).contains(4).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                log("not contains : " + aBoolean);
            }
        });
    }

    /**
     * isEmpty
     */
    private void isEmptyTest() {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onCompleted();
            }
        }).isEmpty()
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        log("isEmpty: " + aBoolean);
                    }
                });
    }

    /**
     * defaultIfEmpty
     */
    private void defaultIfEmptyTest() {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onCompleted();
            }
        }).defaultIfEmpty(10).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                log("empty: " + integer);
            }
        });

        Observable.just(1).defaultIfEmpty(10).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                log("notEmpty :" + integer);
            }
        });
    }

    /**
     * sequenceEqual
     */
    private void sequenceEqualTest() {
        Observable.sequenceEqual(Observable.just(1, 2, 3), Observable.just(1, 2, 3))
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        log("equal: " + aBoolean);
                    }
                });
        Observable.sequenceEqual(Observable.just(1, 2, 3), Observable.just(4, 5, 6))
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        log("not equal :" + aBoolean);
                    }
                });
    }

    /**
     * skipUntil && skipWhile
     */
    private void skipUntil_SkipWhile() {
        Observable.interval(1, TimeUnit.SECONDS)
                .skipUntil(Observable.timer(3, TimeUnit.SECONDS))
                .take(5)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        log("skipUntil: " + aLong);
                    }
                });

        Observable.interval(1, TimeUnit.SECONDS, Schedulers.trampoline()).skipWhile(new Func1<Long, Boolean>() {
            @Override
            public Boolean call(Long aLong) {
                return aLong < 5;
            }
        }).take(5).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                log("skipWhile: " + aLong);
            }
        });
    }

    /**
     * takeUntil && takeWhile
     */
    private void takeUntil_TakeWhile() {
        Observable.interval(1, TimeUnit.SECONDS, Schedulers.trampoline())
                .takeUntil(Observable.timer(3, TimeUnit.SECONDS))
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        log("takeUntil: " + aLong);
                    }
                });
        Observable.interval(1, TimeUnit.SECONDS, Schedulers.trampoline())
                .takeWhile(new Func1<Long, Boolean>() {
                    @Override
                    public Boolean call(Long aLong) {
                        return aLong < 5;
                    }
                }).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                log("takeWhile: " + aLong);
            }
        });
    }

    @Override
    public void runTest() {
//        allTest();
//        ambTest();
//        containsTest();
//        isEmptyTest();
//        defaultIfEmptyTest();
//        sequenceEqualTest();
//        skipUntil_SkipWhile();
        takeUntil_TakeWhile();
    }
}
