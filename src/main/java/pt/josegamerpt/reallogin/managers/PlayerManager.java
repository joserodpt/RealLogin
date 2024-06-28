package pt.josegamerpt.reallogin.managers;

/*
 *   _____            _ _                 _
 *  |  __ \          | | |               (_)
 *  | |__) |___  __ _| | |     ___   __ _ _ _ __
 *  |  _  // _ \/ _` | | |    / _ \ / _` | | '_ \
 *  | | \ \  __/ (_| | | |___| (_) | (_| | | | | |
 *  |_|  \_\___|\__,_|_|______\___/ \__, |_|_| |_|
 *                                   __/ |
 *                                  |___/
 *
 * Licensed under the MIT License
 * @author José Rodrigues
 * @link https://github.com/joserodpt/RealLogin
 */

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pt.josegamerpt.reallogin.RealLogin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerManager {
    
    private List<Player> frozen = new ArrayList<>();
    private Map<Player, String> pin = new HashMap<>();
    private Map<Player, ItemStack[]> inv = new HashMap<>();

    public void setupPlayerLogin(Player p) {
        this.pin.put(p, "");
        this.frozen.add(p);
    }

    public Map<Player, ItemStack[]> getPlayerInventories() {
        return this.inv;
    }

    public List<Player> getFrozenPlayers() {
        return this.frozen;
    }

    public Map<Player, String> getPlayerPIN() {
        return this.pin;
    }
}
