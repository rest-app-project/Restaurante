package com.example.restaurante.helper;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.restaurante.config.ConfiguracaoFirebase;
import com.example.restaurante.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UsuarioFirebase { /*** classe para retornar alguns dados que posso querer usar do usuário ***/

    public static FirebaseUser getUsuarioAtual(){

        FirebaseAuth usuario = ConfiguracaoFirebase.getFirebaseAutenticação();
        return usuario.getCurrentUser();

    }

    /*** retornar o UUID do usuário, meu identificador único ***/
    public static String getIdentificadorUsuario(){
        return getUsuarioAtual().getUid();
    }


    /*** estou salvando os dados do usuário no profile do firebase por questões de facilidade ao recuperar ***/
    public static void salvarNomeUsuario(String nome){

        try {

            //Usuario logado no App
            FirebaseUser usuarioLogado = getUsuarioAtual();

            //Configurar objeto para alteração do perfil
            UserProfileChangeRequest profile = new UserProfileChangeRequest
                    .Builder()
                    .setDisplayName( nome )
                    .build();
            usuarioLogado.updateProfile( profile ).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if( !task.isSuccessful() ){
                        Log.d("Perfil","Erro ao atualizar nome de perfil." );
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*** retorna todos os dados do usuário logado ***/
    public static Usuario getDadosUsuarioLogado(){

        FirebaseUser firebaseUser = getUsuarioAtual();

        Usuario usuario = new Usuario();
        usuario.setNomeUsuario(firebaseUser.getDisplayName());
        usuario.setEmailUsuario(firebaseUser.getEmail());
        usuario.setIdUsuario(firebaseUser.getUid());


        return usuario;
    }

}

