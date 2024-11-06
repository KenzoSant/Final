package com.fatec.smart_parking.user;

import com.fatec.smart_parking.core.authentication.AuthenticationService;
import com.fatec.smart_parking.core.authentication.LoginDTO;
import com.fatec.smart_parking.core.authentication.LoginResponseDTO;
import com.fatec.smart_parking.core.config.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    TokenService tokenService;


    @GetMapping
    public ResponseEntity<List<UserDTO>> findAll(){
        List<UserDTO> list = userService.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Long id){
        UserDTO user = userService.findById(id);
        return ResponseEntity.ok().body(user);
    }


    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @RequestBody User newUser) {
        UserDTO user = userService.update(id, newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PutMapping("/reset-password")
    public ResponseEntity<LoginResponseDTO> resetPassword(@RequestBody PasswordDTO passwordDTO) {
     User user = this.authenticationService.getCurrentUser();
     user = this.userService.updatePassword(user.getId(),passwordDTO.currentPassword(),passwordDTO.newPassword());
     var usernamePassword = new UsernamePasswordAuthenticationToken(user.getEmail(),passwordDTO.newPassword());
     var auth = this.authenticationManager.authenticate(usernamePassword);
     var token = tokenService.generateToken((User) auth.getPrincipal());
     return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> me(){
       UserDTO userDTO = userService.getCurrentUser();
        return ResponseEntity.ok().body(userDTO);
    }




}
