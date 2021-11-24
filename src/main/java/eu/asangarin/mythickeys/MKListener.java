package eu.asangarin.mythickeys;

import eu.asangarin.mythickeys.api.MythicKeyPressEvent;
import eu.asangarin.mythickeys.config.MythicKeyInfo;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MKListener implements PluginMessageListener {
	@Override
	public void onPluginMessageReceived(String channel, @NotNull Player player, byte[] message) {
		// When receiving a handshake or key press, forward to their respective methods.
		if (channel.equalsIgnoreCase(MKChannels.HANDSHAKE)) receiveGreeting(player);
		else if (channel.equalsIgnoreCase(MKChannels.KEY_PRESS)) receiveKeyPress(player, new DataInputStream(new ByteArrayInputStream(message)));
	}

	public void receiveKeyPress(Player player, DataInputStream buf) {
		try {
			// Read the key press ID then call the MythicKeyPress event.
			int id = buf.readInt();
			Bukkit.getPluginManager().callEvent(new MythicKeyPressEvent(player, id));
		} catch (IOException ignored) {
			// Exception is ignored.
		}
	}

	public void receiveGreeting(Player player) {
		/* Send this servers specified keybindings to the
		 client. This is delayed to make sure the client is properly
		 connected before attempting to send any data over. */
		Bukkit.getScheduler().runTaskLater(MythicKeysPlugin.get(), () -> {
			for (MythicKeyInfo info : MythicKeysPlugin.get().getConf().getKeyInfoList())
				sendKeyInformation(player, info.getId(), info.getDef(), info.getName(), info.getCategory());
			/* Send the "load" packet after sending every keybinding packet, to tell
			 the client to load all the user-specific keybinds saved on their machine. */
			player.sendPluginMessage(MythicKeysPlugin.get(), MKChannels.LOAD_KEYS, new byte[]{});
		}, 20);
	}

	// Simply send over the information in an add key packet.
	public void sendKeyInformation(Player player, int id, int def, String name, String category) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		try {
			out.writeInt(id);
			out.writeInt(def);
			out.writeUTF(name);
			out.writeUTF(category);
		} catch (IOException ignored) {
		}

		player.sendPluginMessage(MythicKeysPlugin.get(), MKChannels.ADD_KEY, b.toByteArray());
	}
}
