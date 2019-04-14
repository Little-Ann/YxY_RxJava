package com.yxy.rxjava;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.observables.GroupedObservable;

/**
 * Created by YXY
 * on 2019/4/6
 * 创建操作符测试
 */
public class CreateOperatorTest extends ExampleUnitTest {

/**********************创建Observable操作符**********************/
    /**
     * 1 .range
     */
    private void Range() {
        Observable.range(0, 4)
                .subscribe(new Action1<Integer>() {

                    @Override
                    public void call(Integer integer) {
                        log(integer);
                    }
                });

    }

    /**
     * 2.defer just
     **/
    Observable<Long> deferObservable = getDefer();
    Observable<Long> justObservable = getJust();

    private Observable<Long> getJust() {
        return Observable.just(System.currentTimeMillis());
    }

    private Observable<Long> getDefer() {
        return Observable.defer(new Func0<Observable<Long>>() {
            @Override
            public Observable<Long> call() {
                return getJust();
            }
        });
    }

    private void Just_Defer() {
        for (int i = 0; i < 3; i++) {
            deferObservable.subscribe(new Action1<Long>() {
                @Override
                public void call(Long aLong) {
                    log("defer: " + aLong);
                }
            });
            justObservable.subscribe(new Action1<Long>() {
                @Override
                public void call(Long aLong) {
                    log("just: " + aLong);
                }
            });

        }
    }

    /**
     * 3. from
     **/
    Integer[] arrays = {0, 1, 2, 3, 4, 5};
    List<Integer> list = new ArrayList<>();

    private Observable<Integer> FromArray() {
        return Observable.from(arrays);
    }

    private Observable<Integer> FromIterable() {
        for (int i = 0; i <= 5; i++) {
            list.add(i);
        }
        return Observable.from(list);
    }

    private Observable<Integer[]> JustArray() {
        return Observable.just(arrays);
    }

    private void From() {
        FromArray().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                log("FromArray: " + integer);
            }
        });
        FromIterable().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                log("FromIterable: " + integer);
            }
        });
        JustArray().subscribe(new Action1<Integer[]>() {
            @Override
            public void call(Integer[] integers) {

            }
        });


    }

    /**
     * 4. interval
     **/
    private void interval() {
        Subscriber<Long> subscriber = new Subscriber<Long>() {
            @Override
            public void onCompleted() {
                log("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                log("onError: " + e.getMessage());
            }

            @Override
            public void onNext(Long aLong) {
                log("onNext:" + aLong);
            }
        };

        Observable.interval(0, 10, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        subscriber.unsubscribe();
    }

    /**
     * 5. repeat & timer
     **/
    private void Repeat_Timer() {
        Observable.just(1, 2, 3).repeat(3)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        log("repeat: " + integer);
                    }
                });

        Observable.timer(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        log("timer: " + aLong);
                    }
                });
    }




    @Override
    public void runTest() {
//        Range();
//        Just_Defer();
//        From();
//        interval();
        Repeat_Timer();


    }


}