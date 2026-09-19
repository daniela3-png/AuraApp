package com.aura.aura.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.aura.aura.models.Ciclo;
import com.aura.aura.models.Usuario;

// Agregamos Ciclo.class a las entidades
@Database(entities = {Usuario.class, Ciclo.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UsuarioDao usuarioDao();
    public abstract CicloDao cicloDao(); // Nuevo DAO agregado

    private static volatile AppDatabase INSTANCIA;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCIA == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCIA == null) {
                    INSTANCIA = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "aura_database")
                            .build();
                }
            }
        }
        return INSTANCIA;
    }
}