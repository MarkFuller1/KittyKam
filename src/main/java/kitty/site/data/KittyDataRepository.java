package kitty.site.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface KittyDataRepository extends CrudRepository<KittyData, UUID> {
    List<KittyData> findByTimestampGreaterThanEqual(@NonNull LocalDateTime timestamp);

}
