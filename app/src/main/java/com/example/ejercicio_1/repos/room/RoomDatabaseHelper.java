package com.example.ejercicio_1.repos.room;

import android.content.Context;

import androidx.room.Room;

import com.example.ejercicio_1.model.Actividad;
import com.example.ejercicio_1.repos.room.dao.ActividadDao;

import java.util.List;

public class RoomDatabaseHelper {

    private AppDatabase database;

    public RoomDatabaseHelper(Context context) {
        database = Room.databaseBuilder(context, AppDatabase.class, "app_database")
                .fallbackToDestructiveMigration()
                .build();
    }

    public List<Actividad> getTasksByDay(String day) {
        return database.actividadDao().getByDay(day);
    }

    public Actividad getCurrentTask(String hora) {
        return database.actividadDao().getCurrentTask(hora);
    }

    public void saveActivity(Actividad actividad) {
        new Thread(() -> database.actividadDao().insert(actividad)).start();
    }

    // Nuevo método para obtener todas las actividades
    public List<Actividad> getAllActivities() {
        return database.actividadDao().getAll();
    }
}
