package com.example.ejercicio_1.repos.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.ejercicio_1.model.Actividad;
import com.example.ejercicio_1.repos.room.dao.ActividadDao;

@Database(entities = {Actividad.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ActividadDao actividadDao();
}

