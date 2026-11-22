package com.aura.aura.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.aura.aura.models.Ciclo;
import com.aura.aura.models.CitaMedica;
import com.aura.aura.models.Usuario;

import net.sqlcipher.database.SupportFactory;

// Subimos la versión a 3 por la integración de CitaMedica y cambios en Usuario
@Database(entities = {Usuario.class, Ciclo.class, CitaMedica.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UsuarioDao usuarioDao();
    public abstract CicloDao cicloDao();
    public abstract CitaMedicaDao citaMedicaDao();

    private static volatile AppDatabase INSTANCIA;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCIA == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCIA == null) {

                    // Clave de encriptación para SQLCipher
                    final byte[] passphrase = "AuraSecure_Key_2026!".getBytes();
                    SupportFactory factory = new SupportFactory(passphrase);

                    INSTANCIA = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "aura_db_v3") // <--- AQUÍ ESTÁ EL CAMBIO
                            .openHelperFactory(factory)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCIA;
    }
}