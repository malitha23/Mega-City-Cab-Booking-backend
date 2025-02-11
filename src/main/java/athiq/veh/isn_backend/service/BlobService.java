package athiq.veh.isn_backend.service;

import athiq.veh.isn_backend.model.FileBlob;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface BlobService {

    FileBlob saveBlobToLocal(String directory, MultipartFile file) throws IOException;

    byte[] getBlobFromLocal(String directory, String fileName) throws IOException;

    void deleteLocalBlob(String directory, String fileName) throws IOException;
}
