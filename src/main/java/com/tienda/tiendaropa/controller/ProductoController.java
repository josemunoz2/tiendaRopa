package com.tienda.tiendaropa.controller;

import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.tienda.tiendaropa.model.Producto;
import com.tienda.tiendaropa.service.FirebaseService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private FirebaseService firebaseService;

    @PostMapping
    public String saveProducto(@Valid @RequestBody Producto producto) throws ExecutionException, InterruptedException {
        return firebaseService.saveProducto(producto);
    }

    @GetMapping
    public List<Producto> getProductos() throws ExecutionException, InterruptedException {
        List<QueryDocumentSnapshot> documents = firebaseService.getAllProductos();
        List<Producto> productos = new ArrayList<>();
        for (QueryDocumentSnapshot doc : documents) {
            productos.add(doc.toObject(Producto.class));
        }
        return productos;
    }

    @GetMapping("/{id}")
    public Producto getProductoById(@PathVariable String id) throws ExecutionException, InterruptedException {
        Producto producto = firebaseService.getProductoById(id);
        if (producto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado");
        }
        return producto;
    }

    @PutMapping("/{id}")
    public String updateProducto(@PathVariable String id, @Valid @RequestBody Producto producto) throws ExecutionException, InterruptedException {
        producto.setId(id);
        return firebaseService.saveProducto(producto);
    }

    @DeleteMapping("/{id}")
    public String deleteProducto(@PathVariable String id) throws ExecutionException, InterruptedException {
        return firebaseService.deleteProductoById(id);
    }
}
