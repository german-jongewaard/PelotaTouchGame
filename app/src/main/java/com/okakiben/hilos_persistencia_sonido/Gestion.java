package com.okakiben.hilos_persistencia_sonido;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

public class Gestion extends AppCompatActivity {

    private Partida partida;

    private int dificultad;

    private  int FPS = 30;

    private Handler temporizador;

    private int botes;

    MediaPlayer golpeo;

    MediaPlayer parque;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        botes = 0;

        setContentView(R.layout.activity_gestion);

        Bundle extras = getIntent().getExtras();

        dificultad = extras.getInt("DIFICULTAD");

        //aqui comienza la partida
        partida = new Partida(getApplicationContext(),dificultad); //(instancio la clase Partida)

        //aqui cargo la partida
        setContentView(partida);

        temporizador=new Handler();

        temporizador.postDelayed(elhilo,2000);

        golpeo = MediaPlayer.create(this, R.raw.golpeobalon);

        parque = MediaPlayer.create(this, R.raw.park);

    }

    private Runnable elhilo = new Runnable() {
        @Override
        public void run() {

            if(partida.movimientoBola()) {

                fin();

                parque.stop();

            }else{

                partida.invalidate(); //elimina el contenido de ImageView y llama denuevo a onDraw
                                    // (borra la imagen de la Bola anterior mientra esta se mueve)

                parque.start();

                //con el temporizador determino la velocidad de borrado y pintado de imagen
                temporizador.postDelayed(elhilo, 1000/FPS);

            }
        }
    };


    public boolean onTouchEvent(MotionEvent evento){

        //le hago un casting para transformarlo en entero, porque no devuelve entero sino que boolean.
        int x = (int)evento.getX();

        int y = (int)evento.getY();

        /* Con esto  "x e y" (de arriba) ya tengo capturado en esta variable el punto
            exacto donde el usuario pulso en la pantalla*/


        golpeo.start();

        //  partida.toque(x,y);  aqui esta el punto concreto donde el usuario pulso en la pantalla

        if(partida.toque(x,y)) botes++;  //aqui cuento los toques que le da el usuario a la pelota

        return false;

    }


    public void fin(){
        //aqui limpio el temp y elhilo
        temporizador.removeCallbacks(elhilo);

        Intent in = new Intent(); //aqui creo el Bundle que vuelve al MainActivity.java

        in.putExtra("PUNTUACION", botes*dificultad);



        setResult(RESULT_OK, in);

        finish(); //destruye la actividad actual

    }

}
