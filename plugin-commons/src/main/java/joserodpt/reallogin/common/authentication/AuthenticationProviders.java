package joserodpt.reallogin.common.authentication;

import joserodpt.reallogin.common.authentication.impl.CloudProtectedAuthenticationProvider;
import joserodpt.reallogin.common.authentication.impl.MineToolsAuthenticationProvider;
import joserodpt.reallogin.common.authentication.impl.MojangAuthenticationProvider;

public interface AuthenticationProviders {

    /**
     * Official authentication provider using Mojang API.
     */
    MojangAuthenticationProvider MOJANG_AUTHENTICATION_PROVIDER = new MojangAuthenticationProvider();

    /**
     * Unofficial authentication provider with better rate limiting rules than official one.
     */
    CloudProtectedAuthenticationProvider CLOUD_PROTECTED_AUTHENTICATION_PROVIDER = new CloudProtectedAuthenticationProvider();

    /**
     * Alternative provider for {@link CloudProtectedAuthenticationProvider}, also with good rate limits.
     */
    MineToolsAuthenticationProvider MINE_TOOLS_AUTHENTICATION_PROVIDER = new MineToolsAuthenticationProvider();
}