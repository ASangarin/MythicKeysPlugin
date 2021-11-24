package eu.asangarin.mythickeys.config;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;

@Getter
public class MythicKeyInfo {
	private MythicKeyInfo(int id, String name, String category, int def) {
		this.id = id;
		this.name = name;
		this.category = category;
		this.def = def;
	}

	private final String name, category;
	private final int def, id;

	// Using a static method to insert KeyInfo verification code.
	public static @Nullable MythicKeyInfo from(ConfigurationSection config) {
		if (config.contains("Id") && config.contains("Name")
				&& config.contains("DefaultKey") && config.contains("Category"))
			return new MythicKeyInfo(config.getInt("Id"), config.getString("Name"),
					config.getString("Category"), config.getInt("DefaultKey"));
		return null;
	}
}
