package com.okakiben.hilos_persistencia_sonido;

/**
 * Created by german on 2-5-17.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * Created by Juan on 23/02/2017.
 */

public class Partida extends ImageView {

    private int acel;
    private Bitmap pelota, fondo;
    private int tam_pantX, tam_pantY, posX, posY, velX, velY;
    private int tamPelota;
    boolean pelota_sube;

    public Partida(Context contexto, int nivel_dificultad){

        super(contexto);

        WindowManager manejador_ventana=(WindowManager) contexto.getSystemService(Context.WINDOW_SERVICE);

        Display pantalla=manejador_ventana.getDefaultDisplay();

        Point maneja_coord=new Point(); //integrar dos coordenadas X e Y

        pantalla.getSize(maneja_coord); // getSize determina el tamaño de la pantalla donde se ejecuta la app

        tam_pantX=maneja_coord.x;

        tam_pantY=maneja_coord.y;

        //Esto que viene abajo construye un leyaut prográmatico (grafica, mediante codigo)

        BitmapDrawable dibujo_fondo=(BitmapDrawable) ContextCompat.getDrawable(contexto, R.drawable.paisaje_1);

        fondo=dibujo_fondo.getBitmap();// mirar en api getBitmap en clase BitmapDrawable. esto nos lleva a la siguiente instr.

        fondo=Bitmap.createScaledBitmap(fondo, tam_pantX, tam_pantY, false);//mirar en clase Bitmap


        //Dibuja pelota
        BitmapDrawable objetoPelota=(BitmapDrawable)ContextCompat.getDrawable(contexto, R.drawable.pelota_1);

        pelota=objetoPelota.getBitmap();

        tamPelota=tam_pantY/3; //este calculo es el determinante para el nivel de dificultad

        pelota=Bitmap.createScaledBitmap(pelota, tamPelota, tamPelota, false);

        posX=tam_pantX/2-tamPelota/2;

        posY=0-tamPelota;

        acel=nivel_dificultad*(maneja_coord.y/400);


    }

    public boolean toque(int x, int y){

        if(y<tam_pantY/3) return false; //esta primera instruccion invalida el primer tercio de la pantalla

        if(velY<=0) return false; //si la pelota esta parada, el metodo no tiene que funcionar "false"

        if(x<posX || x> posX+tamPelota) return false; //si toco al rededor de la pelota nada deberia pasar

        if(y<posY || y>posY+tamPelota) return false;  //si toco al rededor de la pelota nada deberia pasar

        velY=-velY;

        double desplX=x-(posX+tamPelota/2); //desplaza la pelota horizontalmente

        desplX=desplX/(tamPelota/2)*velY/2; //desplaza la pelota verticalmente

        velX+=(int)desplX;

        return true;
    }


    public boolean movimientoBola(){

        if(posX<0-tamPelota){

            posY=0-tamPelota;

            velY=acel;
        }

        posX+=velX;

        posY+=velY;

        if(posY>=tam_pantY) return true;

        if(posX+tamPelota<0 || posX>tam_pantX) return true;

        if(velY<0) pelota_sube=true;

        if(velY>0 && pelota_sube){

            pelota_sube=false;

        }

        velY+=acel;

        return false;
    }

    protected void onDraw(Canvas lienzo){

        lienzo.drawBitmap(fondo, 0,0, null);

        lienzo.drawBitmap(pelota, posX, posY, null);


    }
}
