package dev.kscott.abridged.listeners;

import com.google.inject.Inject;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Listens on {@link org.bukkit.event.player.PlayerEvent}s.
 */
public class PlayerListeners implements Listener {

    /**
     * The MOTD to display upon joining.
     */
    private static final @NonNull Component[] motd = new Component[]{
            MiniMessage.get().parse("<aqua>Welcome to Minecraft: Abridged.</aqua>"),
            MiniMessage.get().parse("<aqua>Wanna beat Minecraft but only got 5 minutes?</aqua>"),
            MiniMessage.get().parse("<aqua>You're in the right place. Have fun!</aqua>"),
    };

    /**
     * {@link BukkitAudiences} reference.
     */
    private final @NonNull BukkitAudiences audiences;

    /**
     * Constructs {@link PlayerListeners}.
     *
     * @param audiences {@link BukkitAudiences} reference.
     */
    @Inject
    public PlayerListeners(final @NonNull BukkitAudiences audiences) {
        this.audiences = audiences;
    }

    /**
     * Sends a nice MOTD on player join.
     *
     * @param event {@link PlayerJoinEvent}.
     */
    @EventHandler
    public void onPlayerJoin(final @NonNull PlayerJoinEvent event) {
        final @NonNull Player player = event.getPlayer();
        final @NonNull Audience audience = audiences.player(player);

        for (final @NonNull Component component : motd) {
            audience.sendMessage(component);
        }
    }

}
