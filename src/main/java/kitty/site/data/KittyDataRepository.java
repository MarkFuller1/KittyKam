package kitty.site.data;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface KittyDataRepository extends CrudRepository<KittyData, UUID> {
}
