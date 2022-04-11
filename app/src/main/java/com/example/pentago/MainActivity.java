package com.example.pentago;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText name1, name2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.name1 = (EditText) findViewById(R.id.name1);
        this.name2 = (EditText) findViewById(R.id.name2);

    }

    public void ButtonPlay(View view) {
        Toast.makeText(this, "Play!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, GameUI.class);
        String name1Text = this.name1.getText().toString();
        String name2Text= this.name2.getText().toString();
        intent.putExtra("name1",name1Text);
        intent.putExtra("name2", name2Text);
        this.startActivity(intent);
    }


}