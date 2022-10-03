package pl.simmo.authservice.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.simmo.authservice.DTO.LoginDTO;
import pl.simmo.authservice.DTO.RegisterDTO;
import pl.simmo.authservice.DTO.UserModelDTO;
import pl.simmo.authservice.Exceptions.*;
import pl.simmo.authservice.Models.Role;
import pl.simmo.authservice.Models.RoleEnum;
import pl.simmo.authservice.Models.UserModel;
import pl.simmo.authservice.Repositories.RoleRepository;
import pl.simmo.authservice.Repositories.UserRepository;
import pl.simmo.authservice.Security.TokenManager;
import pl.simmo.authservice.Security.VerificationManager;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    private final VerificationManager verificationManager;

    private final TokenManager tokenManager;

    private final RoleRepository roleRepository;

    public UserModel createUser(RegisterDTO registerDTO) throws UsernameAlreadyInUseException, EmailAlreadyInUseException {
       if( userRepository.findByUsername(registerDTO.getUsername()).isPresent()){
           throw new UsernameAlreadyInUseException("Username already in use: "+registerDTO.getUsername());
       }

       if(userRepository.findByEmail(registerDTO.getEmail()).isPresent()){
           throw new EmailAlreadyInUseException("Email already in use: "+registerDTO.getEmail());
       }

       Role role;

       if(roleRepository.findByName(RoleEnum.USER.name()).isPresent()){
           role = roleRepository.findByName(RoleEnum.USER.name()).get();
       }else {
           role = roleRepository.save(new Role(RoleEnum.USER));
       }

        UserModel model = UserModel.builder()
                .username(registerDTO.getUsername())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .email(registerDTO.getEmail())
                .roles(Set.of(role))
                .active(false)
                .build();

        return userRepository.save(model);
    }


    public String authenticate(LoginDTO loginDTO) throws UserDoesNotExitException, CredentialsDoesNotMatchException, UserNotActivatedException {
       Optional<UserModel> userModel= userRepository.findByUsername(loginDTO.getUsername());
       if(!userModel.isPresent()){
           throw new CredentialsDoesNotMatchException();
       }
       if(!userModel.get().getActive()){
           throw new UserNotActivatedException();
       }
       if(!passwordEncoder.matches(loginDTO.getPassword(),userModel.get().getPassword())){
           throw new CredentialsDoesNotMatchException();
       }



       return tokenManager.generateJwtToken(loginDTO);

    }


    public UserModelDTO getByUsername(String username){

       UserModel userModel =  userRepository.findByUsername(username).orElse(null);

       if(userModel!=null){
           return new UserModelDTO(userModel.getId(),userModel.getUsername(),userModel.getEmail(),userModel.getRoles());
       }
        return null;
    }

    public Optional<UserModel> getByToken(String token){
        String username = verificationManager.getUserNameFromJwtToken(token);
        return userRepository.findByUsername(username);
    }

    public UserModelDTO updateUser(UserModel userModel){
        UserModel userModel1 = userRepository.save(userModel);
        return new UserModelDTO(userModel1.getId(),userModel1.getUsername(),userModel1.getEmail(),userModel1.getRoles());

    }
}
