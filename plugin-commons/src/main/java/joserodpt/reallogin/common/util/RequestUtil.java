package joserodpt.reallogin.common.util;

import java.time.Duration;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RequestUtil {

    public static final Duration CLIENT_TIMEOUT = Duration.ofSeconds(5);
    public static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(5);
}