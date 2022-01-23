package eu.asangarin.mythickeys.api;

import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class MythicKeyReleaseEvent extends MythicKeyEvent {
	@Getter
	private static final HandlerList handlerList = new HandlerList();

	public MythicKeyReleaseEvent(Player player, NamespacedKey id, boolean registered) {
		super(player, id, registered);
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return handlerList;
	}
}