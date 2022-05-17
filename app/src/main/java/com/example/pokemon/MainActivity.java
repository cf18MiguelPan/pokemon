
package com.example.pokemon;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

        SignInButton btnGoogle = findViewById(R.id.sign_in_button);
        //Autenticar con el firebase y obtener la instancia
        mAuth = FirebaseAuth.getInstance();

        //Opciones de Loguear Google
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //Obtener la cuenta al mostrar el pop up de cuentas de Google
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

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
                        // Si la tarea ha terminado te mandará a otra actividad
                        if (task.isSuccessful()) {
                            startActivity(new Intent(MainActivity.this
                                    ,UserProfileActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

                            Toast.makeText(MainActivity.this, "Successful login", Toast.LENGTH_SHORT).show();

                            Log.i(TAG, "Ha sido un exito", task.getException());

                        // Si no has podido entrar
                        } else {
                            Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();

                            Log.i(TAG, "Ha sido un fracaso", task.getException());
                        }

                    }
                });
    }

}

























/*
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
 */