package eu.asangarin.mythickeys.api;

import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class MythicKeyReleaseEvent extends PlayerEvent {
	@Getter
	private static final HandlerList handlerList = new HandlerList();
	@Getter
	private final NamespacedKey id;

	public MythicKeyReleaseEvent(Player player, NamespacedKey id) {
		super(player);
		this.id = id;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return handlerList;
	}
}