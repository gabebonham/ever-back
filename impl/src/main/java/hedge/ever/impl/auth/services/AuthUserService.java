package hedge.ever.impl.auth.services;

import hedge.ever.common.models.user.UserModel;
import hedge.ever.repository.auth.AuthRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class AuthUserService  {
    private AuthRepository repository;
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
