package joserodpt.reallogin.common.authentication;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import joserodpt.reallogin.common.profile.ProfileResponse;

public interface AuthenticationProvider {

    CompletableFuture<ProfileResponse> requestProfile(final String username);

    default ExecutorService getThreadPool() {
        final int availableProcessors = Runtime.getRuntime().availableProcessors();
        return Executors.newFixedThreadPool(availableProcessors * 2);
    }
}
