package athiq.veh.isn_backend.service;

import athiq.veh.isn_backend.dto.auth.UserMinDTO;
import athiq.veh.isn_backend.dto.request.UserUpdateRequestDTO;
import athiq.veh.isn_backend.dto.response.UserResponseDTO;
import athiq.veh.isn_backend.exception.ResourceNotFoundException;
import athiq.veh.isn_backend.mapper.BlobMapper;
import athiq.veh.isn_backend.model.FileBlob;
import athiq.veh.isn_backend.model.User;
import athiq.veh.isn_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Value("${spring.app.localBlobDirectory}")
    private String FILE_DIRECTORY;
    private final UserRepository userRepository;
    private final BlobService blobService;

    public UserService(UserRepository userRepository, BlobService blobService) {
        this.userRepository = userRepository;
        this.blobService = blobService;
    }


    private String getRole(User user) {
        return user.getRole() != null ? user.getRole().getAuthority().name() : "Unknown"; // Returns the role as a string
    }


    public Set<UserMinDTO> getUsers() {

        List<User> users = this.userRepository.findAll();
        Set<UserMinDTO> UserMinDTOSet = new HashSet<>();

        for (User user : users) {

            UserMinDTOSet.add(new UserMinDTO(
                    user.getId(),
                    user.getEmail(),
                    user.getFirstName(),
                    user.getLastName(),
                    
                    user.getImageUrl(),
                    user.getImageBlob(),
                    getRole(user)

            ));
        }

        return UserMinDTOSet;
    }
//
//    public List<User> findAll() {
//        return userRepository.findAll();
//    }

    public Optional<UserMinDTO> getUserById(Long id) {
        return userRepository.findById(id)
                .map(user -> new UserMinDTO(
                        user.getId(),
                        user.getEmail(),
                        user.getFirstName(),
                        user.getLastName(),
                        
                        user.getImageUrl(),
                        user.getImageBlob(),
                        getRole(user)

                ));
    }

    public Optional<UserMinDTO> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(user -> new UserMinDTO(
                        user.getId(),
                        user.getEmail(),
                        user.getFirstName(),
                        user.getLastName(),
                        
                        user.getImageUrl(),
                        user.getImageBlob(),
                        getRole(user)

                ));
    }

    public UserResponseDTO updateUser(MultipartFile file, UserUpdateRequestDTO requestDTO, Long id) throws IOException {

        FileBlob fileBlob = this.blobService.saveBlobToLocal(FILE_DIRECTORY, file);

        Optional<User> optionalUser = this.userRepository.findById(id);

        if (optionalUser.isPresent()) {
            var user = optionalUser.get();
            if (requestDTO.firstName() != null) {
                user.setFirstName(requestDTO.firstName());
            }
            if (requestDTO.lastName() != null) {
                user.setLastName(requestDTO.lastName());
            }

            user.setImageBlob(fileBlob);

            var updatedUser = this.userRepository.save(user);
            return new UserResponseDTO(
                    updatedUser.getId(),
                    updatedUser.getEmail(),
                    updatedUser.getFirstName(),
                    updatedUser.getLastName(),
                    updatedUser.getImageUrl(),
                    BlobMapper.INSTANCE.toDto(updatedUser.getImageBlob()),
                    updatedUser.getRole()
            );


        } else {
            throw new ResourceNotFoundException("user not found");
        }

    }
    public List<UserMinDTO> searchUsersByFirstName(String firstName) {
        List<User> users = userRepository.findByFirstNameContainingIgnoreCase(firstName);
        return users.stream()
                .map(user -> new UserMinDTO(
                        user.getId(),
                        user.getEmail(),
                        user.getFirstName(),
                        user.getLastName(),
                        
                        user.getImageUrl(),
                        user.getImageBlob(),
                        getRole(user)

                ))
                .collect(Collectors.toList());
    }

}
