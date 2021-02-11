package dev.kscott.abridged.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Listens on {@link org.bukkit.event.player.PlayerEvent}s.
 */
public class PlayerListeners implements Listener {

    @EventHandler
    public void onPlayerJoin(final @NonNull PlayerJoinEvent event) {

    }

}
