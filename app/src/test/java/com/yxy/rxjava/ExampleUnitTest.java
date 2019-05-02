package com.yxy.rxjava;

import org.junit.Test;

import java.util.Observable;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public abstract class ExampleUnitTest {

    @Test
    public abstract void runTest();
    public void log(String ss){
        System.out.println(ss);
    }
    public void log(Long l){
        log(String.valueOf(l));
    }
    public void log(Integer i){
        log(String.valueOf(i));
    }
    public long getCurrentTime(){
        return System.currentTimeMillis()/1000;
    }
}