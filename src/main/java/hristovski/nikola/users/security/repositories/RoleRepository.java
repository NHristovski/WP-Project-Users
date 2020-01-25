package hristovski.nikola.users.security.repositories;

import hristovski.nikola.users.security.models.Role;
import hristovski.nikola.users.security.models.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}