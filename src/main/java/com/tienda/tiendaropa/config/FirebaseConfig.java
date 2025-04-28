package com.tienda.tiendaropa.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initialize() {
        try {
            InputStream serviceAccount = getClass().getClassLoader()
                    .getResourceAsStream("firebase/tiendaropa-3f787-firebase-adminsdk-fbsvc-1e4978e6f3.json"); // <-- aquí tu archivo real

            if (serviceAccount == null) {
                throw new IOException("No se encontró el archivo de Firebase en la ruta especificada.");
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) { // Importante para evitar errores si se inicializa más de una vez
                FirebaseApp.initializeApp(options);
                System.out.println("✅ Firebase inicializado correctamente.");
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("❌ Error inicializando Firebase: " + e.getMessage());
        }
    }
}
