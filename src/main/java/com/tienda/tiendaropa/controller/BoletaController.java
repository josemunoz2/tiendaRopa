package com.tienda.tiendaropa.controller;

import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.tienda.tiendaropa.model.Boleta;
import com.tienda.tiendaropa.model.DetalleBoleta;
import com.tienda.tiendaropa.model.Producto;
import com.tienda.tiendaropa.service.FirebaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/boletas")
public class BoletaController {

    @Autowired
    private FirebaseService firebaseService;

    @PostMapping
    public String saveBoleta(@RequestBody Boleta boleta) throws ExecutionException, InterruptedException {
        double total = 0.0;

        // Buscar el precio REAL de cada producto
        for (DetalleBoleta detalle : boleta.getDetalles()) {
            Producto producto = firebaseService.getProductoSimpleById(detalle.getProductoId());
            if (producto != null) {
                detalle.setPrecioUnitario(producto.getPrecio());
                total += detalle.getPrecioUnitario() * detalle.getCantidad();
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado: " + detalle.getProductoId());
            }
        }

        boleta.setTotal(total);
        return firebaseService.saveBoleta(boleta);
    }

    @GetMapping
    public List<Boleta> getBoletas() throws ExecutionException, InterruptedException {
        List<QueryDocumentSnapshot> documents = firebaseService.getAllBoletas();
        List<Boleta> boletas = new ArrayList<>();
        for (QueryDocumentSnapshot doc : documents) {
            boletas.add(doc.toObject(Boleta.class));
        }
        return boletas;
    }

    @GetMapping("/{id}")
    public Boleta getBoletaById(@PathVariable String id) throws ExecutionException, InterruptedException {
        Boleta boleta = firebaseService.getBoletaById(id);
        if (boleta == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Boleta no encontrada");
        }
        return boleta;
    }

    @PutMapping("/{id}")
    public String updateBoleta(@PathVariable String id, @RequestBody Boleta boleta) throws ExecutionException, InterruptedException {
        boleta.setId(id);
        return firebaseService.saveBoleta(boleta);
    }

    @DeleteMapping("/{id}")
    public String deleteBoleta(@PathVariable String id) throws ExecutionException, InterruptedException {
        return firebaseService.deleteBoletaById(id);
    }

    @GetMapping("/cliente/{clienteId}")
    public List<Boleta> getBoletasByClienteId(@PathVariable String clienteId) throws ExecutionException, InterruptedException {
        List<QueryDocumentSnapshot> documents = firebaseService.getBoletasByClienteId(clienteId);
        List<Boleta> boletas = new ArrayList<>();
        for (QueryDocumentSnapshot doc : documents) {
            boletas.add(doc.toObject(Boleta.class));
        }
        return boletas;
    }
}
