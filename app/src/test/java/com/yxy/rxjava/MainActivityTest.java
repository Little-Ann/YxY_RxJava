package com.yxy.rxjava;

import android.util.Log;

import org.junit.Test;

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

import static org.junit.Assert.*;

/**
 * Created by YXY
 * on 2019/4/6
 */
public class MainActivityTest {
    private static final String TAG = "YXY";

/**********************创建Observable操作符**********************/
    /**
     * 1 .range
     */
    private void Range() {
        Observable.range(0, 4)
                .subscribe(new Action1<Integer>() {

                    @Override
                    public void call(Integer integer) {
                        System.out.println(integer);
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
                    System.out.println("defer: " + aLong);
                }
            });
            justObservable.subscribe(new Action1<Long>() {
                @Override
                public void call(Long aLong) {
                    System.out.println("just: " + aLong);
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
                System.out.println("FromArray: " + integer);
            }
        });
        FromIterable().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                System.out.println("FromIterable: " + integer);
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
                System.out.println("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError: " + e.getMessage());
            }

            @Override
            public void onNext(Long aLong) {
                System.out.println("onNext:" + aLong);
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
                        System.out.println("repeat: " + integer);
                    }
                });

        Observable.timer(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        System.out.println("timer: " + aLong);
                    }
                });
    }

    /**********************转化Observable操作符**********************/
    /**
     * 1.buffer
     */
    private void buffer() {
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9)
                .buffer(2, 3)
                .subscribe(new Action1<List<Integer>>() {
                    @Override
                    public void call(List<Integer> integers) {
                        System.out.println("buffer: " + integers);
                    }
                });
        Observable.interval(1, TimeUnit.SECONDS)
                .buffer(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Long>>() {
                    @Override
                    public void call(List<Long> longs) {
                        System.out.println("bufferTime: " + longs);
                    }
                });
    }

    /**
     * 2. flatmap
     */
    private void flatmap() {
        Observable.just(1, 2, 3)
                .flatMap(new Func1<Integer, Observable<String>>() {
                    @Override
                    public Observable<String> call(Integer integer) {
                        return Observable.just("flat map: " + integer);
                    }
                }).subscribe(new Action1<String>() {
            @Override
            public void call(String o) {
                System.out.println(o);
            }
        });
        Observable.just(1, 2, 3)
                .flatMapIterable(new Func1<Integer, Iterable<String>>() {
                    @Override
                    public Iterable<String> call(Integer integer) {
                        ArrayList<String> arrayList = new ArrayList<>();
                        for (int i = 0; i < 3; i++) {
                            arrayList.add("flatmapIterable: " + integer);
                        }
                        return arrayList;
                    }
                }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println(s);
            }
        });
    }
    /**
     * 3. groupBy
     */
    private void groupBy() {
        Observable.just(1,2,3,4,5,6,7,8,9)
                .groupBy(new Func1<Integer, Integer>() {
                    @Override
                    public Integer call(Integer integer) {
                        return integer%2;
                    }
                }).subscribe(new Action1<GroupedObservable<Integer, Integer>>() {
            @Override
            public void call(final GroupedObservable<Integer, Integer> integerIntegerGroupedObservable) {

                integerIntegerGroupedObservable.count().subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        System.out.println("key"+ integerIntegerGroupedObservable.getKey()
                                +" contains:" + integer + " numbers");
                    }
                });
            }
        });

        Observable.just(1,2,3,4,5,6,7,8,9)
                .groupBy(new Func1<Integer, Integer>() {
                    @Override
                    public Integer call(Integer integer) {
                        return integer % 2;
                    }
                }, new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        return "groupByKeyValue: " + integer;
                    }
                }).subscribe(new Action1<GroupedObservable<Integer, String>>() {
            @Override
            public void call(GroupedObservable<Integer, String> integerStringGroupedObservable) {
                if(integerStringGroupedObservable.getKey() == 0) {
                    integerStringGroupedObservable.subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            System.out.println(s);
                        }
                    });
                }
            }
        });
    }
    /**
     * 4. map
     */
    private void map(){
        Observable.just(1,2,3).map(new Func1<Integer, Integer>() {
            @Override
            public Integer call(Integer integer) {
                return integer*10;
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                System.out.println("map: "+ integer);
            }
        });
    }

    /**
     * 5. cast
     */
    class Animal{
        protected String name = "Animal";
        Animal(){
            System.out.println("create: " + name);
        }
        String getName(){
            return name;
        }
    }
    class Dog extends Animal{
        Dog(){
            name = getClass().getSimpleName();
            System.out.println("create: " + name);
        }
    }
    Animal getAnimal(){
        return new Dog();
    }
    private void cast(){
//        Observable.just(getAnimal())
//                .cast(Dog.class)
//                .subscribe(new Action1<Dog>() {
//                    @Override
//                    public void call(Dog dog) {
//                        System.out.println("cast: " + dog.name);
//                    }
//                });
        Observable.just(getAnimal())
                .subscribe(new Action1<Animal>() {
                    @Override
                    public void call(Animal animal) {
                        System.out.println("no cast: " + animal.getName());
                    }
                });
    }

    /**
     * 6. scan
     */
    private ArrayList<Integer> arrayList = new ArrayList<>();
    private void scan(){
       for(int i= 1; i<=5; i++){
           arrayList.add(i);
       }
       Observable.from(arrayList)
               .scan(new Func2<Integer, Integer, Integer>() {
                   @Override
                   public Integer call(Integer integer, Integer integer2) {
                       return integer * integer2;
                   }
               }).subscribe(new Action1<Integer>() {
           @Override
           public void call(Integer integer) {
               System.out.println("scan: "+ integer);
           }
       });
    }

    /**
     * 7. window
     */
    private void window(){
        Observable.just(1,2,3,4,5,6,7,8,9)
                .window(3)
                .subscribe(new Action1<Observable<Integer>>() {
                    @Override
                    public void call(Observable<Integer> integerObservable) {
                        System.out.println(integerObservable.getClass().getName());
                        integerObservable.subscribe(new Action1<Integer>() {
                            @Override
                            public void call(Integer integer) {
                                System.out.println("window1: "+ integer);
                            }
                        });
                    }
                });

        // FIXME
        Observable.interval(1000, TimeUnit.MILLISECONDS)
                .window(3000, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Observable<Long>>() {
                    @Override
                    public void call(Observable<Long> longObservable) {
                        System.out.println(System.currentTimeMillis()/1000);
                        longObservable.subscribe(new Action1<Long>() {
                            @Override
                            public void call(Long aLong) {
                                System.out.println("windowTime: "+ aLong);
                            }
                        });
                    }
                });
    }



    @Test
    public void onCreate() {
//        Range();
//        Just_Defer();
//        From();
//        interval();
//        Repeat_Timer();
//        buffer();
//        flatmap();
//        groupBy();
//        map();
//        cast();
//        scan();
        window();
    }


}