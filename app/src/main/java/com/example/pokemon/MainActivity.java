package com.example.pokemon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {

    // Inicializar variables
    SignInButton btnSignIn;
    GoogleSignInClient googleSignInClient;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // asignar variable
        btnSignIn=findViewById(R.id.sign_in_button);

        // Inicializar las opciones de inicio de sesion
        GoogleSignInOptions googleSignInOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Inicializar el inicio de sesión del cliente
        googleSignInClient= GoogleSignIn.getClient(MainActivity.this
                ,googleSignInOptions);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Inicializar sign in intent
                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent,100);

            }
        });

        firebaseAuth=FirebaseAuth.getInstance();                // Inicializar la autenticación firebase
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();// Inicializar el de usuario firebase

        // Cuando el usuario no es nulo
        if(firebaseUser!=null) {
            // Desde el MainActivity te redirige a UserProfileActivity
            startActivity(new Intent(MainActivity.this, UserProfileActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)); //FLAG_ACTIVITY_NEW_TASK: Inicia la actividad en una nueva tarea
        }           //setFlags: Controla como manejar los Intents
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Si el requestCode es igual a 100
        // requestCode: ayuda a identificar de qué Intención regresa
        if(requestCode==100) {
            // Inicializar tarea
            Task<GoogleSignInAccount> signInAccountTask=GoogleSignIn.getSignedInAccountFromIntent(data);

            // Si la tarea ha sido un exito
            if(signInAccountTask.isSuccessful()) {
                // Inicializar el inicio de sesion de la cuenta
                try {
                    //Inicializar el inicio de sesion de la cuenta
                    GoogleSignInAccount googleSignInAccount=signInAccountTask.getResult(ApiException.class);

                    // Cuando la cuenta no es nulo
                    if(googleSignInAccount!=null) {
                        // Inicializar autenticación de credenciales
                        // AuthCredential: Representa una credencial que el servidor de Firebase Authentication puede usar para autenticar a un usuario
                        AuthCredential authCredential= GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(),null);

                        // Comprobar las credenciales
                        firebaseAuth.signInWithCredential(authCredential)
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        // Sale bien
                                        if(task.isSuccessful()) {
                                            // Te manda a la actividad UserProfileActivity
                                            startActivity(new Intent(MainActivity.this
                                                    ,UserProfileActivity.class)
                                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

                                            Toast.makeText(MainActivity.this, "Has entrado", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            // Sale mal
                                            Toast.makeText(MainActivity.this, "No has entrado", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
                catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}