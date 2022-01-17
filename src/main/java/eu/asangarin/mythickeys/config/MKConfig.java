package eu.asangarin.mythickeys.config;

import eu.asangarin.mythickeys.MythicKeysPlugin;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class MKConfig {
	private final List<MythicKeyInfo> keyInfoList = new ArrayList<>();
	private final Map<Integer, String> keyCommands = new HashMap<>();

	private boolean eventOnCommand;

	// Loads all the keys specified in config.yml and puts them in keyInfoList.
	public void reload(FileConfiguration config) {
		eventOnCommand = config.getBoolean("run_event_on_command");
		keyInfoList.clear();
		keyCommands.clear();
		ConfigurationSection keys = config.getConfigurationSection("Keys");
		if (keys == null) return;
		for (String key : keys.getKeys(false)) {
			if (keys.contains(key, true)) {
				ConfigurationSection section = keys.getConfigurationSection(key);
				if (section == null) {
					error(key);
					return;
				}

				MythicKeyInfo info = MythicKeyInfo.from(section);
				if (info == null) {
					error(key);
					return;
				}

				keyInfoList.add(info);
			}
		}

		ConfigurationSection commands = config.getConfigurationSection("CommandOnPress");
		if (commands == null) return;
		for (String key : commands.getKeys(false)) {
			if (commands.contains(key, true)) {
				try {
					int id = Integer.parseInt(key);
					String cmd = commands.getString(key);
					if (cmd == null) MythicKeysPlugin.get().getLogger().severe("Invalid command for ID: '" + key + "' - Check your syntax!");
					else keyCommands.put(id, cmd);
				} catch (NumberFormatException e) {
					MythicKeysPlugin.get().getLogger().severe("Couldn't parse: '" + key + "' - Is it a valid ID?");
				}
			}
		}
	}

	private void error(String name) {
		MythicKeysPlugin.get().getLogger().severe("Unable to add MythicKey: '" + name + "' - Check your syntax!");
	}
}
