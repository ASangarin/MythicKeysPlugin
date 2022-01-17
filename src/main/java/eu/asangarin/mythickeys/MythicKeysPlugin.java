package eu.asangarin.mythickeys;

import eu.asangarin.mythickeys.cmd.MythicKeysCommand;
import eu.asangarin.mythickeys.config.MKConfig;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class MythicKeysPlugin extends JavaPlugin {
	private static MythicKeysPlugin plugin;

	@Getter
	private final MKConfig conf = new MKConfig();
	private final MKListener mkl = new MKListener();

	public boolean papi;

	@Override
	public void onEnable() {
		plugin = this;
		// Register all the channels needed for incoming/outgoing packets.
		Bukkit.getMessenger().registerIncomingPluginChannel(plugin, MKChannels.HANDSHAKE, mkl);
		Bukkit.getMessenger().registerIncomingPluginChannel(plugin, MKChannels.KEY_PRESS, mkl);
		Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, MKChannels.ADD_KEY);
		Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, MKChannels.LOAD_KEYS);

		// Set up command
		MythicKeysCommand cmd = new MythicKeysCommand();
		PluginCommand command = getCommand("mythickeys");
		if (command != null) {
			command.setExecutor(cmd);
			command.setTabCompleter(cmd);
		}

		papi = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;

		saveDefaultConfig();
		reload();
	}

	@Override
	public void onDisable() {
		// Unregister the channels
		Bukkit.getMessenger().unregisterIncomingPluginChannel(plugin);
		Bukkit.getMessenger().unregisterOutgoingPluginChannel(plugin);
		plugin = null;
	}

	// mythickeys reload
	public void reload() {
		reloadConfig();
		conf.reload(getConfig());
	}

	public static MythicKeysPlugin get() {
		return plugin;
	}
}
