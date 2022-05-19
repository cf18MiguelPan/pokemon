package com.example.pokemon;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class pokedex extends AppCompatActivity{

    public static Activity act;
    public static TextView txtDisplay;
    public static ImageView imgPok;
    public int iterador;

    public String [] ListaTipo;             //Lista de tipos
    public String BuscaTipo;                //Buscar tipo en lista de tipos

    Button Siguiente;
    Button Anterior;
    Button Inicio;
    Button Final;

    ImageButton MeGusta;

    public static ImageView [] imgType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokedex);

        ListaTipo = getResources().getStringArray(R.array.tipos);

        act = this;
        imgType = new ImageView[2];

        txtDisplay = findViewById(R.id.txtDisplay);
        imgPok = findViewById(R.id.imgPok);
        imgType[0] = findViewById(R.id.imgType0);
        imgType[1] = findViewById(R.id.imgType1);

        Siguiente=findViewById(R.id.btnRight);
        Anterior=findViewById(R.id.btnLeft);
        Inicio=findViewById(R.id.btnInicio);
        Final=findViewById(R.id.btnFinal);

        MeGusta=findViewById(R.id.btnLike);

        MeGusta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        //Mostrar los pokemon a partir del inicio 1>>>>>898
        Inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.example.pokemon.fetchData process = new com.example.pokemon.fetchData("1");
                iterador = 1;
                process.execute();
            }
        });

        //Mostrar los pokemon a partir del final 898>>>>>1
        Final.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.example.pokemon.fetchData process = new com.example.pokemon.fetchData("898");
                iterador = 898;
                process.execute();
            }
        });

        Siguiente.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (iterador >= 898) {
                    iterador = 898;
                    String pokSearch = String.valueOf(iterador);
                    com.example.pokemon.fetchData process = new com.example.pokemon.fetchData(pokSearch);
                    process.execute();
                } else {
                    iterador++;
                    String pokSearch = String.valueOf(iterador);
                    com.example.pokemon.fetchData process = new com.example.pokemon.fetchData(pokSearch);
                    process.execute();
                }
            }
        });
        Anterior.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (iterador <= 1) {
                    iterador = 1;
                    String pokSearch = String.valueOf(iterador);
                    com.example.pokemon.fetchData process = new com.example.pokemon.fetchData(pokSearch);
                    process.execute();
                } else {
                    iterador--;
                    String pokSearch = String.valueOf(iterador);
                    com.example.pokemon.fetchData process = new com.example.pokemon.fetchData(pokSearch);
                    process.execute();
                }
            }
        });

        Button btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showTxtSearch();
            }
        });

        com.example.pokemon.fetchData process = new com.example.pokemon.fetchData("1");
        process.execute();
    }

    public void showTxtSearch(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Search a Pokemon");

        final EditText input = new EditText(this);
        input.setHint("Pokemon");
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String pokSearch = input.getText().toString();
                com.example.pokemon.fetchData process = new com.example.pokemon.fetchData(pokSearch);
                process.execute();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert.show();
    }




}