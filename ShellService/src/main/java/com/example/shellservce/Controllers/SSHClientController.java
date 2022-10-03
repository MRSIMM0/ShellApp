package com.example.shellservce.Controllers;

import com.example.shellservce.Entities.SSHClient;
import com.example.shellservce.Entities.UserModel;
import com.example.shellservce.Services.SSHService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@RestController
@RequestMapping("/api/v1/client")
@RequiredArgsConstructor
public class SSHClientController {

    private final RestTemplate restTemplate;

    private final SSHService service;

    private final String validateUrl = "http://auth-service/api/v1/auth/validate";



    @PostMapping
    public ResponseEntity addSSHClient(@RequestBody SSHClient client,@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token){

        HttpEntity entity = authEntity(token);

        try {
            ResponseEntity<UserModel> model = restTemplate.exchange(validateUrl, HttpMethod.POST, entity, UserModel.class);
            client.setUsername(model.getBody().getUsername());
            return ResponseEntity.ok(service.addOrUpdateSSHClient(client));

        }catch (Exception e){
            return ResponseEntity.status(401).body(e.getMessage());
        }


    }

    @GetMapping("/")
    public ResponseEntity getAllSSHClients(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token){

        HttpEntity entity = authEntity(token);

        try {
            ResponseEntity<UserModel> model = restTemplate.exchange(validateUrl, HttpMethod.POST, entity, UserModel.class);
            return ResponseEntity.ok( service.getAllSSHClients(model.getBody().getUsername()));
        }catch (Exception e){
            return ResponseEntity.status(401).body(e.getMessage());
        }

    }

    @GetMapping("/one/{id}")
    public ResponseEntity getById(@PathVariable(name = "id") Long id,@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {



        HttpEntity entity = authEntity(token);

        try {
            ResponseEntity<UserModel> model = restTemplate.exchange(validateUrl, HttpMethod.POST, entity, UserModel.class);
            return ResponseEntity.ok(service.getAllSSHClients(model.getBody().getUsername()).stream().filter(filter->filter.getId() == id).toList().get(0));

        }catch (Exception e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable(name="id") Long id, @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token){

        HttpEntity entity = authEntity(token);

        try {
            ResponseEntity<UserModel> model = restTemplate.exchange(validateUrl, HttpMethod.POST, entity, UserModel.class);
            service.deleteById(service.getAllSSHClients(model.getBody().getUsername()).stream().filter(filter->filter.getId() == id).toList().get(0).getId());
            return ResponseEntity.ok().build();
        }catch (Exception e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }

    }

    @PatchMapping
    public ResponseEntity updateById(@RequestBody SSHClient client, @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token){
        HttpEntity entity = authEntity(token);

        try {
            ResponseEntity<UserModel> model = restTemplate.exchange(validateUrl, HttpMethod.POST, entity, UserModel.class);
            return ResponseEntity.ok(service.addOrUpdateSSHClient(service.getAllSSHClients(model.getBody().getUsername()).stream().filter(filter->filter.getId() == client.getId()).toList().get(0)));
        }catch (Exception e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }


    private HttpEntity authEntity( String token){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set(HttpHeaders.AUTHORIZATION,token.split(" ")[1]);
        return new HttpEntity<>(null, headers);
    }

}
