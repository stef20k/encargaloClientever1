package com.example.cliente2;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

public class vespass extends Activity {

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pass);

        DisplayMetrics dm =new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);


    }
}
