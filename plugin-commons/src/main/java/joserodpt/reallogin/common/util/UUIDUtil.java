package joserodpt.reallogin.common.util;

import java.util.UUID;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UUIDUtil {

    public static UUID asDashedUUID(final String uuidString) {
        if (uuidString == null || uuidString.length() != 32) return null;
        return UUID.fromString(
                uuidString.substring(0, 8) + "-" + uuidString.substring(8, 12) + "-" +
                        uuidString.substring(12, 16) + "-" +  uuidString.substring(16, 20) + "-" +
                        uuidString.substring(20, 32)
        );
    }
}