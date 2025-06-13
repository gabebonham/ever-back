package hedge.ever.controllers.auth;

import hedge.ever.common.mappers.GeneralMapper;
import hedge.ever.common.models.auth.*;
import hedge.ever.common.models.user.UserModel;
import hedge.ever.impl.auth.services.AuthService;
import hedge.ever.security.services.JWTService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private AuthService service;
    private JWTService jwtService;
    public AuthController(AuthenticationManager authenticationManager,AuthService service,JWTService jwtService) {
        this.authenticationManager = authenticationManager;
        this.service = service;
        this.jwtService = jwtService;
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginReq request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(token);
    }


    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody UserReq user) {
        service.register(user.getUsername(), user.getPassword(), user.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/send-code")
    public ResponseEntity<?> sendCode(@RequestBody EmailReq request) {
        service.sendCode(request.getEmail());
        return ResponseEntity.ok("Código enviado.");
    }

    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(@RequestBody CodeVerificationReq code) {
        return service.verifyCode(code.getEmail(), code.getCode()) ?
                ResponseEntity.ok("Código validado.") :
                ResponseEntity.ok("Código incorreto.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordReq request) {
        service.resetPassword(request.getEmail(), request.getNewPassword(), request.getCode());
        return ResponseEntity.ok("Senha atualizada.");
    }

}