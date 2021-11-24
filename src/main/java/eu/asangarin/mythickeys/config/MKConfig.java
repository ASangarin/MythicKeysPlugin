package eu.asangarin.mythickeys.config;

import eu.asangarin.mythickeys.MythicKeysPlugin;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class MKConfig {
	@Getter
	private final List<MythicKeyInfo> keyInfoList = new ArrayList<>();

	// Loads all the keys specified in config.yml and puts them in keyInfoList.
	public void reload(FileConfiguration config) {
		keyInfoList.clear();
		ConfigurationSection keys = config.getConfigurationSection("Keys");
		if(keys == null) return;
		for(String key : keys.getKeys(false)) {
			if(keys.contains(key, false)) {
				ConfigurationSection section = keys.getConfigurationSection(key);
				if(section == null) {
					error(key);
					return;
				}

				MythicKeyInfo info = MythicKeyInfo.from(section);
				if(info == null) {
					error(key);
					return;
				}

				keyInfoList.add(info);
			}
		}
	}

	private void error(String name) {
		MythicKeysPlugin.get().getLogger().severe("Unable to add MythicKey: '" + name + "' - Check your syntax!");
	}
}
