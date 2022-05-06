package com.example.restaurante.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.restaurante.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        user = FirebaseAuth.getInstance().getCurrentUser(); //para capturar meu usuário atual

        /*** temporizador para rodar tela de splash com a logo e layout do app ***/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                abrirAutenticacao();
            }
        }, 2000);
    }

    /*** Verificação se o usuário já estava previamente logado direcionando assim para tela prevista ***/
    private void abrirAutenticacao(){ //Verificar se o usuário está logado

        if(user != null) {
            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }else{
            Intent i = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
    }

}