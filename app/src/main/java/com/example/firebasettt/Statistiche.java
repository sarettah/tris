package com.example.firebasettt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class Statistiche extends AppCompatActivity {

    PieChartView pieChartView;
    TextView tv;
    MaterialButton back;
    int perse, vinte, pareggiate, totali;
    FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistiche);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        perse=0;
        vinte=0;
        pareggiate=0;
        totali=0;

        tv = findViewById(R.id.name_tv);
        back = findViewById(R.id.statistiche);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Statistiche.this, ChooseGame.class);
                startActivity(intent);
                //finish();
            }
        });


        //che lo faccia solo quando viene modificato il documento??
        DocumentReference docRef = db.collection("users").document(auth.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                        String nome="";
                        if(document.getData().get("nome")!=null)
                             nome = (String)document.getData().get("nome");
                        tv.setText("Welcome " + nome+"");
                        if(document.getData().get("perse")!=null)
                            perse = ((Long)document.getData().get("perse")).intValue();
                        if(document.getData().get("vinte")!=null)
                            vinte = ((Long)document.getData().get("vinte")).intValue();
                        if(document.getData().get("pareggiate")!=null)
                            pareggiate = ((Long)document.getData().get("pareggiate")).intValue();

                        totali=perse+vinte+pareggiate;

                       // Log.d("TAG", "DocumentSnapshot data: " + totali);
                        createPie();

                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });

        //Log.d("TAG", "DocumentSnapshot data: " + totali);


    }

    public void createPie(){
        pieChartView = findViewById(R.id.chart);

        List<SliceValue> pieData = new ArrayList<>();

        pieData.add(new SliceValue(perse, Color.RED).setLabel("Perse "+perse));
        pieData.add(new SliceValue(vinte, Color.BLUE).setLabel("Vinte "+vinte));
        pieData.add(new SliceValue(pareggiate, Color.GRAY).setLabel("Pareggiate "+pareggiate));

        PieChartData pieChartData = new PieChartData(pieData);

        pieChartData.setHasLabels(true).setValueLabelTextSize(16);
        //pieChartData.setHasCenterCircle(true).setCenterText1("Game played");
        pieChartData.setHasCenterCircle(true).setCenterText1("Totali: "+totali).setCenterText1FontSize(20).setCenterText1Color(Color.parseColor("#0097A7"));

        pieChartView.setPieChartData(pieChartData);
    }
}
