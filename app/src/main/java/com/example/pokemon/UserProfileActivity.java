package com.example.pokemon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserProfileActivity extends AppCompatActivity {

    // Inicializamos las variables
    ImageView Image;                                                  //Foto de perfil
    TextView Name;                                                    //Nombre usuario
    TextView Email;                                                   //Email usuario
    Button btnLogout;                                                    //Botón de salir
    FirebaseAuth firebaseAuth;                                          //Autenticación firebase
    GoogleSignInClient googleSignInClient;                              //Inicio de sesión de cliente de Google
    Button dex;                                                         //Botón de la Pokédex

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Assignando variables
        Image=findViewById(R.id.foto);                            // Variable foto perfil
        Name=findViewById(R.id.nombre);                              // Variable nombre usuario
        Email=findViewById(R.id.email);                            // Variable nombre usuario
        btnLogout=findViewById(R.id.logout);                          // Variable botón de salir


        firebaseAuth=FirebaseAuth.getInstance();                        // Inicializar inicializa la instancia FirebaseAuth
        FirebaseUser usuario=firebaseAuth.getCurrentUser();        // Inicializar usuario de firebase

        // Cuando el usuario de firebase no es nulo
        if(usuario!=null) {
            // La foto de perfil de la cuenta
            Glide.with(UserProfileActivity.this)
                    .load(usuario.getPhotoUrl())
                    .into(Image);
            // Nombre de la cuenta
            Name.setText(usuario.getDisplayName());
            // Email de la cuenta
            Email.setText(usuario.getEmail());
        }

        // Inicializar el inicio de sesión cliente
        googleSignInClient= GoogleSignIn.getClient(UserProfileActivity.this,GoogleSignInOptions.DEFAULT_SIGN_IN);

        dex = findViewById(R.id.pkdx);
        dex.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                IrPokedex();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cerrar sesión de Google
                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Si ha sido un exito
                        if(task.isSuccessful()) {
                            // Cerrar sesión
                            firebaseAuth.signOut();

                            Toast.makeText(getApplicationContext(), "Fue un exito Logout", Toast.LENGTH_SHORT).show();

                            // Terminar actividad
                            finish();
                        }
                    }
                });
            }
        });
    }

    //Método que te manda a la actividad de pokédex
    private void IrPokedex() {
        Intent i = new Intent(this, pokedex.class);
        startActivity(i);
    }

}