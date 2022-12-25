package kitty.site.data.model;

import kitty.site.data.service.Difference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;
import java.util.function.BiFunction;

import static kitty.site.data.service.KittyProcessingService.GROUPING_MARGIN;
import static kitty.site.data.util.Util.getMillis;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class KittyData implements Comparable<KittyData>, Difference<KittyData, Boolean> {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", nullable = false)
    UUID id;

    @Column(name = "timestamp", nullable = false)
    LocalDateTime timestamp;

    @Override
    public int compareTo(KittyData kd) {
        if (getTimestamp() == null || kd.getTimestamp() == null) {
            return 0;
        }
        return getTimestamp().compareTo(kd.getTimestamp());
    }

    public Boolean isWithinMargin(KittyData first, KittyData second) {
        return isWithinMarginCompare(first, second, (KittyData f, KittyData s) -> {
            return Math.abs(getMillis(f.getTimestamp()) - getMillis(s.getTimestamp())) < GROUPING_MARGIN;
        });
    }

    @Override
    public Boolean isWithinMarginCompare(KittyData first, KittyData second, BiFunction<KittyData, KittyData, Boolean> kittyDataKittyDataBooleanBiFunction) {
        return kittyDataKittyDataBooleanBiFunction.apply(first, second);
    }
}
