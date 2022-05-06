package com.example.restaurante.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfiguracaoFirebase {

    private static FirebaseAuth autenticacao;//Cria atributo de autenticação para login e senha
    private static DatabaseReference referenciaFirebase;

    //Retorna a instância do FirebaseDatabase
    public static DatabaseReference getFirebaseDatabase(){

        if(referenciaFirebase == null){
            referenciaFirebase = FirebaseDatabase.getInstance().getReference();
        }
        return referenciaFirebase;
    }

    //retorna a instância do FirebaseAuth para autenticação do usuário
    public static FirebaseAuth getFirebaseAutenticação(){

        if(autenticacao == null) {
            autenticacao = FirebaseAuth.getInstance();
        }
        return autenticacao;
    }
}