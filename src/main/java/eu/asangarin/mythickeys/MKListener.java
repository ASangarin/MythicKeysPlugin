package eu.asangarin.mythickeys;

import eu.asangarin.mythickeys.api.MythicKeyPressEvent;
import eu.asangarin.mythickeys.api.MythicKeyReleaseEvent;
import eu.asangarin.mythickeys.config.MythicKeyInfo;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
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
			String namespace = buf.readUTF();
			String key = buf.readUTF();
			boolean firstPress = !buf.readBoolean();
			NamespacedKey id = NamespacedKey.fromString(namespace + ":" + key);

			if (MythicKeysPlugin.get().getConf().getKeyInfoList().containsKey(id)) {
				MythicKeyInfo info = MythicKeysPlugin.get().getConf().getKeyInfoList().get(id);
				boolean eventCmd = MythicKeysPlugin.get().getConf().isEventOnCommand();

				if (firstPress) {
					if (!info.runCommand(player) || eventCmd) Bukkit.getPluginManager().callEvent(new MythicKeyPressEvent(player, id, true));
					info.mmSkill(player, true);
					return;
				}

				if (!info.hasCommand() || eventCmd) Bukkit.getPluginManager().callEvent(new MythicKeyReleaseEvent(player, id, true));
				info.mmSkill(player, false);
			} else {
				Bukkit.getPluginManager()
						.callEvent(firstPress ? new MythicKeyPressEvent(player, id, false) : new MythicKeyReleaseEvent(player, id, false));
			}
		} catch (IOException ignored) {
		}
	}

	public void receiveGreeting(Player player) {
		/* Send this servers specified keybindings to the
		 client. This is delayed to make sure the client is properly
		 connected before attempting to send any data over. */
		Bukkit.getScheduler().runTaskLater(MythicKeysPlugin.get(), () -> {
			for (MythicKeyInfo info : MythicKeysPlugin.get().getConf().getKeyInfoList().values())
				sendKeyInformation(player, info.getId(), info.getDef(), info.getName(), info.getCategory());
			/* Send the "load" packet after sending every keybinding packet, to tell
			 the client to load all the user-specific keybinds saved on their machine. */
			player.sendPluginMessage(MythicKeysPlugin.get(), MKChannels.LOAD_KEYS, new byte[]{});
		}, 20);
	}

	// Simply send over the information in an add key packet.
	public void sendKeyInformation(Player player, NamespacedKey id, int def, String name, String category) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		try {
			out.writeUTF(id.getNamespace());
			out.writeUTF(id.getKey());
			out.writeInt(def);
			out.writeUTF(name);
			out.writeUTF(category);
		} catch (IOException ignored) {
		}

		player.sendPluginMessage(MythicKeysPlugin.get(), MKChannels.ADD_KEY, b.toByteArray());
	}
}
