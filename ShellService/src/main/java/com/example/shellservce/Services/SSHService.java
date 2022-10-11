package com.example.shellservce.Services;

import com.example.shellservce.Entities.Method;
import com.example.shellservce.Entities.SSHClient;
import com.example.shellservce.Exceptions.ClientNotFoundException;
import com.example.shellservce.Repositories.SSHClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class SSHService {

    private final SSHClientRepository repository;

    public SSHClient addOrUpdateSSHClient( SSHClient client){
        if(client.getKey() != null){

            String dirname = client.getUsername();
            String fileName = client.getUsername()+new Random().nextInt();

            File dir = new File(dirname);

            if(!dir.exists()){
                dir.mkdir();
            }

            File file = new File (dir.getAbsolutePath()+"/"+fileName);
            try {
                Writer output = new BufferedWriter(new FileWriter(file));
                output.write(client.getKey());
                output.close();
                client.setKey(dir.getAbsolutePath()+"/"+fileName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

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
