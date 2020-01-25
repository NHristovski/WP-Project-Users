package hristovski.nikola.users.converters;

import hristovski.nikola.users.models.RegisterRequest;
import hristovski.nikola.users.security.models.ApplicationUser;
import hristovski.nikola.users.security.models.Role;
import hristovski.nikola.users.security.models.RoleName;
import hristovski.nikola.users.security.repositories.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RegisterRequestToApplicationUserConverter implements Converter<RegisterRequest, ApplicationUser> {


    private final RoleRepository roleRepository;

    @Override
    public ApplicationUser convert(RegisterRequest registerRequest) {
        ApplicationUser user = new ApplicationUser();

        user.setId(0L);
        user.setUsername(registerRequest.getUsername());
        user.setPassword(encoder().encode(registerRequest.getPassword()));
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());

        Optional<Role> userRole = roleRepository.findByName(RoleName.ROLE_USER);
        if (userRole.isPresent()) {
            user.setRoles(Collections.singleton(userRole.get()));
        } else {
            user.setRoles(Collections.emptySet());
        }

        return user;
    }

    @Bean
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }
}
