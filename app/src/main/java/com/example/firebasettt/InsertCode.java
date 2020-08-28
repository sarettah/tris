package com.example.firebasettt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InsertCode extends AppCompatActivity {

    Button btn, home;
    EditText ed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_code);

        btn = findViewById(R.id.register);
        home = findViewById(R.id.home);
        ed = findViewById(R.id.ed_p2);

        //apro la tastiera numerica all'apertura dell'activity
        boolean checkFocus = ed.requestFocus();
        //Log.i("CheckFocus", ""+checkFocus);
        if(checkFocus==true)
        {
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            ed.setInputType(InputType.TYPE_CLASS_NUMBER);
            mgr.showSoftInput(ed, InputMethodManager.SHOW_IMPLICIT);
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codeS = ed.getText().toString();
                if(codeS.isEmpty() || codeS == null){
                    Toast.makeText(InsertCode.this, "Insert code", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(InsertCode.this, MultiPlayer.class);
                    intent.putExtra("CODICE", codeS);
                    startActivity(intent);
                    //finish();
                }
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(InsertCode.this, ChooseGame.class);
                    startActivity(intent);
                    //finish();

            }
        });


    }

    @Override
    public void onBackPressed() {
// super.onBackPressed();
// Not calling **super**, disables back button in current screen.
    }
}
