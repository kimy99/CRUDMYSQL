package com.example.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

public class List_View extends AppCompatActivity {
    ListView listViewPersonas;
    ArrayAdapter adaptador;
    SearchView searchView;

    ListView listView;
    ArrayList<String> list;
    ArrayAdapter adapter;
    String [] version =
            {"Aestro","Blender","CupCake","Eclair","GingerBread","HoneyComb",
                    "Jelly Bean","KitKat","Lollipop","MarshMalow","Nougat","Oreo"};

    AdminSQLiteOpenHelper conexion = new AdminSQLiteOpenHelper(this);
    Dto datos = new Dto();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list__view);

        listViewPersonas = (ListView)findViewById(R.id.listViewPersonas);
        searchView = (SearchView)findViewById(R.id.searchView);
/*
        adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1, conexion.consultaListaArticulos1());
        listViewPersonas.setAdapter(adaptador);

        */


    list = new ArrayList<>();
    for (int i=0; i<version.length; i++){
        list.add(version[i]);
    }

        adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        listViewPersonas.setAdapter(adaptador);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                String text = s;
                adaptador.getFilter().filter(text);
                return false;
            }
        });

        listViewPersonas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long l) {
//Recordar que en esta parte adelante de +conexion llevaba comillas pero
                //no se las puse porque me daba error y debia preguntar al profe
                String informacion="Codigo:"
                        +conexion.consultaListaArticulos().get(pos).getCodigo()+"\n";
                informacion+="Descripcion:"
                        +conexion.consultaListaArticulos().get(pos).getDescripcion()+"\n";
                informacion+="Precio:"
                        +conexion.consultaListaArticulos().get(pos).getPrecio();

                Dto articulos = conexion.consultaListaArticulos().get(pos);
                Intent intent = new Intent(List_View.this, DetalleArticulos.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("articulo", articulos);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });




    }
}
