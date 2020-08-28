package com.example.firebasettt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.collection.LLRBNode;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MultiPlayer extends AppCompatActivity {

    TextView tvP1;
    Button btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8;
    Button startPlay;

    final String segnoP1 = "X";
    final String segnoP2 = "O";
    String segno;
    String player;
    String codice;

    ArrayList<Button> listButton;
    Map<String, Object> game;
    ArrayList<HashMap<String,String>> moves;
    ArrayList<Integer> partite;

    FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_player);

        tvP1 = findViewById(R.id.tv_player1);
        tvP1.setVisibility(View.GONE);
        startPlay = findViewById(R.id.strat_game);

        listButton = new ArrayList<>();
        moves = new ArrayList<>();
        game = new HashMap<>();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            player = extras.getString("PLAYER");

            if(player!= null && player.equals("p1")){
                segno=segnoP1;
                tvP1.setVisibility(View.VISIBLE);
                Random r = new Random();
                Integer codiceI = r.nextInt((9999 - 1000) + 1) + 1000;
                codice =  codiceI.toString();
                tvP1.setText("Player 2 write this code to play: "+codice);
            }else{
                player="p2";
                segno=segnoP2;
                codice = extras.getString("CODICE");
            }
        }

        db = FirebaseFirestore.getInstance();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        firestore.setFirestoreSettings(settings);
        auth = FirebaseAuth.getInstance();

        btn0 = findViewById(R.id.b0);
        listButton.add(btn0);
        btn1 = findViewById(R.id.b1);
        listButton.add(btn1);
        btn2 = findViewById(R.id.b2);
        listButton.add(btn2);
        btn3 = findViewById(R.id.b3);
        listButton.add(btn3);
        btn4 = findViewById(R.id.b4);
        listButton.add(btn4);
        btn5 = findViewById(R.id.b5);
        listButton.add(btn5);
        btn6 = findViewById(R.id.b6);
        listButton.add(btn6);
        btn7 = findViewById(R.id.b7);
        listButton.add(btn7);
        btn8 = findViewById(R.id.b8);
        listButton.add(btn8);

        setClickable(false);
        newGame(codice);

        //creo un gioco
        startPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setUser("p2"); //per i test
                deleteGame(codice);
                Intent intent = new Intent(MultiPlayer.this, ChooseGame.class);
                startActivity(intent);
            }
        });


        //per i cambiamenti del documento che sto usando
        final DocumentReference docRef = db.collection("games").document(codice);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("TAG", "Listen failed.", e);
                    return;
                }
                if (snapshot != null && snapshot.exists()) {
                    Log.d("TAG", "Current data: " + snapshot.getData());

                    moves = (ArrayList<HashMap<String, String>>) snapshot.getData().get("moves");

                    //quando viene modificato il documento clickable (che sia stato il p1 o il p2)
                    if(moves.get(moves.size()-1).get("sign").equals(segnoP1) && player.equals("p1")){
                        setClickable(false);
                    }else if(moves.get(moves.size()-1).get("sign").equals(segnoP2) && player.equals("p2")){
                        setClickable(false);
                    }else{
                        setClickable(true);
                    }

                    if(moves.size()==1 && moves.get(0).get("sign").equals("p1")){
                       // Toast.makeText(MultiPlayer.this, ""+moves.size(), Toast.LENGTH_LONG).show();
                        setClickable(false);
                    }

                    HashMap<String, String> moveR = new HashMap<>();
                    if(moves != null || !moves.isEmpty()){
                        for(int i=0; i< moves.size(); i++) {
                            moveR = moves.get(i);
                            switch (moveR.get("position")) {
                                case "0":
                                    caseFunction(btn0, moveR.get("sign"));
                                    break;
                                case "1":
                                    caseFunction(btn1, moveR.get("sign"));
                                    break;
                                case "2":
                                    caseFunction(btn2, moveR.get("sign"));
                                    break;
                                case "3":
                                    caseFunction(btn3, moveR.get("sign"));
                                    break;
                                case "4":
                                    caseFunction(btn4, moveR.get("sign"));
                                    break;
                                case "5":
                                    caseFunction(btn5, moveR.get("sign"));
                                    break;
                                case "6":
                                    caseFunction(btn6, moveR.get("sign"));
                                    break;
                                case "7":
                                    caseFunction(btn7, moveR.get("sign"));
                                    break;
                                case "8":
                                    caseFunction(btn8, moveR.get("sign"));
                                    break;
                            }

                        }
                       // checkWinner();
                        if(moves.size()==10)
                            checkWinner(true);
                        else
                            checkWinner(false);
                    }
                } else {
                    Log.d("TAG", "Current data: null");
                }
            }
        });


        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click("0", btn0);
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click("1", btn1);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click("2", btn2);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click("3", btn3);
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click("4", btn4);
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click("5", btn5);
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { click("6", btn6);
            }
        });
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click("7", btn7);
            }
        });
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click("8", btn8);
            }
        });


        }

        //per le mosse quando clicco un pulsante
    public void click(String position, Button btn){
        HashMap<String, String> move = new HashMap<>();
        move.put("position", position);
        move.put("sign",segno);
        moves.add(move);
        game.put("moves", moves);
        btn.setText(segno);
        db.collection("games").document(codice)
                .set(game)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error writing document", e);
                    }
                });
        listButton.remove(btn); //Integer.parseInt(position)
        btn.setEnabled(false);
        setClickable(false);

    }

    public void setClickable(boolean isClickable){
        for(Button btn: listButton){
            btn.setEnabled(isClickable);
        }
    }

    public void caseFunction(Button btn, String sign){
        btn.setEnabled(false);
        listButton.remove(btn);
        btn.setText(sign);
    }

    //new document
    public void newGame(String CodiceS){
        HashMap<String, String> move = new HashMap<>();
        move.put("position", "-1");
        move.put("sign", player);
        moves = new ArrayList<>();
        moves.add(move);
        game.put("moves", moves);

        db.collection("games").document(CodiceS)
                .set(game)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error writing document", e);
                    }
                });
    }


    //delete document
    public void deleteGame(String codiceS){
        db.collection("games").document(codiceS)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error deleting document", e);
                    }
                });
    }

    public void checkWinner(boolean isLast){
        //prima riga
        if(btn0.getText().equals(segnoP1) && btn0.getText().equals(btn1.getText()) && btn0.getText().equals(btn2.getText())){
            isWinner("Player 1", "p1");
            coloraCaselleVincenti(btn0,btn1,btn2);
        }
        else if(btn0.getText().equals(segnoP2) && btn0.getText().equals(btn1.getText()) && btn0.getText().equals(btn2.getText())){
            isWinner("Player 2", "p2");
            coloraCaselleVincenti(btn0,btn1,btn2);
        }
        //seconda riga
        else if(btn3.getText().equals(segnoP1) && btn3.getText().equals(btn4.getText()) && btn3.getText().equals(btn5.getText())){
            isWinner("Player 1", "p1");
            coloraCaselleVincenti(btn3,btn4,btn5);
        }
        else if(btn3.getText().equals(segnoP2) && btn3.getText().equals(btn4.getText()) && btn3.getText().equals(btn5.getText())){
            isWinner("Player 2", "p2");
            coloraCaselleVincenti(btn3,btn4,btn5);
        }
        //terza riga
        else if(btn6.getText().equals(segnoP1) && btn6.getText().equals(btn7.getText()) && btn6.getText().equals(btn8.getText())){
            isWinner("Player 1", "p1");
            coloraCaselleVincenti(btn6,btn7,btn8);
        }
        else if(btn6.getText().equals(segnoP2) && btn6.getText().equals(btn7.getText()) && btn6.getText().equals(btn8.getText())){
            isWinner("Player 2", "p2");
            coloraCaselleVincenti(btn6,btn7,btn8);
        }
        //prima colonna
        else if(btn0.getText().equals(segnoP1) && btn0.getText().equals(btn3.getText()) && btn0.getText().equals(btn6.getText())){
            isWinner("Player 1", "p1");
            coloraCaselleVincenti(btn0,btn3,btn6);
        }
        else if(btn0.getText().equals(segnoP2) && btn0.getText().equals(btn3.getText()) && btn0.getText().equals(btn6.getText())){
            isWinner("Player 2", "p2");
            coloraCaselleVincenti(btn0,btn3,btn6);
        }
        //seconda colonna
        else if(btn1.getText().equals(segnoP1) && btn1.getText().equals(btn4.getText()) && btn1.getText().equals(btn7.getText())){
            isWinner("Player 1", "p1");
            coloraCaselleVincenti(btn1,btn4,btn7);
        }
        else if(btn1.getText().equals(segnoP2) && btn1.getText().equals(btn4.getText()) && btn1.getText().equals(btn7.getText())){
            isWinner("Player 2", "p2");
            coloraCaselleVincenti(btn1,btn4,btn7);
        }
        //terza colonna
        else if(btn2.getText().equals(segnoP1) && btn2.getText().equals(btn5.getText()) && btn2.getText().equals(btn8.getText())){
            isWinner("Player 1", "p1");
            coloraCaselleVincenti(btn2,btn5,btn8);
        }
        else if(btn2.getText().equals(segnoP2) && btn2.getText().equals(btn5.getText()) && btn2.getText().equals(btn8.getText())){
            isWinner("Player 2", "p2");
            coloraCaselleVincenti(btn2,btn5,btn8);
        }
        //prima diagonale
        else if(btn0.getText().equals(segnoP1) && btn0.getText().equals(btn4.getText()) && btn0.getText().equals(btn8.getText())){
            isWinner("Player 1", "p1");
            coloraCaselleVincenti(btn0,btn4,btn8);
        }
        else if(btn0.getText().equals(segnoP2) && btn0.getText().equals(btn4.getText()) && btn0.getText().equals(btn8.getText())){
            isWinner("Player 2", "p2");
            coloraCaselleVincenti(btn0,btn4,btn8);
        }
        //seconda diagonale
        else if(btn2.getText().equals(segnoP1) && btn2.getText().equals(btn4.getText()) && btn2.getText().equals(btn6.getText())){
            isWinner("Player 1", "p1");
            coloraCaselleVincenti(btn2,btn4,btn6);
        }
        else if(btn2.getText().equals(segnoP2) && btn2.getText().equals(btn4.getText()) && btn2.getText().equals(btn6.getText())){
            isWinner("Player 2", "p2");
            coloraCaselleVincenti(btn2,btn4,btn6);
        }
        //in caso di pareggio
        else if(isLast){
            Toast.makeText(MultiPlayer.this, "Pareggio!", Toast.LENGTH_LONG).show();
            setClickable(false);
            setUser("pp");
        }

    }

    public void coloraCaselleVincenti(Button b1, Button b2, Button b3){
        b1.setTextColor(Color.GRAY);
        b2.setTextColor(Color.GRAY);
        b3.setTextColor(Color.GRAY);
    }

    public void isWinner(String player, String p){
        Toast.makeText(MultiPlayer.this, player+" is the winner!", Toast.LENGTH_LONG).show();
        setClickable(false);
        setUser(p);
    }


    public void setUser(final String winner) {

    //prima faccio il get, incremento e poi faccio il set (dentro
        DocumentReference docRefGet = db.collection("users").document(auth.getUid());
        docRefGet.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("TAG", "DocumentSnapshot data: " + document.getData() );

                        String nome = (String)document.getData().get("nome");
                        int perse=0;
                        int vinte = 0;
                        int pareggiate=0;
                        if(document.getData().get("perse")!=null)
                            perse = ((Long)document.getData().get("perse")).intValue();
                        if(document.getData().get("vinte")!=null)
                            vinte = ((Long)document.getData().get("vinte")).intValue();
                        if(document.getData().get("pareggiate")!=null)
                            pareggiate = ((Long)document.getData().get("pareggiate")).intValue();
                        Log.d("TAG", "Dentro getUser: vinte:"+vinte+" - perse:"+perse+" - pareggiate:"+pareggiate+" -- nome:"+nome);
                        //qui devo aggiungere in caso sia stata vinta, persa o pareggiata una partita!
                        if( winner.equals(player) ){
                            //aggiungo una vittoria
                            vinte += 1 ;
                        }else if(winner.equals("pp")){
                            //aggiungo un pareggio
                            pareggiate += 1 ;
                        }else{
                            //aggiungo una perdita
                            perse += 1 ;
                        }
                        Log.d("TAG", "Dopo l'incremento: vinte:"+vinte+" - perse:"+perse+" - pareggiate:"+pareggiate+" -- nome:"+nome);
                        HashMap<String, Object> newuser = new HashMap<>();
                        newuser.put("vinte", vinte);
                        newuser.put("perse", perse);
                        newuser.put("pareggiate", pareggiate);
                        newuser.put("nome", nome);
                        //faccio il set
                        db.collection("users").document(auth.getUid())
                                .set(newuser)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("SET", "DocumentSnapshot successfully written!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("TAG", "Error writing document", e);

                                    }
                                });


                        } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
        //Log.d("TAG", "Fuori getUser: vinte:"+partite.get(0)+" - perse:"+partite.get(1)+" - pareggiate:"+partite.get(2) + " -- ");

/////////////////////////////////////////////////////////////////////////////////////////////////////////


//se faccio il set il get non funziona
            /*
*/

    }



    @Override
    public void onBackPressed() {
// super.onBackPressed();
// Not calling **super**, disables back button in current screen.
    }

}
