package hristovski.nikola.users.controllers;

import hristovski.nikola.users.exceptions.FailedToCreateUserException;
import hristovski.nikola.users.exceptions.NoUserFoundException;
import hristovski.nikola.users.models.RegisterRequest;
import hristovski.nikola.users.security.models.ApplicationUser;
import hristovski.nikola.users.security.models.Role;
import hristovski.nikola.users.security.models.RoleName;
import hristovski.nikola.users.security.repositories.ApplicationUserRepository;
import hristovski.nikola.users.security.repositories.RoleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.Optional;
import java.util.Set;

@RestController
@AllArgsConstructor
@Slf4j
public class UsersController {

    private final ApplicationUserRepository applicationUserRepository;
    private final RoleRepository roleRepository;
    private final ConversionService conversionService;


    @PostConstruct
    public void init(){
        Optional<Role> roleUser = roleRepository.findByName(RoleName.ROLE_USER);
        if (roleUser.isEmpty()){
            roleRepository.save(new Role(1L,RoleName.ROLE_USER));
        }

        Optional<Role> roleAdmin = roleRepository.findByName(RoleName.ROLE_ADMIN);
        if (roleAdmin.isEmpty()){
            roleRepository.save(new Role(2L,RoleName.ROLE_ADMIN));
        }

        Optional<ApplicationUser> admin = applicationUserRepository.findByUsername("admin");

        if (admin.isEmpty()) {
            RegisterRequest rr = new RegisterRequest("admin", "admin", "admin@gmail.com", "admin", "admin");

            Set<Role> roles = Set.of(roleRepository.findByName(RoleName.ROLE_USER).get(),
                    roleRepository.findByName(RoleName.ROLE_ADMIN).get());
            ApplicationUser user = conversionService.convert(rr, ApplicationUser.class);

            user.setRoles(roles);
            applicationUserRepository.save(user);
            log.info("Added admin user");
        }
    }


    @PostMapping("/register")
    public ResponseEntity register(@Valid @RequestBody RegisterRequest registerRequest) {

        log.info("In register");
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())){
            throw new FailedToCreateUserException("Failed to create user. Message: Passwords do not match");
        }

        try {
            ApplicationUser user = conversionService.convert(registerRequest, ApplicationUser.class);

            applicationUserRepository.save(user);

            log.info("OK register");
            return ResponseEntity.ok().build();
        } catch (DataIntegrityViolationException ex) {
            log.error("Failed to create user because of data integrity violation", ex);

            throw new FailedToCreateUserException("Failed to create user because the data provided violates the data integrity!");
        } catch (Exception ex) {
            log.error("Failed to create user", ex);

            throw new FailedToCreateUserException("Failed to create user. Message: " + ex.getMessage());
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<ApplicationUser> getUser(@PathVariable String username) {
        ApplicationUser applicationUser = applicationUserRepository.findByUsername(username)
                .orElseThrow(() -> new NoUserFoundException(username));

        ApplicationUser user = new ApplicationUser();

        user.setId(applicationUser.getId());
        user.setRoles(applicationUser.getRoles());
        user.setEmail(applicationUser.getEmail());
        user.setName(applicationUser.getName());
        user.setUsername(applicationUser.getUsername());
        user.setPassword(applicationUser.getPassword());

        log.info("OK returning user {}",user);
        return ResponseEntity.ok(user);
    }

}
