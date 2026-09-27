package joserodpt.reallogin;

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
 * @author José Rodrigues © 2020-2026
 * @link https://github.com/joserodpt/RealLogin
 */

import joserodpt.reallogin.config.RLConfig;
import joserodpt.realutils.dialog.SettingsDialog;
import joserodpt.realutils.dialog.SettingsStore;
import joserodpt.realutils.text.Text;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * config.yml as dialogs, for {@code /rl settings}: a menu of categories, each its own form. There is
 * no inventory version, so on a server without dialogs the player is told to edit the file.
 */
public final class ConfigEditor {

    private ConfigEditor() {
    }

    public static void open(final Player p, final RealLogin rl) {
        final SettingsDialog settings = new SettingsDialog("&fReal&7Login &8| &fSettings")
                .icon(Material.TRIPWIRE_HOOK)
                .onSave((player, category) -> {
                    //what /rl reload does for the session timer, so a new session length applies now
                    rl.getPlayerManager().startTickTask();
                    Text.send(player, "&fSettings saved.");
                });
        settings.category("&eGeneral", "&7PINs, sessions and the keypad")
                .slider("Settings.Max-Pin-Length", "Longest a PIN can be", 2, 16, 1)
                .slider("Settings.Max-Session-Time", "Session timeout length", 0, 3600, 30)
                .note("0 turns sessions off")
                .toggle("Settings.Hide-Inventories", "Hide inventories until logged in")
                .toggle("Settings.Use-Custom-Heads", "Number heads on the keypad")
                .text("Settings.Date-Format", "Date format", 64)
                .toggle("Settings.Use-Dialogs", "Use dialogs").note("the keypad is always an inventory");
        settings.category("&9BungeeCord", "&7Sending players to a lobby after logging in")
                .toggle("Settings.BungeeCord.Connect-Lobby", "Send players to the lobby server after logging in")
                .text("Settings.BungeeCord.Lobby-Server", "Lobby server", 64);
        settings.category("&6Messages", "&7What players are told and shown")
                .text("Strings.Prefix", "Plugin prefix", 64)
                .text("Strings.Kick-Message", "When a player leaves from the keypad", 256)
                .text("Strings.GUI.Login", "Login screen title", 64)
                .text("Strings.GUI.Register", "Register screen title", 64)
                .text("Strings.GUI.PIN", "PIN line", 64)
                .text("Strings.Messages.Admin.Bypass-Granted", "Told to the admin after a bypass", 256)
                .text("Strings.Messages.Admin.Bypass-Target", "Told to the bypassed player", 256);

        settings.open(p, SettingsStore.of(RLConfig.file()::get, RLConfig.file()::set, RLConfig::save),
                () -> Text.send(p, "&fThe settings editor needs a server with dialogs (1.21.6 and up). Edit &bconfig.yml &fand use &b/rl reload &finstead."));
    }
}
