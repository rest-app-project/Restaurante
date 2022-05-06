package com.example.restaurante.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restaurante.R;
import com.example.restaurante.anim.MyBounceInterpolator;
import com.example.restaurante.config.ConfiguracaoFirebase;
import com.example.restaurante.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {

    private EditText editEmailLogin, editSenhaLogin;
    private Button buttonEntrarLogin;
    private Usuario usuario;
    private FirebaseAuth autenticacao;
    private TextView textViewCadastre;
    private ProgressBar progressBarLogin;
    private String textoValidacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inicializarComponentesUi();

        /*** evento de clique para botão login ***/
        buttonEntrarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyBounceInterpolator.vibrar(v.getContext());
                MyBounceInterpolator.animationBounce(v, v.getContext());

                progressBarLogin.setVisibility(View.VISIBLE);
                buttonEntrarLogin.setText("Verificando");
                buttonEntrarLogin.setEnabled(false);

                String textoEmail = editEmailLogin.getText().toString();
                String textoSenha = editSenhaLogin.getText().toString();

                /*** verificações de preenchimento de campos ***/
                if(!textoEmail.isEmpty()){
                    if(!textoSenha.isEmpty()){

                        if(verificarInternet()) {

                            usuario = new Usuario();
                            usuario.setEmailUsuario(textoEmail);
                            usuario.setSenhaUsuario(textoSenha);
                            validarLogin();

                        } else{
                            textoValidacao = "Sem conexão com a internet.";
                            rodarHandlerLogin(textoValidacao);
                        }

                    }else{
                        textoValidacao = "Por favor preencha a senha.";
                        rodarHandlerLogin(textoValidacao);
                    }

                }else{
                    textoValidacao = "Por favor preencha o email.";
                    rodarHandlerLogin(textoValidacao);
                }


            }
        });


        /*** evento de clique para possibilidade do usuário se cadastrar ***/
        textViewCadastre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyBounceInterpolator.vibrar(v.getContext());
                MyBounceInterpolator.animationBounce(v, v.getContext());

                Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
                startActivity(intent);
            }
        });


    }

    /*** validando meu login com email e senha do usuário ***/
    public void validarLogin(){

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticação();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmailUsuario(),
                usuario.getSenhaUsuario()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){ // SE task == Ok abre tela principal

                    abrirTelaPrincipal();
                    Toast.makeText(getApplicationContext(), "Login efetuado com sucesso", Toast.LENGTH_LONG).show();

                }else{ // SE task == ERRO lança exceções

                    try {
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e) { //CASO O USUÁRIO NÃO ESTEJA CADASTRADO
                        textoValidacao = "Usuário não está cadastrado!";
                        rodarHandlerLogin(textoValidacao);
                    }catch (FirebaseAuthInvalidCredentialsException e){//CASO EMAIL E SENHA NÃO CORRESPONDAM A USUARIO CADASTRADO
                        textoValidacao = "Email e senha não correspondem a um usuário.";
                        rodarHandlerLogin(textoValidacao);
                    }catch(Exception e){
                        textoValidacao = "Erro ao logar usuário: " + e.getMessage();
                        rodarHandlerLogin(textoValidacao);
                        e.printStackTrace();

                    }

                    Toast.makeText(LoginActivity.this, textoValidacao,Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /*** para abrir tela principal do app ***/
    public void abrirTelaPrincipal(){

        startActivity(new Intent(this, MainActivity.class));
        finish(); //Para fechar a LoginActivity
        Toast.makeText(LoginActivity.this,"Login efetuado com sucesso",Toast.LENGTH_LONG).show();
    }

    /*** verificando conexão com internet ao logar ***/
    public boolean verificarInternet(){

        ConnectivityManager conexao = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = conexao.getActiveNetworkInfo();

        if(info != null && info.isConnected()){
            return true;
        }else{
            return false;
        }
    }

    /*** temporizador para modificação de meus componentes da UI ***/
    public void rodarHandlerLogin(String validacao){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBarLogin.setVisibility(View.GONE);
                buttonEntrarLogin.setText("Entrar");
                buttonEntrarLogin.setEnabled(true);
                Toast.makeText(LoginActivity.this, validacao, Toast.LENGTH_LONG).show();
            }
        }, 1500);
    }

    /*** inicializando meus componentes da UI (chamo no onCreate) ***/
    public  void inicializarComponentesUi(){

        editEmailLogin = findViewById(R.id.editEmailLogin);
        editSenhaLogin = findViewById(R.id.editSenhaLogin);
        buttonEntrarLogin = findViewById(R.id.buttonLogin);
        textViewCadastre = findViewById(R.id.textViewCadastrese);
        progressBarLogin = findViewById(R.id.progressBarLogin);
        progressBarLogin.setVisibility(View.GONE);
    }
}