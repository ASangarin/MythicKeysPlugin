package eu.asangarin.mythickeys.api;

import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class MythicKeyPressEvent extends PlayerEvent {
	@Getter
	private static final HandlerList handlerList = new HandlerList();
	@Getter
	private final NamespacedKey id;
	@Getter
	private final boolean held;

	public MythicKeyPressEvent(Player player, NamespacedKey id, boolean held) {
		super(player);
		this.id = id;
		this.held = held;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return handlerList;
	}
}