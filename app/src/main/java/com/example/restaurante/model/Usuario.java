package com.example.restaurante.model;

import com.example.restaurante.config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

public class Usuario { /*** minha classe model para acesso ao meu usuário - ENCAPSULAMENTO ***/

    private String idUsuario;
    private String NomeUsuario;
    private String emailUsuario;
    private String senhaUsuario;

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNomeUsuario() {
        return NomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        NomeUsuario = nomeUsuario;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    @Exclude
    public String getSenhaUsuario() { //a senha lanço um @exclude para não salvar, por questões éticas e LGPD
        return senhaUsuario;
    }

    public void setSenhaUsuario(String senhaUsuario) {
        this.senhaUsuario = senhaUsuario;
    }

    /*** método para salvar meu usuário ***/
    public void salvarUsuario(){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference usuariosRef = firebaseRef.child("usuarios").child( getIdUsuario() );
        usuariosRef.setValue( this );
    }
}
