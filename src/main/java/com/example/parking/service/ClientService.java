package com.example.parking.service;

import com.example.parking.entity.Client;
import com.example.parking.exception.NotFoundException;
import com.example.parking.repository.ClientRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClientService {
    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client findById(Long id){
        return clientRepository.findById(id).orElseThrow(() -> new NotFoundException("Client with id = " + id + " not found"));
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public Client create(Client newClient) {
        newClient.setId(null);
        return clientRepository.saveAndFlush(newClient);
    }

    public Client updateOrCreate(Client newClient, Long id) {
        Optional<Client> clientFromDB = clientRepository.findById(id);
        if (clientFromDB.isPresent()){
            BeanUtils.copyProperties(newClient, clientFromDB.get(), "id");
            return clientRepository.saveAndFlush(clientFromDB.get());
        }
        return create(newClient);
    }

    public void delete(Client client) {
        clientRepository.delete(client);
    }

}


