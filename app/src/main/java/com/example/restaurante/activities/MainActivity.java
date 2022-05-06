package com.example.restaurante.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restaurante.R;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private TextView textViewNomeUsuarioMain;
    private WebView webViewMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializarComponentesUI();

        /*** Meu WebView que vai puxar as informações do WebService ***/
        webViewMain.setWebViewClient(new WebViewClient());
        webViewMain.loadUrl("https://www.google.com.br"); // ainda linkado com o google a título de teste
        WebSettings webSettings = webViewMain.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    /*** método sobrescrito para manipulação do webView ***/
    @Override
    public void onBackPressed(){
        if (webViewMain.canGoBack()){
            webViewMain.goBack();
        }else{
            super.onBackPressed();
        }
    }

    /*** inflando meu menu da página principal do App ***/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*** Eventos de cliques para o menu_main ***/
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_info:
                Toast.makeText(this, "Caso queira, tela informações", Toast.LENGTH_SHORT).show(); //Caso eu queira adicionar mais uma tela com informações sobre o app
                break;
            case R.id.menu_sair: //desloga usuário
                deslogarUsuario();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /*** Deslogando usuário e enviando para tela de login ***/
    public void deslogarUsuario(){
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
        Toast.makeText(this, "Usuário deslogado com sucesso", Toast.LENGTH_SHORT).show();
        finish();
    }

    /*** trabalhando com o ciclo de vida para carregar as informações na toolbar e um temporizador para informação do nome do usuário logado ***/
    @Override
    protected void onStart() {
        super.onStart();

        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("Restaurante App");
        actionbar.setElevation(0);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                String nomeUsuario = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                textViewNomeUsuarioMain.setText(nomeUsuario);

            }
        }, 2000);
    }

    /*** inicializando os componentes de minha UI ***/
    public void inicializarComponentesUI(){
        textViewNomeUsuarioMain = findViewById(R.id.textViewNomeUsuarioMain);
        webViewMain = findViewById(R.id.webViewMain);
    }
}