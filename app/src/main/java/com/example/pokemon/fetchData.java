package com.example.pokemon;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.net.URL;
import java.util.ArrayList;

public class fetchData extends AsyncTask<Void, Void, Void> {

    protected String data = "";
    protected String results = "";
    protected ArrayList<String> strTypes; // Create an ArrayList object
    protected String pokSearch;

    public fetchData(String pokSearch) {
        this.pokSearch = pokSearch;
        strTypes = new ArrayList<>();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            //Make API connection
            URL url = new URL("https://pokeapi.co/api/v2/pokemon/" + pokSearch);
            Log.i("logtest", "https://pokeapi.co/api/v2/pokemon/" + pokSearch);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            // Read API results
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sBuilder = new StringBuilder();

            // Build JSON String
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                sBuilder.append(line + "\n");
            }

            inputStream.close();
            data = sBuilder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid){
        JSONObject jObject = null;
        String img = "";
        String typeName = "";
        String typeObj="";

        try {
            jObject = new JSONObject(data);

            // Get JSON name, height, weight // Obtener la info del pokemon
            results += "Name: " + jObject.getString("name").toUpperCase() + "\n" +
                    "Height: " + jObject.getString("height") + "\n" +
                    "Weight: " + jObject.getString("weight");

            // Get type/types
            JSONArray types = new JSONArray(jObject.getString("types"));
            for(int i=0; i<types.length(); i++){
                JSONObject type = new JSONObject(types.getString(i));
                JSONObject type2  = new JSONObject(type.getString("type"));
                strTypes.add(type2.getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Set info
        pokedex.txtDisplay.setText(this.results);           //Info del pokemon

        Glide.with(pokedex.act)             //activity
                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/"+pokSearch+".png")     //Cargar en la url la foto
                .into(pokedex.imgPok);//establecer la foto en el imgview llamado:imgPok

        // For que recorre la cantidad de tipo que representa el pokémon
        for(int i=0; i<strTypes.size(); i++){
            //En caso de que sale sólo 1 tipo
            pokedex.imgType[0].setImageResource(pokedex.act.getResources().getIdentifier(strTypes.get(0), "drawable", pokedex.act.getPackageName()));

            //En caso de que sale 2 tipos
            if(strTypes.size()>1){
                pokedex.imgType[1].setImageResource(pokedex.act.getResources().getIdentifier(strTypes.get(1), "drawable", pokedex.act.getPackageName()));
            }else{
                //Cambia a color blanco el fondo al haber un tipo residual
                pokedex.imgType[1].setImageResource(R.drawable.blanco);
            }

        }

    }

}