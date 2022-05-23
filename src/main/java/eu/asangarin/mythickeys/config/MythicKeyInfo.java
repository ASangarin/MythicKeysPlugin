package eu.asangarin.mythickeys.config;

import eu.asangarin.mythickeys.CoolDown;
import eu.asangarin.mythickeys.MythicKeysPlugin;
import eu.asangarin.mythickeys.compat.MythicMobsCompat;
import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

@Getter
public class MythicKeyInfo {
	private MythicKeyInfo(NamespacedKey id, String name, String category, int def, String command, long cd, String cdCmd, String mythicPress, String mythicRelease) {
		this.id = id;
		this.name = name;
		this.category = category;
		this.def = def;
		this.command = command;
		this.cd = cd;
		this.cdCmd = cdCmd;
		this.mythicPress = mythicPress;
		this.mythicRelease = mythicRelease;
	}

	private final NamespacedKey id;
	private final String name, category, command, cdCmd, mythicPress, mythicRelease;
	private final int def;
	private final long cd;

	// Using a static method to insert KeyInfo verification code.
	public static @Nullable MythicKeyInfo from(ConfigurationSection config) {
		if (config.contains("Id") && config.contains("Name") && config.contains("DefaultKey") && config.contains("Category")) {
			NamespacedKey key = NamespacedKey.fromString(config.getString("Id", ""), MythicKeysPlugin.get());
			if (key == null) return null;

			return new MythicKeyInfo(key, config.getString("Name"), config.getString("Category"), config.getInt("DefaultKey"),
					config.getString("RunCommand", ""), config.getLong("CD"), config.getString("CdCommand"), config.getString("SkillPress", ""), config.getString("SkillRelease", ""));
		}
		return null;
	}

	public boolean hasCd() {
		return cd > 0;
	}

	public boolean isCooling(Player player) {
		return CoolDown.isCooling(player.getUniqueId(), id.getKey());
	}

	public void setCd(Player player) {
		CoolDown.setCdToMsLater(player.getUniqueId(), id.getKey(), cd);
	}

	public boolean hasCdCmd() {
		return cdCmd != null && !cdCmd.isEmpty();
	}

	public void runCdCmd(Player player) {
		if (!hasCdCmd()) return;

		final boolean isAdmin = cdCmd.startsWith("!");
		String cmd = (isAdmin ? cdCmd.substring(1) : cdCmd).replace("%player%", player.getName());
		if (cmd.contains("%s%")) {
			Long cdLeft = CoolDown.getCdLeft(player.getUniqueId(), id.getKey());
			cmd = cmd.replaceAll("%s%", String.valueOf(cdLeft/1000));
		}
		if (cmd.contains("%ms%")) {
			Long cdLeft = CoolDown.getCdLeft(player.getUniqueId(), id.getKey());
			cmd = cmd.replaceAll("%ms%", String.valueOf(cdLeft));
		}
		if (MythicKeysPlugin.get().papi) cmd = PlaceholderAPI.setPlaceholders(player, cmd);

		if (isAdmin) Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
		else Bukkit.dispatchCommand(player, cmd);
	}

	public boolean runCommand(Player player) {
		if (command == null || command.isEmpty()) return false;

		final boolean isAdmin = command.startsWith("!");
		String cmd = (isAdmin ? command.substring(1) : command).replace("%player%", player.getName());
		if (MythicKeysPlugin.get().papi) cmd = PlaceholderAPI.setPlaceholders(player, cmd);

		if (isAdmin) Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
		else Bukkit.dispatchCommand(player, cmd);
		return true;
	}

	public boolean hasCommand() {
		return command != null && !command.isEmpty();
	}

	public void mmSkill(Player player, boolean press) {
		if (!MythicKeysPlugin.get().mm) return;
		MythicMobsCompat.runSkill(press ? mythicPress : mythicRelease, player);
	}
}
