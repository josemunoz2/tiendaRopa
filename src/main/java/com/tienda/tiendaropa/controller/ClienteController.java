package com.tienda.tiendaropa.controller;

import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.tienda.tiendaropa.model.Cliente;
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
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private FirebaseService firebaseService;

    @PostMapping
    public String saveCliente(@Valid @RequestBody Cliente cliente) throws ExecutionException, InterruptedException {
        return firebaseService.saveCliente(cliente);
    }

    @GetMapping
    public List<Cliente> getClientes() throws ExecutionException, InterruptedException {
        List<QueryDocumentSnapshot> documents = firebaseService.getAllClientes();
        List<Cliente> clientes = new ArrayList<>();
        for (QueryDocumentSnapshot doc : documents) {
            clientes.add(doc.toObject(Cliente.class));
        }
        return clientes; 
    }

    @GetMapping("/{id}")
    public Cliente getClienteById(@PathVariable String id) throws ExecutionException, InterruptedException {
        Cliente cliente = firebaseService.getClienteById(id);
        if (cliente == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado");
        }
        return cliente;
    }

    @PutMapping("/{id}")
    public String updateCliente(@PathVariable String id, @Valid @RequestBody Cliente cliente) throws ExecutionException, InterruptedException {
        cliente.setId(id);
        return firebaseService.saveCliente(cliente);
    }

    @DeleteMapping("/{id}")
    public String deleteCliente(@PathVariable String id) throws ExecutionException, InterruptedException {
        return firebaseService.deleteClienteById(id);
    }
}
