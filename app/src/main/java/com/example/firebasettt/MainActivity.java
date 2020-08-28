package com.example.firebasettt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    String emailAddress, password;
    TextInputEditText email;
    EditText psw;
    Button login, signup;
    private FirebaseAuth auth;

/**
 * COSE DA MIGLIORARE
 * //FATTO// label con email
 * //FATTO// colore pulasnti di chi vince
 * //FATTO// in alcuni schermi il pulsante statistiche è attaccato alle card view
 * //FATTO// aggiumgere pulsante indietro nella schermata statistiche
 * grafica
 * ogni giocatore ha un codice suo invece di fare per ogni partita un codice nuovo (lo metto nella card view del primo gicatore)
 * //FATTO// fare icona app
 * //FATTO// quando il giocatore due deve inserire il codice faccio apparire la tastiera con i numeri
 *
 */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

      /* Intent intent = new Intent(MainActivity.this, ChooseGame.class);
        intent.putExtra("USERID", "tJOi1rQz7vVJ7knVAN4iy9pigdi1");
        startActivity(intent);
*/
        if (auth.getCurrentUser() != null) {
            // Already signed in
            go();
        } else {

            email = findViewById(R.id.eR);
            psw = findViewById(R.id.pswR);
            login = findViewById(R.id.register);
            signup = findViewById(R.id.sign);


            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    emailAddress = email.getText().toString().trim();
                    password = psw.getText().toString().trim();

                    if(!emailAddress.isEmpty() && emailAddress!=null && !password.isEmpty() && password!=null) {
                        auth.signInWithEmailAndPassword(emailAddress, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            go();
                                        } else {

                                            Toast.makeText(MainActivity.this, "User not logged successfully", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                    }

                }
            });

            /*  */

            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(intent);
                   /* emailAddress = email.getText().toString();
                    password = psw.getText().toString();
                    if (emailAddress.isEmpty()) {
                        email.setError("Provide your Email first!");
                        email.requestFocus();
                    } else if (password.isEmpty()) {
                        psw.setError("Set your password");
                        psw.requestFocus();
                    } else if (emailAddress.isEmpty() && password.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Fields Empty!", Toast.LENGTH_SHORT).show();
                    } else if (!(emailAddress.isEmpty() && password.isEmpty())) {
                        auth.createUserWithEmailAndPassword(emailAddress, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {

                                if (!task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this.getApplicationContext(),
                                            "SignUp unsuccessful: " + task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "User SignUp successfully", Toast.LENGTH_SHORT).show();
                                    go();
                                    //startActivity(new Intent(MainActivity.this, ChooseGame.class));
                                }
                            }
                        });
                    } else {
                        Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }*/
                }
            });
        }

    }

    public void go(){
        Toast.makeText(MainActivity.this, "User logged successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, ChooseGame.class);
        startActivity(intent);
        //finish();
    }

    @Override
    public void onBackPressed() {
// super.onBackPressed();
// Not calling **super**, disables back button in current screen.
    }
}
