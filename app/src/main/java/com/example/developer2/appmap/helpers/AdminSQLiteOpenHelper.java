package com.example.developer2.appmap.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {

    public AdminSQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //aquí creamos la tabla de usuario (idPersona, lalitud, longitud, fecha, idGrupo)
        db.execSQL("create table usuario(idPersona integer primary key, latitud double, longitud double, fecha date, idGrupo integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists usuario");

        db.execSQL("create table usuario(idPersona integer primary key, latitud double, longitud double, fecha date, idGrupo integer)");
    }
}
