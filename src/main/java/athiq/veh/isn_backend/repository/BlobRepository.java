package athiq.veh.isn_backend.repository;

import athiq.veh.isn_backend.model.FileBlob;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlobRepository extends JpaRepository<FileBlob, Long> {
}
