package com.example.sqlite;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import javax.xml.transform.dom.DOMLocator;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {
    boolean estadoDelete = true;

    ArrayList<String> listaArticulos;
    ArrayList<Dto> articulosList;

    /*
    public AdminSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }*/

    public AdminSQLiteOpenHelper(Context context) {
        super(context, "administracion.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table articulos (codigo integer not null primary key, descripcion text, precio real)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists articulos");
        onCreate(db);

    }

    public SQLiteDatabase bd() {
        SQLiteDatabase bd = this.getWritableDatabase();
        return bd;

    }

    public boolean InserTradicional(Dto datos) {
        boolean estado = true;

        try {
            int codigo = datos.getCodigo();
            String descripcion = datos.getDescripcion();
            double precio = datos.getPrecio();

            Cursor fila = bd().rawQuery("select codigo from articulos where codigo='" + codigo + "'", null);
            if (fila.moveToFirst() == true) {
                estado = false;
            } else {
                String SQL = "INSERT INTO articulos \n" +
                        "(codigo,descripcion,precio) \n" +
                        "VALUES \n" +
                        "('" + String.valueOf(codigo) + "', '" + descripcion + "', '" + String.valueOf(precio) + "');";

                bd().execSQL(SQL);
                bd().close();

                estado = true;
            }
        } catch (Exception e) {
            estado = false;
            Log.e("error.", e.toString());
        }
        return estado;
    }


    /*
    public boolean insertardatos(Dto datos) {
        boolean estado = true;
        int resultado;
    }

    */

    public boolean consultarCodigo(Dto datos){
        boolean estado = true;
        int resultado;
        SQLiteDatabase bd = this.getWritableDatabase();
        try {
            int codigo = datos.getCodigo();
            Cursor fila = bd.rawQuery("select codigo, descripcion, precio from articulos where codigo=" + codigo, null);
                    if (fila.moveToFirst()){
                        datos.setCodigo(Integer.parseInt(fila.getString(0)));
                        datos.setDescripcion(fila.getString(1));
                        datos.setPrecio(Double.parseDouble(fila.getString(2)));
                        estado = true;
                    }else{
                        estado = false;
                    }
                    bd.close();
        }catch (Exception e){
            estado = false;
            Log.e("error.", e.toString());
        }
        return estado;
    }

    public boolean consultarDescripcion (Dto datos){
        boolean estado;
        int resultado;
        SQLiteDatabase bd = this.getWritableDatabase();
        try {
            String descripcion = datos.getDescripcion();
            Cursor fila = bd.rawQuery("select codigo, descripcion, precio from articulos where descripcion='" + descripcion + "'", null);
            if (fila.moveToFirst()){
                datos.setCodigo(Integer.parseInt(fila.getString(0)));
                datos.setDescripcion(fila.getString(1));
                datos.setPrecio(Double.parseDouble(fila.getString(2)));
                estado = true;
            }else{
                estado = false;
            }
            bd.close();
        }catch (Exception e){
            estado = false;
            Log.e("error.", e.toString());
        }
        return estado;
    }

    public boolean bajaCodigo (final Context context ,final Dto datos){
        estadoDelete = true;
        try{
            int codigo = datos.getCodigo();
            Cursor fila = bd().rawQuery("select * from articulos where codigo=" + codigo, null);
            if (fila.moveToFirst()){
                datos.setCodigo(Integer.parseInt(fila.getString(0)));
                datos.setDescripcion(fila.getString(1));
                datos.setPrecio(Double.parseDouble(fila.getString(2)));
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.ic_delete);
                builder.setTitle("Warning");
                builder.setMessage("¿Esta seguro de borrar el registro? \nCódigo: " + datos.getCodigo()+"\nDescripción: "+datos.getDescripcion());
               builder.setCancelable(false);
               builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       int codigo = datos.getCodigo();
                       int cant = bd().delete("articulos", "codigo=" + codigo, null);
                       if (cant > 0) {
                           estadoDelete = true;
                           Toast.makeText(context, "Registro eliminado satisfactoriamente.", Toast.LENGTH_SHORT).show();
                       }else {
                           estadoDelete = false;
                       }
                       bd().close();
                   }
               });
               builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {

                   }
               });
               AlertDialog dialog = builder.create();
               dialog.show();
            }else{
                Toast.makeText(context, "No hay resultados encontrados para la búsqueda específica", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            estadoDelete = false;
            Log.e("Error.", e.toString());
        }
        return estadoDelete;
    }

    public boolean modificar (Dto datos){
        boolean estado = true;
        int resultado;
        SQLiteDatabase bd = this.getWritableDatabase();
        try{
            int codigo = datos.getCodigo();
            String descripcion = datos.getDescripcion();
            double precio = datos.getPrecio();

            ContentValues registro = new ContentValues();
            registro.put("codigo", codigo);
            registro.put("descripcion", descripcion);
            registro.put("precio", precio);

            int cant = (int) bd.update("articulos", registro, "codigo=" + codigo, null);

            bd.close();
            if (cant>0) estado = true;
            else estado = false;
        }catch (Exception e){
            estado = false;
            Log.e("error.", e.toString());
        }
        return estado;
    }

    public ArrayList<Dto> consultaListaArticulos(){
        boolean estado = false;
        SQLiteDatabase bd = this.getWritableDatabase();

        Dto articulos = null;
        articulosList = new ArrayList<Dto>();

        try{
            Cursor fila = bd().rawQuery("select * from articulos", null);
            while(fila.moveToNext()){
                articulos = new Dto();
                articulos.setCodigo(fila.getInt(0));
                articulos.setDescripcion(fila.getString(1));
                articulos.setPrecio(fila.getDouble(2));

                articulosList.add(articulos);

                Log.i("codigo", String.valueOf(articulos.getCodigo()));
                Log.i("descripcion", articulos.getDescripcion().toString());
                Log.i("precio", String.valueOf(articulos.getPrecio()));
            }
            obtenerListaArticulos();
        }catch (Exception e){
        }
        return articulosList;
    }

    public ArrayList<String> obtenerListaArticulos(){
        listaArticulos = new ArrayList<String>();
        listaArticulos.add("seleccione");

        for(int i=0; i<articulosList.size(); i++){
            listaArticulos.add(articulosList.get(i).getCodigo()+" ~ "+articulosList.get(i).getDescripcion());
        }
        return listaArticulos;
    }

    public ArrayList<String> consultaListaArticulos1(){
        boolean estado = false;

        SQLiteDatabase bd = this.getWritableDatabase();

        Dto articulos = null;
        articulosList = new ArrayList<Dto>();

        try{
            Cursor fila = bd.rawQuery("select * from articulos", null);
            while(fila.moveToNext()){
                articulos = new Dto();
                articulos.setCodigo(fila.getInt(0));
                articulos.setDescripcion(fila.getString(1));
                articulos.setPrecio(fila.getDouble(2));
                articulosList.add(articulos);
            }
            listaArticulos = new ArrayList<String>();

            for (int i=0; i<=articulosList.size(); i++){
                listaArticulos.add(articulosList.get(i).getCodigo()+" ~ "+articulosList.get(i).getDescripcion());
            }
        }catch (Exception e){

        }
        return listaArticulos;
    }
}