package com.example.firebasettt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;


public class ChooseGame extends AppCompatActivity {

    Button logout, statistiche;
    CardView player1, player2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_game);

        player1 = findViewById(R.id.card1);
        player2 = findViewById(R.id.card2);
        logout = findViewById(R.id.logout);
        statistiche = findViewById(R.id.btn_stat);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChooseGame.this, "Logout", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ChooseGame.this, MainActivity.class);
                startActivity(intent);
                //finish();
            }
        });

        player1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseGame.this, MultiPlayer.class);
                intent.putExtra("PLAYER", "p1");
                startActivity(intent);
                finish();
            }
        });
        player2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseGame.this, InsertCode.class);
                startActivity(intent);
                finish();
            }
        });

        statistiche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseGame.this, Statistiche.class);
                startActivity(intent);
            }
        });




    }

    @Override
    public void onBackPressed() {
// super.onBackPressed();
// Not calling **super**, disables back button in current screen.
    }




}
