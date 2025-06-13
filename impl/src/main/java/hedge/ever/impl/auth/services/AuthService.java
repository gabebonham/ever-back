package hedge.ever.impl.auth.services;

import hedge.ever.common.models.user.UserModel;
import hedge.ever.repository.auth.AuthRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@AllArgsConstructor
@Service
public class AuthService implements UserDetailsService {
    private AuthRepository repository;
    private PasswordEncoder passwordEncoder;
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
}
