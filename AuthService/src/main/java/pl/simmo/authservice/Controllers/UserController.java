package pl.simmo.authservice.Controllers;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.simmo.authservice.DTO.LoginDTO;
import pl.simmo.authservice.DTO.RegisterDTO;
import pl.simmo.authservice.DTO.TokenDTO;
import pl.simmo.authservice.DTO.UserModelDTO;
import pl.simmo.authservice.Exceptions.*;
import pl.simmo.authservice.Models.UserModel;
import pl.simmo.authservice.Models.VerificationToken;
import pl.simmo.authservice.Security.TokenManager;
import pl.simmo.authservice.Service.UserService;
import pl.simmo.authservice.Service.VerificationService;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final TokenManager tokenManager;


    private final VerificationService verificationService;


    @PostMapping("/register")
    public ResponseEntity createUser(@RequestBody RegisterDTO registerDTO){
        try {
            UserModel userModel = userService.createUser(registerDTO);

            verificationService.saveToken(userModel);

           return ResponseEntity.ok(
                   new UserModelDTO(userModel.getId(),userModel.getUsername(),userModel.getEmail(),userModel.getRoles())
            );
        } catch (UsernameAlreadyInUseException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (EmailAlreadyInUseException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }

    }

    @PostMapping("/authenticate")
    public ResponseEntity login(@RequestBody LoginDTO loginDTO){
        try {
            TokenDTO tokenDTO = new TokenDTO(userService.authenticate(loginDTO));
            return ResponseEntity.ok(tokenDTO);
        } catch (UserDoesNotExitException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (CredentialsDoesNotMatchException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UserNotActivatedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/validate")
    public ResponseEntity validate(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        
        try {
            tokenManager.validateToken(token);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(401).body(e.getMessage());
        }

        return ResponseEntity.status(200).body(userService.getByUsername(tokenManager.getUserNameFromJwtToken(token)));
    }


    @GetMapping("/activate/{token}")
    public ResponseEntity activate(@PathVariable("token") String token){
        try {
           VerificationToken verificationToken = verificationService.findByToken(token);
            Optional<UserModel> userModel = userService.getByToken(token);
            if(userModel.isPresent()){
                UserModel userModel1 = userModel.get();
                if(userModel1.equals(verificationToken.getUser())){
                    userModel1.setActive(true);
                    userService.updateUser(userModel1);
                    verificationService.delete(verificationToken);
                    return ResponseEntity.ok("activated");
                }
            }
            return ResponseEntity.status(401).build();
        } catch (TokenNotFoundException e) {
            return ResponseEntity.status(401).build();
        }
    }

}
