package com.milog.myannotationtest;

import android.app.Activity;
import android.os.Bundle;

import com.milog.MyAnnotation1;
import com.milog.annotation.MiloConfig2;

@MyAnnotation1
public class MainActivity extends Activity {

    @MiloConfig2(1)
    private boolean state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
