package joserodpt.reallogin.common.http;

import java.net.http.HttpClient;
import joserodpt.reallogin.common.util.RequestUtil;
import lombok.Getter;

public final class HttpClientAccessor {

    public static final HttpClientAccessor INSTANCE = new HttpClientAccessor();
    @Getter
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(RequestUtil.CLIENT_TIMEOUT)
            .build();

    private HttpClientAccessor() {
    }
}