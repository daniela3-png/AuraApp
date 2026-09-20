package com.aura.aura.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.aura.aura.models.Ciclo;
import com.aura.aura.models.Usuario;
import net.sqlcipher.database.SupportFactory;

// Subimos la versión a 2 por los cambios en la entidad Usuario
@Database(entities = {Usuario.class, Ciclo.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UsuarioDao usuarioDao();
    public abstract CicloDao cicloDao();

    private static volatile AppDatabase INSTANCIA;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCIA == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCIA == null) {

                    // Clave de encriptación para SQLCipher
                    // (En un entorno de producción avanzado, esta clave se genera y guarda en el Android Keystore)
                    final byte[] passphrase = "AuraSecure_Key_2026!".getBytes();
                    SupportFactory factory = new SupportFactory(passphrase);

                    INSTANCIA = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "aura_database")
                            .openHelperFactory(factory) // Inyectamos el motor de encriptación
                            .fallbackToDestructiveMigration() // Recrea la BD si hay cambios de versión
                            .build();
                }
            }
        }
        return INSTANCIA;
    }
}