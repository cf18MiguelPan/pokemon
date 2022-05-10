package com.example.pokemon;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {

    //Para el Logcat
    private static final String TAG = "GoogleActivity";
    //Autenticar con el firebase
    private FirebaseAuth mAuth;
    //Cliente Log In
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Botón de Google
        SignInButton btnGoogle = findViewById(R.id.sign_in_button);
        //Autenticar con el firebase y obtener la instancia
        mAuth = FirebaseAuth.getInstance();

        //Opciones de Loguear Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //Obtener la cuenta al mostrar el pop up de cuentas de Google
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //Al pulsar el boton de Google
        btnGoogle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                resultLauncher.launch(new Intent(mGoogleSignInClient.getSignInIntent()));
            }
        });

    }

    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result){
            if(result.getResultCode() == Activity.RESULT_OK){
                Intent intent = result.getData();

                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(intent);
                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);

                    assert account != null;
                    firebaseAuthWithGoogle(account.getIdToken());
                    Log.i(TAG, "Ha sido un exito2", task.getException());
                } catch (ApiException e) {
                    Log.i(TAG, "Ha sido un fracaso2", task.getException());
                }

            }

        }
    });

    //Autenticar el firebase con Google
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Si se ha registrado sin problemas te mandará a otra actividad
                        if (task.isSuccessful()) {
                            startActivity(new Intent(MainActivity.this, UserProfileActivity.class));

                            Toast.makeText(MainActivity.this, "Successful login", Toast.LENGTH_SHORT).show();
                            Log.i(TAG, "Ha sido un exito", task.getException());
                        // Si no has podido registrar
                        } else {
                            Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                            Log.i(TAG, "Ha sido un fracaso", task.getException());
                        }

                    }
                });
    }

}