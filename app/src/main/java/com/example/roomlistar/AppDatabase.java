package com.example.roomlistar;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {LocationEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    // Declaración del DAO
    public abstract LocationDao locationDao();

    // Instancia única de la base de datos
    private static volatile AppDatabase INSTANCE;

    // Método para obtener la instancia de la base de datos
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "app_database")
                            .fallbackToDestructiveMigration() // Elimina esta línea si deseas manejar migraciones
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
