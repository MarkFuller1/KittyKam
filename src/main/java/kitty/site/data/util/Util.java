package kitty.site.data.util;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class Util {
    public static Long getMillis(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
