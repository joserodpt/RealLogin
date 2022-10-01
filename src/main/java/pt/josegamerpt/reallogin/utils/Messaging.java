package pt.josegamerpt.reallogin.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import pt.josegamerpt.reallogin.RealLogin;

public class Messaging {

    public static void connect(Player player, String serverName) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("ConnectOther");
        out.writeUTF(player.getName());
        out.writeUTF(serverName);
        player.sendPluginMessage(RealLogin.getPlugin(RealLogin.class), "BungeeCord", out.toByteArray());
    }
}
