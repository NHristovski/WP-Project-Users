package hristovski.nikola.users.controllers;

import hristovski.nikola.users.exceptions.FailedToCreateUserException;
import hristovski.nikola.users.exceptions.NoUserFoundException;
import hristovski.nikola.users.models.RegisterRequest;
import hristovski.nikola.users.security.models.ApplicationUser;
import hristovski.nikola.users.security.repositories.ApplicationUserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@Slf4j
public class UsersController {

    private final ApplicationUserRepository applicationUserRepository;
    private final ConversionService conversionService;

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
