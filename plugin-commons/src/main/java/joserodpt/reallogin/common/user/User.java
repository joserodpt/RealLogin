package joserodpt.reallogin.common.user;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class User {

    private final UUID uniqueId;
    private String username;
    private String passwordHash;
    private String salt;
    private boolean paid;
    private boolean logged;
    private int loginAttempts;
    private int loginCountdown;

    public User(final UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.username = null;
        this.passwordHash = null;
        this.salt = null;
        this.paid = false;
        this.logged = false;
        this.loginAttempts = 0;
        this.loginCountdown = 0;
    }
}