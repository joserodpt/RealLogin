package joserodpt.reallogin.common.profile;

import java.util.UUID;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class ProfileResponse {

    private final UUID uniqueId;
    private final String username;

    public ProfileResponse(final UUID uniqueId, final String username) {
        this.uniqueId = uniqueId;
        this.username = username;
    }
}