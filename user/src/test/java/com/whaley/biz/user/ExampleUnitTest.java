package com.whaley.biz.user;

import android.util.Log;

import com.whaley.biz.common.utils.TimerUtil;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void timer() throws Exception {
        TimerUtil.timerSeconds(60, new TimerUtil.TimerNext() {
            @Override
            public void doNext(long number) {
                Log.e("XXXXX","number="+number);
            }
        });
    }


}