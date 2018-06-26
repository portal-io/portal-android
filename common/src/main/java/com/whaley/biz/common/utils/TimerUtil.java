package com.whaley.biz.common.utils;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Author: qxw
 * Date:2017/8/5
 * Introduction:
 */

public class TimerUtil {


    private static Disposable disposable;

    public static void timerSeconds(long seconds, final TimerNext next) {
        timerSeconds(seconds, 1, next);
    }

    /**
     * 总共seconds秒
     * interval秒后执行next操作
     *
     * @param seconds
     * @param interval
     * @param next
     */
    public static void timerSeconds(long seconds, long interval, final TimerNext next) {
        cancel();
        final long totalTime = seconds;
        disposable = Observable.interval(0, interval, TimeUnit.SECONDS)
                .take(totalTime / interval + 1)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long aLong) throws Exception {
                        return totalTime - aLong;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Long>() {

                    @Override
                    public void onNext(@NonNull Long number) {
                        if (next != null) {
                            next.doNext(number);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        cancel();
                    }

                    @Override
                    public void onComplete() {
                        cancel();
                    }
                });
    }


    public static void timerMilliseconds(long milliseconds, final TimerNext next) {
        timerMilliseconds(milliseconds, 100, next);
    }

    /**
     * 总共milliseconds毫秒
     * interval毫秒后执行next操作
     *
     * @param milliseconds
     * @param interval
     * @param next
     */
    public static void timerMilliseconds(long milliseconds, final long interval, final TimerNext next) {
        cancel();
        final long totalTime = milliseconds;
        disposable = Observable.interval(0, interval, TimeUnit.MILLISECONDS)
                .take(milliseconds / interval + 1)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long aLong) throws Exception {
                        return totalTime - aLong * interval;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Long>() {

                    @Override
                    public void onNext(@NonNull Long number) {
                        if (next != null) {
                            next.doNext(number);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        cancel();
                    }

                    @Override
                    public void onComplete() {
                        cancel();
                    }
                });
    }


    /**
     * 取消订阅
     */
    public static void cancel() {
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
    }

    public interface TimerNext {
        void doNext(long number);

    }
}

