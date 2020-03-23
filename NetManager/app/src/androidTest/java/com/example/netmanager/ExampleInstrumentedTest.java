package com.example.netmanager;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
//        // Context of the app under test.
//        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
//
//        assertEquals("com.example.netmanager", appContext.getPackageName());
        System.out.println("1231311");
    }

    @Test
    public void test(){
        String url="http://v.juhe.cn/historyWeather/citys?province_id=2&key=bb52107206585ab074f5e59a8c73875b";
        NeHttp.sendJsonRequest(url, null, ResponseClass.class, new IJsonDataListener<ResponseClass>() {
            @Override
            public void onSuccess(ResponseClass clazz) {
                Log.e("1111111111111111",clazz.toString());
                System.out.println(clazz.toString());
            }
        });
    }
}
