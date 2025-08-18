package joserodpt.reallogin.common.authentication;

import joserodpt.reallogin.common.authentication.impl.CloudProtectedAuthenticationProvider;
import joserodpt.reallogin.common.authentication.impl.MineToolsAuthenticationProvider;
import joserodpt.reallogin.common.authentication.impl.MojangAuthenticationProvider;

public enum AuthenticationProviderType {

    MOJANG(MojangAuthenticationProvider.class),
    CLOUD_PROTECTED(CloudProtectedAuthenticationProvider.class),
    MINE_TOOLS(MineToolsAuthenticationProvider.class);

    private final Class<? extends AuthenticationProvider> authenticationProviderClass;

    AuthenticationProviderType(final Class<? extends AuthenticationProvider> authenticationProviderClass) {
        this.authenticationProviderClass = authenticationProviderClass;
    }

    public AuthenticationProvider getAuthenticationProvider() {
        if (this.authenticationProviderClass == MojangAuthenticationProvider.class) {
            return AuthenticationProviders.MOJANG_AUTHENTICATION_PROVIDER;
        }
        if (this.authenticationProviderClass == CloudProtectedAuthenticationProvider.class) {
            return AuthenticationProviders.CLOUD_PROTECTED_AUTHENTICATION_PROVIDER;
        }
        if (this.authenticationProviderClass == MineToolsAuthenticationProvider.class) {
            return AuthenticationProviders.MINE_TOOLS_AUTHENTICATION_PROVIDER;
        }
        throw new IllegalStateException("Authentication provider not found");
    }
}
