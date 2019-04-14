package com.yxy.rxjava;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.observables.GroupedObservable;

/**
 * Created by YXY
 * on 2019/4/14
 * 转化操作符
 */
public class ConversionTest extends ExampleUnitTest {
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
                        log("buffer: " + integers);
                    }
                });
        Observable.interval(1, TimeUnit.SECONDS)
                .buffer(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Long>>() {
                    @Override
                    public void call(List<Long> longs) {
                        log("bufferTime: " + longs);
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
                log(o);
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
                log(s);
            }
        });
    }

    /**
     * 3. groupBy
     */
    private void groupBy() {
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9)
                .groupBy(new Func1<Integer, Integer>() {
                    @Override
                    public Integer call(Integer integer) {
                        return integer % 2;
                    }
                }).subscribe(new Action1<GroupedObservable<Integer, Integer>>() {
            @Override
            public void call(final GroupedObservable<Integer, Integer> integerIntegerGroupedObservable) {

                integerIntegerGroupedObservable.count().subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        log("key" + integerIntegerGroupedObservable.getKey()
                                + " contains:" + integer + " numbers");
                    }
                });
            }
        });

        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9)
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
                if (integerStringGroupedObservable.getKey() == 0) {
                    integerStringGroupedObservable.subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            log(s);
                        }
                    });
                }
            }
        });
    }

    /**
     * 4. map
     */
    private void map() {
        Observable.just(1, 2, 3).map(new Func1<Integer, Integer>() {
            @Override
            public Integer call(Integer integer) {
                return integer * 10;
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                log("map: " + integer);
            }
        });
    }


    /**
     * 5. cast
     */
    class Animal {
        protected String name = "Animal";

        Animal() {
            log("create: " + name);
        }

        String getName() {
            return name;
        }
    }

    class Dog extends Animal {
        Dog() {
            name = getClass().getSimpleName();
            log("create: " + name);
        }
    }

    Animal getAnimal() {
        return new Dog();
    }

    private void cast() {
//        Observable.just(getAnimal())
//                .cast(Dog.class)
//                .subscribe(new Action1<Dog>() {
//                    @Override
//                    public void call(Dog dog) {
//                        log("cast: " + dog.name);
//                    }
//                });
        Observable.just(getAnimal())
                .subscribe(new Action1<Animal>() {
                    @Override
                    public void call(Animal animal) {
                        log("no cast: " + animal.getName());
                    }
                });
    }

    /**
     * 6. scan
     */
    private ArrayList<Integer> arrayList = new ArrayList<>();

    private void scan() {
        for (int i = 1; i <= 5; i++) {
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
                log("scan: " + integer);
            }
        });
    }

    /**
     * 7. window
     */
    private void window() {
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9)
                .window(3)
                .subscribe(new Action1<Observable<Integer>>() {
                    @Override
                    public void call(Observable<Integer> integerObservable) {
                        log(integerObservable.getClass().getName());
                        integerObservable.subscribe(new Action1<Integer>() {
                            @Override
                            public void call(Integer integer) {
                                log("window1: " + integer);
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
                        log(System.currentTimeMillis() / 1000);
                        longObservable.subscribe(new Action1<Long>() {
                            @Override
                            public void call(Long aLong) {
                                log("windowTime: " + aLong);
                            }
                        });
                    }
                });
    }

    @Override
    public void runTest() {
        //        buffer();
//        flatmap();
//        groupBy();
//        map();
//        cast();
//        scan();
        window();
    }
}
