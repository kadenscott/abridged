package dev.kscott.abridged.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

/**
 * Listens on Oskar-related events.
 */
public class OskarListener implements Listener {

    /**
     * The message to display when an Oskar-related connection is attempted.
     */
    private static final @NonNull Component oskarMessage = MiniMessage.get().parse(
            "<aqua>unban me i unban u</aqua>"
    );

    /**
     * Listens on OskarJoinEvent.
     *
     * @param event OskarJoinEvent.
     */
    @EventHandler
    public void onOskarJoin(final @NonNull PlayerLoginEvent event) {
        if (!event.getPlayer().getUniqueId().equals(UUID.fromString("20e21989-5521-49df-9750-a4ef8bd5441b"))) {
            return;
        }
        event.disallow(PlayerLoginEvent.Result.KICK_BANNED, LegacyComponentSerializer.legacySection().serialize(oskarMessage));
    }

}
