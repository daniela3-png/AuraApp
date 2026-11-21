package com.aura.aura.database;

import androidx.annotation.NonNull;
import com.aura.aura.models.Ciclo;
import com.aura.aura.models.CitaMedica;
import com.aura.aura.models.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class FirebaseRepository {

    private final DatabaseReference rootRef;

    // Interfaces de callback para comunicar resultados o errores a la interfaz
    public interface OperacionCallback {
        void onExito(String mensaje);
        void onError(String error);
    }

    public interface ListaCiclosCallback {
        void onDatosCargados(List<Ciclo> ciclos);
        void onError(String error);
    }

    public interface ListaCitasCallback {
        void onDatosCargados(List<CitaMedica> citas);
        void onError(String error);
    }

    public FirebaseRepository() {
        // Obtiene la instancia configurada en google-services.json
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        this.rootRef = database.getReference("aura_app");
    }

    // 1. REGISTRAR / SINCRONIZAR USUARIO EN FIREBASE
    public void guardarUsuario(Usuario usuario, OperacionCallback callback) {
        String key = String.valueOf(usuario.id_usuario);
        rootRef.child("usuarios").child(key).setValue(usuario)
                .addOnSuccessListener(unused -> {
                    if (callback != null) callback.onExito("Usuario sincronizado en la nube");
                })
                .addOnFailureListener(e -> {
                    if (callback != null) callback.onError("Error al guardar usuario: " + e.getMessage());
                });
    }

    // 2. REGISTRAR CICLO MENSTRUAL EN FIREBASE (CREATE)
    public void registrarCiclo(Ciclo ciclo, OperacionCallback callback) {
        DatabaseReference ciclosRef = rootRef.child("ciclos").child(String.valueOf(ciclo.id_usuario));
        String key = ciclosRef.push().getKey(); // Genera clave única automática

        if (key != null) {
            ciclosRef.child(key).setValue(ciclo)
                    .addOnSuccessListener(unused -> {
                        if (callback != null) callback.onExito("Ciclo respaldado en Firebase");
                    })
                    .addOnFailureListener(e -> {
                        if (callback != null) callback.onError("Error de red al guardar ciclo: " + e.getMessage());
                    });
        }
    }

    // 3. CONSULTAR Y ESCUCHAR CICLOS EN TIEMPO REAL (READ)
    public void escucharCiclosUsuario(int idUsuario, ListaCiclosCallback callback) {
        DatabaseReference ciclosRef = rootRef.child("ciclos").child(String.valueOf(idUsuario));

        ciclosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Ciclo> lista = new ArrayList<>();
                for (DataSnapshot dato : snapshot.getChildren()) {
                    Ciclo ciclo = dato.getValue(Ciclo.class);
                    if (ciclo != null) {
                        lista.add(ciclo);
                    }
                }
                if (callback != null) callback.onDatosCargados(lista);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (callback != null) callback.onError("Error al leer ciclos: " + error.getMessage());
            }
        });
    }

    // 4. REGISTRAR CITA MÉDICA EN FIREBASE (CREATE)
    public void registrarCita(CitaMedica cita, OperacionCallback callback) {
        DatabaseReference citasRef = rootRef.child("citas").child(String.valueOf(cita.id_usuario));
        String key = citasRef.push().getKey();

        if (key != null) {
            citasRef.child(key).setValue(cita)
                    .addOnSuccessListener(unused -> {
                        if (callback != null) callback.onExito("Cita agendada en Firebase");
                    })
                    .addOnFailureListener(e -> {
                        if (callback != null) callback.onError("Error de red al agendar cita: " + e.getMessage());
                    });
        }
    }

    // 5. CONSULTAR Y ESCUCHAR CITAS MÉDICAS EN TIEMPO REAL (READ)
    public void escucharCitasUsuario(int idUsuario, ListaCitasCallback callback) {
        DatabaseReference citasRef = rootRef.child("citas").child(String.valueOf(idUsuario));

        citasRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<CitaMedica> lista = new ArrayList<>();
                for (DataSnapshot dato : snapshot.getChildren()) {
                    CitaMedica cita = dato.getValue(CitaMedica.class);
                    if (cita != null) {
                        lista.add(cita);
                    }
                }
                if (callback != null) callback.onDatosCargados(lista);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (callback != null) callback.onError("Error al leer citas: " + error.getMessage());
            }
        });
    }
}