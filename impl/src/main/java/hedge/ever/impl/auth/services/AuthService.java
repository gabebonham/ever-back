package hedge.ever.impl.auth.services;

import hedge.ever.common.models.user.UserModel;
import hedge.ever.impl.email.services.EmailService;
import hedge.ever.repository.auth.AuthRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Random;

@AllArgsConstructor
@Service
public class AuthService implements UserDetailsService {
    private AuthRepository repository;
    private PasswordEncoder passwordEncoder;
    private EmailService emailService;
    public void register(String username, String password, String email){
        var defaultRoles = "user";
        repository.create(UserModel.builder()
                .username(username)
                .role(defaultRoles)
                .password(passwordEncoder.encode(password))
                        .email(email)
                .build());
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel myUser = repository.getByUsername(username);
        if (Objects.nonNull(myUser)) {
            return UserModel.builder()
                    .username(myUser.getUsername())
                    .password(myUser.getPassword())
                    .role(myUser.getRole())
                    .build();
        } else {
            throw new UsernameNotFoundException(username);
        }
    }
    public void sendCode(String email){
        String code = generateSixDigitCode();
        var codeDb = repository.getCodeByEmail(email);
        if (Objects.nonNull(codeDb)){
            repository.deleteCode(email);
        }
        repository.generateCode(email,code);
        emailService.sendVerificationEmail(email, code);
    }
    private String generateSixDigitCode() {
        Random random = new Random();
        int number = 100000 + random.nextInt(900000);
        return String.valueOf(number);
    }
    public boolean verifyCode(String email, String code){
        String codeDb = repository.getCodeByEmail(email);
        return codeDb.equals(code);

    }
    public void resetPassword(String email, String password, String code){
        String codeDb = repository.getCodeByEmail(email);
        var isSame = codeDb.equals(code);
        if (isSame){
            repository.resetPassword(password,email);
            repository.deleteCode(email);
        }
    }
}
