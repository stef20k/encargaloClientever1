package com.example.cliente2.adapter;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.cliente2.R;

public class FragmentosFun extends FragmentActivity {

    public static String dato="";

    public void createFragment(Fragment fragment,Context context){
        FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();

        transaction.add(R.id.nav_host_fragment, fragment);

        transaction.commit();
    }
    public void showFragment(Fragment fragment,Context context){
        FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();


        transaction.show(fragment);
        transaction.commit();
    }
    public void hideFragment(Fragment fragment,Context context){
        FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();

        transaction.hide(fragment);
        transaction .commit();
    }

    public void Setdato(String t){
         dato=t;
        Log.d("hola2",dato);
    }

    public String Getdato(){
        Log.d("hola",dato);
        return dato;
    }


}
