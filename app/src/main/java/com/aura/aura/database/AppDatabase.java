package com.aura.aura.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.aura.aura.models.Usuario;
import com.aura.aura.models.Ciclo;
import com.aura.aura.models.CitaMedica; // Importamos la nueva tabla

// Agregamos CitaMedica.class y subimos la versión a 3
@Database(entities = {Usuario.class, Ciclo.class, CitaMedica.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UsuarioDao usuarioDao();
    public abstract CicloDao cicloDao();
    public abstract CitaMedicaDao citaMedicaDao(); // Exponemos el DAO de citas

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "aura_database")
                            .fallbackToDestructiveMigration() // Al subir a versión 3, limpiará la BD de nuevo
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}