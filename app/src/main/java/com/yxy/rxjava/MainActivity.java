package com.yxy.rxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import rx.Observable;
import rx.functions.Action1;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "YXY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Range();
    }
    private void Range(){
        Observable.range(0,4)
                .subscribe(new Action1<Integer>(){

                    @Override
                    public void call(Integer integer) {
                        Log.i(TAG, "call: range = " + integer);
                    }
                });

    }
}
