package com.isw.movingout.Daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.isw.movingout.Objetos.ArticuloCuarto;

import java.util.ArrayList;

public class daoArticuloCuarto {
    SQLiteDatabase cx;
    ArrayList<ArticuloCuarto> lista = new ArrayList<ArticuloCuarto>();
    ArticuloCuarto articuloCuarto;
    Context contexto;
    int currentCuarto;
    String nombreBD = "BDArticulosCuarto";


    public daoArticuloCuarto(Context c, int cuartoID)
    {
        this.contexto = c;
        this.currentCuarto = cuartoID;
        cx = c.openOrCreateDatabase(nombreBD, Context.MODE_PRIVATE, null);
        cx.execSQL("create table if not exists articuloCuarto(id integer primary key autoincrement, nombre text, etiqueta text, etiqueta2 text, cuartoid integer)");

    }

    public boolean insertar(ArticuloCuarto articuloCuarto)
    {
        ContentValues contenedor = new ContentValues();
        contenedor.put("nombre", articuloCuarto.getNombre());
        contenedor.put("cuartoid", articuloCuarto.getCuartoID());
        return (cx.insert("articuloCuarto", null, contenedor)) > 0;
    }

    public boolean eliminar(int id)
    {
        return (cx.delete("articuloCuarto", "id="+id,null)) > 0;
    }

    public boolean editar(ArticuloCuarto a)
    {
        ContentValues contenedor = new ContentValues();
        contenedor.put("nombre", a.getNombre());
        return (cx.update("articuloCuarto", contenedor, "id="+a.getId(), null)) > 0;
    }

    public boolean asignarEtiqueta(ArticuloCuarto articuloCuarto1)
    {
        ContentValues contenedor = new ContentValues();
        contenedor.put("etiqueta", articuloCuarto1.getEtiqueta());
        return (cx.update("articuloCuarto", contenedor, "id="+articuloCuarto1.getId(), null)) > 0;
    }

    public boolean asignarEtiqueta2(ArticuloCuarto articuloCuarto2)
    {
        ContentValues contenedor = new ContentValues();
        contenedor.put("etiqueta2", articuloCuarto2.getEtiqueta2());
        return (cx.update("articuloCuarto" , contenedor, "id="+articuloCuarto2.getId(), null)) > 0;
    }

    public ArrayList<ArticuloCuarto> verTodos()
    {

        lista.clear();
        Cursor cursor = cx.rawQuery("select * from articuloCuarto where cuartoid ="+ currentCuarto, null);
        if (cursor != null && cursor.getCount()>0){
            cursor.moveToFirst();
            do{
                lista.add(new ArticuloCuarto(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getInt(4)));

            }while (cursor.moveToNext());
        }
        return lista;
    }

    public ArticuloCuarto verUno(int posicion)
    {
        Cursor cursor = cx.rawQuery("select * from articuloCuarto where cuartoid ="+ currentCuarto, null);
        cursor.moveToPosition(posicion);
        articuloCuarto = new ArticuloCuarto(cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getInt(4));
        return articuloCuarto;
    }
}
