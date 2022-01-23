package eu.asangarin.mythickeys.api;

import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;

public abstract class MythicKeyEvent extends PlayerEvent {
	@Getter
	private final NamespacedKey id;
	@Getter
	private final boolean registered;

	protected MythicKeyEvent(Player player, NamespacedKey id, boolean registered) {
		super(player);
		this.id = id;
		this.registered = registered;
	}
}
