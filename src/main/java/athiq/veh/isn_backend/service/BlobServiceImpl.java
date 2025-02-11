package athiq.veh.isn_backend.service;

import athiq.veh.isn_backend.exception.ResourceNotFoundException;
import athiq.veh.isn_backend.model.FileBlob;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

@Service
public class BlobServiceImpl implements BlobService {

    @Override
    public FileBlob saveBlobToLocal(String directory, MultipartFile file) throws IOException {

        FileBlob fileBlob = new FileBlob();

        String originalFilename = Objects.requireNonNull(file.getOriginalFilename());
        String fileExt = "";
        int dotIndex = originalFilename.lastIndexOf(".");
        if (dotIndex > 0 && dotIndex < originalFilename.length() - 1) {
            fileExt = originalFilename.substring(dotIndex);
        }

        String uniqueFileName = UUID.randomUUID() + "_" + LocalDateTime
                .now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")) + fileExt;

        Path uploadPath = Path.of(directory);
        Path filePath = uploadPath.resolve(uniqueFileName);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        fileBlob.setUuid(uniqueFileName);

        return fileBlob;
    }

    @Override
    public byte[] getBlobFromLocal(String directory, String fileName) throws IOException {
        Path imagePath = Path.of(directory, fileName);

        if (Files.exists(imagePath)) {
            return Files.readAllBytes(imagePath);
        } else {
            return null;
        }
    }

    @Override
    public void deleteLocalBlob(String directory, String fileName) throws IOException {
        Path imagePath = Path.of(directory, fileName);

        if (Files.exists(imagePath)) {
            Files.delete(imagePath);
        } else {
            throw new ResourceNotFoundException("Operation failed file not found");
        }
    }

}
