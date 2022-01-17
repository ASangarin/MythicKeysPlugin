package eu.asangarin.mythickeys.api;

import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

/**
 * The Event will fire every tick that a button is held down.
 * If a button was just pressed the isHeld() method will
 * return false, and then true on the next ticks, until the
 * button has been released and is pressed yet again.
 */
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