package com.yxy.rxjava;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;

/**
 * Created by YXY
 * on 2019/5/2
 * 与Connectable Observable相关的操作符
 */
public class ConnectableOperatorTest extends ExampleUnitTest {
    /**
     * publish && connect
     */
    private void publishConnectTest() {
        Observable<Long> obser = Observable.interval(1, TimeUnit.SECONDS, Schedulers.trampoline());
        final ConnectableObservable<Long> observable = obser.publish();
        final Action1 action2 = new Action1() {
            @Override
            public void call(Object o) {
                log("action2: " + o);
            }
        };
        Action1 action1 = new Action1() {
            @Override
            public void call(Object o) {
                log("action1: " + o);
                if ((long) o == 3)
                    observable.take(6).subscribe(action2);
            }
        };
        observable.take(6).subscribe(action1);
        observable.connect();
    }

    /**
     * refCount
     */
    private void refCountTest() {
        Observable<Long> obser = Observable.interval(1, TimeUnit.SECONDS, Schedulers.trampoline());
        ConnectableObservable<Long> observable = obser.publish();
        observable.refCount().take(5).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                log("refCount: " + aLong);
            }
        });
    }

    /**
     * replay
     */
    private ConnectableObservable<Long> replayCountObserver() {
        Observable<Long> obser = Observable.interval(1, TimeUnit.SECONDS, Schedulers.trampoline());
        return obser.replay(2);
    }

    private ConnectableObservable<Long> replayTimeObserver() {
        Observable<Long> obser = Observable.interval(1, TimeUnit.SECONDS, Schedulers.trampoline());
        return obser.replay(3, TimeUnit.SECONDS);
    }

    private void replayTest() {
        final ConnectableObservable<Long> observer = replayTimeObserver();
        final Action1 action2 = new Action1() {
            @Override
            public void call(Object o) {
                log("action2: " + o);
            }
        };
        Action1 action1 = new Action1() {
            @Override
            public void call(Object o) {
                log("action1: " + o);
                if ((long) o == 3)
                    observer.take(6).subscribe(action2);
            }
        };
        observer.take(10).subscribe(action1);
        log("timecount");
        observer.connect();
    }

    @Override
    public void runTest() {
//        publishConnectTest();
//        refCountTest();
        replayTest();
    }
}
