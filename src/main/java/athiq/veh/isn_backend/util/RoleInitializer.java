package athiq.veh.isn_backend.util;


import athiq.veh.isn_backend.constant.RoleAuthorityEnum;
import athiq.veh.isn_backend.model.Role;
import athiq.veh.isn_backend.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RoleInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public RoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override //POLYMORPH AND CHANGE CONSTRUCTOR FROM THE RILE MODEL
    public void run(String... args) throws Exception {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(RoleAuthorityEnum.ROLE_USER));
            roleRepository.save(new Role(RoleAuthorityEnum.ROLE_MODERATOR));
            roleRepository.save(new Role(RoleAuthorityEnum.ROLE_ADMIN));
        }
    }
}
