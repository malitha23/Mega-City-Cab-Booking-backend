package athiq.veh.isn_backend.repository;

import athiq.veh.isn_backend.constant.RoleAuthorityEnum;
import athiq.veh.isn_backend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByAuthority(RoleAuthorityEnum authority);
}
