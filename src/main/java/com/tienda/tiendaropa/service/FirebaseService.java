package com.tienda.tiendaropa.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.tienda.tiendaropa.model.*;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class FirebaseService {

    // Nombres de las colecciones en Firebase
    private static final String COLLECTION_CLIENTES = "clientes";
    private static final String COLLECTION_PRODUCTOS = "productos";
    private static final String COLLECTION_BOLETAS = "boletas";

    // Guardar cliente, generando un nuevo ID automático si ya existe
    public String saveCliente(Cliente cliente) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
    
        String baseId = cliente.getId();
        String newId = baseId;
        int counter = 1;
    
        // Buscar ID disponible
        while (true) {
            DocumentSnapshot document = db.collection(COLLECTION_CLIENTES).document(newId).get().get();
            if (!document.exists()) {
                break;
            }
            counter++;
            newId = baseId.replaceAll("\\d+$", "") + String.format("%03d", counter);
        }
    
        cliente.setId(newId);
        ApiFuture<WriteResult> collectionApiFuture = db.collection(COLLECTION_CLIENTES).document(newId).set(cliente);
        return "Cliente guardado con ID: " + newId + " en " + collectionApiFuture.get().getUpdateTime();
    }
    
    // Guardar producto, generando un nuevo ID automático si ya existe
    public String saveProducto(Producto producto) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
    
        String baseId = producto.getId();
        String newId = baseId;
        int counter = 1;
    
        while (true) {
            DocumentSnapshot document = db.collection(COLLECTION_PRODUCTOS).document(newId).get().get();
            if (!document.exists()) {
                break;
            }
            counter++;
            newId = baseId.replaceAll("\\d+$", "") + String.format("%03d", counter);
        }
    
        producto.setId(newId);
        ApiFuture<WriteResult> collectionApiFuture = db.collection(COLLECTION_PRODUCTOS).document(newId).set(producto);
        return "Producto guardado con ID: " + newId + " en " + collectionApiFuture.get().getUpdateTime();
    }

    // Guardar boleta, lanzando error si el ID ya existe
    public String saveBoleta(Boleta boleta) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        DocumentSnapshot document = db.collection(COLLECTION_BOLETAS).document(boleta.getId()).get().get();
        if (document.exists()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Boleta con este ID ya existe");
        }

        ApiFuture<WriteResult> collectionApiFuture = db.collection(COLLECTION_BOLETAS).document(boleta.getId()).set(boleta);
        return "Boleta guardada en: " + collectionApiFuture.get().getUpdateTime();
    }

    // Listar todos los clientes
    public List<QueryDocumentSnapshot> getAllClientes() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_CLIENTES).get();
        return future.get().getDocuments();
    }

    // Listar todos los productos
    public List<QueryDocumentSnapshot> getAllProductos() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_PRODUCTOS).get();
        return future.get().getDocuments();
    }

    // Listar todas las boletas
    public List<QueryDocumentSnapshot> getAllBoletas() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_BOLETAS).get();
        return future.get().getDocuments();
    }

    // Buscar cliente por ID
    public Cliente getClienteById(String id) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentSnapshot document = db.collection(COLLECTION_CLIENTES).document(id).get().get();
        if (document.exists()) {
            return document.toObject(Cliente.class);
        }
        return null;
    }

    // Buscar producto por ID
    public Producto getProductoById(String id) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentSnapshot document = db.collection(COLLECTION_PRODUCTOS).document(id).get().get();
        if (document.exists()) {
            return document.toObject(Producto.class);
        }
        return null;
    }

    // Buscar boleta por ID
    public Boleta getBoletaById(String id) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentSnapshot document = db.collection(COLLECTION_BOLETAS).document(id).get().get();
        if (document.exists()) {
            return document.toObject(Boleta.class);
        }
        return null;
    }

    // Buscar producto de forma simple (sin validaciones especiales)
    public Producto getProductoSimpleById(String id) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentSnapshot document = db.collection(COLLECTION_PRODUCTOS).document(id).get().get();
        if (document.exists()) {
            return document.toObject(Producto.class);
        }
        return null;
    }

    // Eliminar cliente por ID
    public String deleteClienteById(String id) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> writeResult = db.collection(COLLECTION_CLIENTES).document(id).delete();
        return "Cliente eliminado en: " + writeResult.get().getUpdateTime();
    }

    // Buscar boletas por clienteId
    public List<QueryDocumentSnapshot> getBoletasByClienteId(String clienteId) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_BOLETAS).whereEqualTo("clienteId", clienteId).get();
        return future.get().getDocuments();
    }

    // Eliminar producto por ID
    public String deleteProductoById(String id) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> writeResult = db.collection(COLLECTION_PRODUCTOS).document(id).delete();
        return "Producto eliminado en: " + writeResult.get().getUpdateTime();
    }

    // Eliminar boleta por ID
    public String deleteBoletaById(String id) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> writeResult = db.collection(COLLECTION_BOLETAS).document(id).delete();
        return "Boleta eliminada en: " + writeResult.get().getUpdateTime();
    }
}
