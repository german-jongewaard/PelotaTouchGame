package com.okakiben.hilos_persistencia_sonido;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    int record;

    MediaPlayer perdiopartida;

    MediaPlayer ganopartida;

    MediaPlayer principio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        perdiopartida = MediaPlayer.create(this, R.raw.perdiofinal);

        ganopartida = MediaPlayer.create(this, R.raw.ganopartida);

        principio = MediaPlayer.create(this, R.raw.principiojuego);

        principio.start();
    }




    //El onClick llamara a este Método!!
    public void ayuda(View vista){
        //el metodo crea una intención
        Intent intencion = new Intent(this,AyudaActividad.class);
        //el metodo arranca la intencion!
        startActivity(intencion);

    }

    public void dificultad(View vista){

        String dific = (String) ((Button) vista).getText(); //con getText obtengo el nombre del boton
                                            // y se lo paso a dific para saber que boton pulse

        int dificultad = 1;

        if(dific.equals(getString(R.string.medio))) dificultad = 2;

        if(dific.equals(getString(R.string.dificil))) dificultad = 3;

        Intent in = new Intent(this, Gestion.class);

        in.putExtra("DIFICULTAD", dificultad);

        principio.stop();

        startActivityForResult(in, 1);

        //startActivity(in);
    }

    protected void onActivityResult(int peticion, int codigo, Intent puntuacion){

        if(peticion != 1 || codigo != RESULT_OK) return;

        int resultado = puntuacion.getIntExtra("PUNTUACION", 0);


        if(resultado>record){

            record = resultado;

            TextView caja = (TextView)findViewById(R.id.record);

            caja.setText(" Record: " + record + " ");

            ganopartida.start();

            guardarecord();

        }else {

            String puntuacionpartida = "Lo siento has realizado: " + resultado + " toques.";

            perdiopartida.start();

            Toast mitoast = Toast.makeText(this, puntuacionpartida, Toast.LENGTH_LONG);

            mitoast.setGravity(Gravity.CENTER,0,0);

            mitoast.show();
        }

    }

    public void onResume(){

        super.onResume();

        leerecord();

    }

    private void guardarecord(){

        SharedPreferences datos = PreferenceManager.getDefaultSharedPreferences(this);

        SharedPreferences.Editor mieditor = datos.edit();

        mieditor.putInt("RECORD", record);

        mieditor.apply();  //con este metodo consigo guardar el record
    }

    public void reset (View vista){

        record = 0;

        TextView caja = (TextView)findViewById(R.id.record);

        caja.setText("Record: " + record);

        guardarecord();

    }

    private void leerecord(){

        SharedPreferences datos = PreferenceManager.getDefaultSharedPreferences(this);

        record = datos.getInt("RECORD", 0);

        TextView caja = (TextView)findViewById(R.id.record);

        caja.setText("Record: " + record);

    }

}
