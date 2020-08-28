package com.example.firebasettt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    FirebaseFirestore db;
    private FirebaseAuth auth;
    Button register, back;
    String emailAddress, password, name;
    EditText email, psw, nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        register = findViewById(R.id.register);
        back = findViewById(R.id.indietro);
        email = findViewById(R.id.eR);
        psw = findViewById(R.id.pswR);
        nickname = findViewById(R.id.nome);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailAddress = email.getText().toString().trim();
                password = psw.getText().toString().trim();
                name = nickname.getText().toString();
                if (emailAddress.isEmpty()) {
                    email.setError("Provide your Email first!");
                    email.requestFocus();
                } else if (password.isEmpty()) {
                    psw.setError("Set your password");
                    psw.requestFocus();
                } else if (password.isEmpty()) {
                    nickname.setError("Set your name");
                    nickname.requestFocus();
                } else if (emailAddress.isEmpty() && password.isEmpty() && name.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Fields Empty!", Toast.LENGTH_SHORT).show();
                } else if (!(emailAddress.isEmpty() && password.isEmpty() && name.isEmpty())) {
                    auth.createUserWithEmailAndPassword(emailAddress, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {

                            if (!task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this.getApplicationContext(),
                                        "SignUp unsuccessful: " + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, "User SignUp successfully", Toast.LENGTH_SHORT).show();
                                go();
                                //startActivity(new Intent(MainActivity.this, ChooseGame.class));
                            }
                        }
                    });
                } else {
                    Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void go(){

        HashMap<String, Object> newuser = new HashMap<>();
        newuser.put("nome", name);
        newuser.put("vinte", 0);
        newuser.put("perse", 0);
        newuser.put("pareggiate", 0);
        //newuser.put("giocate", "0");

        db.collection("users").document(auth.getUid())
                .set(newuser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "DocumentSnapshot successfully written!");
                        Intent intent = new Intent(RegisterActivity.this, ChooseGame.class);
                        intent.putExtra("NOME", name);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error writing document", e);
                        Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public void onBackPressed() {
// super.onBackPressed();
// Not calling **super**, disables back button in current screen.
    }
}
