package com.example.parking.controller;

import com.example.parking.entity.Client;
import com.example.parking.entity.Views;
import com.example.parking.service.ClientService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("clients")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }


    @GetMapping("/{id}")
    @JsonView(Views.FullClass.class)
    Client findById(@PathVariable("id") Long id) {
        return clientService.findById(id);
    }

    @GetMapping
    @JsonView(Views.Basic.class)
    List<Client> findAll() {
        return clientService.findAll();
    }

    @PostMapping
    @JsonView(Views.FullClass.class)
    Client create(@RequestBody Client newClient) {
        return clientService.create(newClient);
    }

    @PutMapping("/{id}")
    @JsonView(Views.Basic.class)
    Client update(@RequestBody Client newClient, @PathVariable("id") Long id) {
        return clientService.updateOrCreate(newClient, id);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable("id") Client client) {
        clientService.delete(client);
    }

}
