package eu.asangarin.mythickeys.config;

import eu.asangarin.mythickeys.MythicKeysPlugin;
import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

@Getter
public class MKConfig {
	private final Map<NamespacedKey, MythicKeyInfo> keyInfoList = new HashMap<>();

	private boolean eventOnCommand;

	// Loads all the keys specified in config.yml and puts them in keyInfoList.
	public void reload(FileConfiguration config) {
		eventOnCommand = config.getBoolean("run_event_on_command");
		keyInfoList.clear();
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

				keyInfoList.put(info.getId(), info);
			}
		}
	}

	private void error(String name) {
		MythicKeysPlugin.get().getLogger().severe("Unable to add MythicKey: '" + name + "' - Check your syntax!");
	}
}
