package com.example.cliente2.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cliente2.R;


public class ChatFragment extends Fragment {

    TextView nombre;
    String idusuario;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_chat, container, false);
        nombre=view.findViewById(R.id.nombrePerfil);
        idusuario= getActivity().getIntent().getExtras().getString("idusuario");

        nombre.setText("Mi usuario");
        return view;
    }
}