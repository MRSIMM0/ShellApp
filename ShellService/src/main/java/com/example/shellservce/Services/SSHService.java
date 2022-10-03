package com.example.shellservce.Services;

import com.example.shellservce.Entities.SSHClient;
import com.example.shellservce.Exceptions.ClientNotFoundException;
import com.example.shellservce.Repositories.SSHClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SSHService {

    private final SSHClientRepository repository;

    public SSHClient addOrUpdateSSHClient( SSHClient client){
        return repository.save(client);
    }

    public List<SSHClient> getAllSSHClients(String username){
        return repository.findSSHClientsByUsername(username).stream().map(ssh-> {
            try {
                return ssh.orElseThrow(ClientNotFoundException::new);
            } catch (ClientNotFoundException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

    public SSHClient getById(Long id) throws ClientNotFoundException {
        return repository.findById(id).orElseThrow(ClientNotFoundException::new);
    }

    public void deleteById( Long id){
        repository.findById(id).ifPresent(
                ssh->{
                    repository.delete(ssh);
                }
        );

    }


}
