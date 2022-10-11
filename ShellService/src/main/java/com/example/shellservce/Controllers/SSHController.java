package com.example.shellservce.Controllers;

import com.example.shellservce.Entities.SSHClient;
import com.example.shellservce.Entities.UserModel;
import com.example.shellservce.SSHUtils.SSHUtils;
import com.example.shellservce.Services.SSHService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/client/ssh")
@RequiredArgsConstructor
public class SSHController {

    Map<UserModel ,SSHUtils> map = new HashMap<>();

    private final SSHService service;

    private final RestTemplate restTemplate;

    private final String validateUrl = "http://auth-service/api/v1/auth/validate";

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter getResourceUsage(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token,@RequestHeader(name = "sshId") String id) {
        HttpEntity entity = authEntity(token);


        try {
            ResponseEntity<UserModel> model = restTemplate.exchange(validateUrl, HttpMethod.POST, entity, UserModel.class);

            SSHClient client = service.getAllSSHClients(model.getBody().getUsername()).stream().filter(filter -> filter.getId() == Long.valueOf(id)).toList().get(0);
            SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
            SSHUtils utils = SSHUtils.builder()
                    .emitter(emitter)
                    .port(Integer.valueOf(client.getPort()))
                    .keyLocation(client.getKey())
                    .method(client.getMethod())
                    .username(client.getUser())
                    .password(client.getPassword())
                    .host(client.getHost()).build();
            utils.connect();
            map.put(model.getBody(), utils);
            return emitter;
        }   catch (Exception e) {
            return null;
        }
    }
    @PostMapping
    public ResponseEntity command(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token,@RequestHeader(name = "command") String command){
        try {
            HttpEntity entity = authEntity(token);
            ResponseEntity<UserModel> model = restTemplate.exchange(validateUrl, HttpMethod.POST, entity, UserModel.class);
            SSHUtils utils = map.get(model.getBody());
            if(command.equals("+]n")) {
                utils.outputStream.write("\n".getBytes());
            }else{
                utils.outputStream.write(command.getBytes(StandardCharsets.UTF_8));
            }
            return ResponseEntity.ok(command);
        } catch (IOException e) {
            return  ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @DeleteMapping
    public void disconnect(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token){
        HttpEntity entity = authEntity(token);
        try {
            ResponseEntity<UserModel> model = restTemplate.exchange(validateUrl, HttpMethod.POST, entity, UserModel.class);
            SSHUtils utils = map.get(model.getBody());
            utils.getSession().disconnect();
        }catch (Exception e){

        }


    }
    private HttpEntity authEntity( String token){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set(HttpHeaders.AUTHORIZATION,token.split(" ")[1]);
        return new HttpEntity<>(null, headers);
    }
}
