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
import android.widget.Toast;

import com.example.restaurante.R;
import com.example.restaurante.anim.MyBounceInterpolator;
import com.example.restaurante.config.ConfiguracaoFirebase;
import com.example.restaurante.helper.UsuarioFirebase;
import com.example.restaurante.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivity extends AppCompatActivity {

    private Button buttonCadastrarUsuario;
    private ProgressBar progressBarCadastroUsuario;
    private EditText campoNome, campoEmail, campoSenha, campoSenhaRepeat;
    private FirebaseAuth autenticacao;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.restaurante.R.layout.activity_cadastro);

        inicializarComponentesUi();

        /*** evento de clique para o botão cadastrarUsuario ***/
        buttonCadastrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyBounceInterpolator.vibrar(v.getContext());
                MyBounceInterpolator.animationBounce(v, v.getContext());

                progressBarCadastroUsuario.setVisibility(View.VISIBLE);
                buttonCadastrarUsuario.setText("Verificando");
                buttonCadastrarUsuario.setEnabled(false);

                String textoNome = campoNome.getText().toString();
                String textoEmail = campoEmail.getText().toString();
                String textosenha = campoSenha.getText().toString();
                String textoSenhaRepetida = campoSenhaRepeat.getText().toString();

                /*** verificações de preenchimento ***/
                if(!textoNome.isEmpty()){
                    if(!textoEmail.isEmpty()){
                       if(!textosenha.isEmpty()){
                           if(!textoSenhaRepetida.isEmpty()){
                               if(textosenha.equals(textoSenhaRepetida)){
                                   if(verificarInternet()){

                                       usuario = new Usuario();
                                       usuario.setNomeUsuario(textoNome);
                                       usuario.setEmailUsuario(textoEmail);
                                       usuario.setSenhaUsuario(textosenha);
                                       cadastrar(usuario); /*** chama método cadastrar usuário ***/

                                   }else{
                                       rodarHandlerCadastro();
                                       Toast.makeText(CadastroActivity.this, "Dispositivo sem conexão", Toast.LENGTH_SHORT).show();
                                   }

                               }else{
                                   rodarHandlerCadastro();
                                   Toast.makeText(CadastroActivity.this, "As senhas estão diferentes", Toast.LENGTH_SHORT).show();
                               }

                           }else{
                               rodarHandlerCadastro();
                               Toast.makeText(CadastroActivity.this, "Please, preencha o campo repetição de senha", Toast.LENGTH_SHORT).show();
                           }

                       }else{
                           rodarHandlerCadastro();
                           Toast.makeText(CadastroActivity.this, "Please, preencha o campo senha", Toast.LENGTH_SHORT).show();
                       }

                    }else{
                        rodarHandlerCadastro();
                        Toast.makeText(CadastroActivity.this, "Please, preencha o email", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    rodarHandlerCadastro();
                    Toast.makeText(CadastroActivity.this, "Please, preencha o campo nome", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    /*** cadastrando e autenticando usuário ***/
    public void cadastrar(final Usuario usuario) {

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticação();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmailUsuario(),
                usuario.getSenhaUsuario()
        ).addOnCompleteListener(
                this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            try {

                                //Salvar dados no firebase
                                String idUsuario = task.getResult().getUser().getUid();
                                usuario.setIdUsuario(idUsuario);
                                usuario.salvarUsuario();

                                //Salvar dados no profile firebase para recuperar mais facilmente
                                UsuarioFirebase.salvarNomeUsuario(usuario.getNomeUsuario());

                                Intent intent = new Intent(CadastroActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();

                                Toast.makeText(CadastroActivity.this,
                                        "Cadastro com sucesso",
                                        Toast.LENGTH_SHORT).show();


                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        } else { // caso dê erro, cai no bloco de exceções

                            String erroExcecao = "";
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                rodarHandlerCadastro();
                                erroExcecao = "Digite uma senha mais forte.";
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                rodarHandlerCadastro();
                                erroExcecao = "Por favor, digite um email válido.";
                            } catch (FirebaseAuthUserCollisionException e) {
                                rodarHandlerCadastro();
                                erroExcecao = "Este usuário já foi cadastrado.";
                            } catch (Exception e) {
                                rodarHandlerCadastro();
                                erroExcecao = " Erro ao cadastrar usuário: " + e.getMessage();
                                e.printStackTrace();
                            }

                            rodarHandlerCadastro();
                            Toast.makeText(CadastroActivity.this,
                                    "Erro: " + erroExcecao,
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                }
        );
    }

    /*** temporizador de carregamento de meus componentes da UI ***/
    public void rodarHandlerCadastro(){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBarCadastroUsuario.setVisibility(View.GONE);
                buttonCadastrarUsuario.setText("Cadastrar");
                buttonCadastrarUsuario.setEnabled(true);
            }
        }, 1500);
    }

    /*** método verificação de Internet caso usuário esteja sem conexão ***/
    public boolean verificarInternet(){

        ConnectivityManager conexao = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = conexao.getActiveNetworkInfo();

        if(info != null && info.isConnected()){
            return true;
        }else{
            return false;
        }
    }

    /*** inicialização de meu componentes da UI (chamo no onCreate) ***/
    public void inicializarComponentesUi(){

        buttonCadastrarUsuario = findViewById(R.id.buttonCadastrarUsuario);
        progressBarCadastroUsuario = findViewById(R.id.progressBarCadastroUsuario);
        campoNome = findViewById(R.id.editNomeCadastroUsuario);
        campoEmail = findViewById(R.id.editEmailCadastroUsuario);
        campoSenha = findViewById(R.id.senhaCadastroUsuario);
        campoSenhaRepeat = findViewById(R.id.repetirSenhaCadastroUsuario);

        progressBarCadastroUsuario.setVisibility(View.GONE);
    }

}