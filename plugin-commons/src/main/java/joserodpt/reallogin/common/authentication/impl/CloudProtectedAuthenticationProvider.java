package joserodpt.reallogin.common.authentication.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import joserodpt.reallogin.common.authentication.AuthenticationProvider;
import joserodpt.reallogin.common.exception.AuthenticationRequestException;
import joserodpt.reallogin.common.http.HttpClientAccessor;
import joserodpt.reallogin.common.profile.ProfileResponse;
import joserodpt.reallogin.common.util.RequestUtil;
import joserodpt.reallogin.common.util.UUIDUtil;
import joserodpt.reallogin.common.util.UserAgents;

public class CloudProtectedAuthenticationProvider implements AuthenticationProvider {

    private static final String URL = "https://mcapi.cloudprotected.net/uuid/%s";
    private static final String ID_FIELD = "id";

    @Override
    public CompletableFuture<ProfileResponse> requestProfile(final String username) {
        return CompletableFuture.supplyAsync(() -> {
            final HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(String.format(URL, username)))
                    .header("User-Agent", UserAgents.FIREFOX_USER_AGENT)
                    .timeout(RequestUtil.REQUEST_TIMEOUT)
                    .build();
            try {
                final HttpResponse<String> response = HttpClientAccessor.INSTANCE
                        .getHttpClient()
                        .send(httpRequest, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() != 200) {
                    return null;
                }
                final JsonElement jsonElement = JsonParser.parseString(response.body());
                if (jsonElement == null || !jsonElement.isJsonObject()) return null;
                final JsonObject jsonObject = jsonElement.getAsJsonObject();
                if (jsonObject.has(ID_FIELD)) {
                    final String resultString = jsonObject.get(ID_FIELD).getAsString();
                    final UUID uuid = UUIDUtil.asDashedUUID(resultString);
                    return new ProfileResponse(uuid, username);
                }
            } catch (final IOException | InterruptedException exception) {
                throw new AuthenticationRequestException(exception);
            }
            return null;
        }, this.getThreadPool());
    }
}
