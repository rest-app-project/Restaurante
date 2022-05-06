package com.example.restaurante.anim;

import android.content.Context;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

import com.example.restaurante.R;

public class MyBounceInterpolator implements Interpolator {

    private double myAmplitude = 1;
    private double myFrequency = 10;

    public MyBounceInterpolator(double amplitude, double frequency){
        myAmplitude = amplitude;
        myFrequency = frequency;
    }

    /*** retorna o efeito customizado desejado ***/
    @Override
    public float getInterpolation(float time){
        return (float)(-1* Math.pow(Math.E,-time/myAmplitude)* Math.cos(myFrequency*time)+1);
    }

    /*** Cria animação Bounce (para balançar) os botões ***/
    public static void animationBounce(View view,Context c) {
        final Animation animation = AnimationUtils.loadAnimation(c, R.anim.bounce); //Instancia um animation e aponta para o resource file bounce em : res/anim/bounce
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.05, 5); //Instancia classe criada para facilitar e controlar o interpolator para animação bounce
        animation.setInterpolator(interpolator);
        view.startAnimation(animation); //inicia animação
    }

    /*** Método para vibrar as teclas ***/
    public static void vibrar(Context c) {
        Vibrator vibrator = (Vibrator) c.getSystemService(Context.VIBRATOR_SERVICE);//Instancia vibrator + permição contida no Android Manifest
        long millissegundos = 40;
        vibrator.vibrate(millissegundos);
    }
}
