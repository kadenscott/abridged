package dev.kscott.abridged.listeners;

import com.google.inject.Inject;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * Listens on {@link org.bukkit.event.player.PlayerEvent}s.
 */
public class PlayerListeners implements Listener {

    /**
     * The {@link Location} to teleport players to when leaving the world and entering the Nether.
     */
    private static final @NonNull Location worldToNetherLocation = new Location(
            Bukkit.getWorld("world_nether"), 1.5, 65, 1.5, 41, 0
    );

    /**
     * The {@link Location} to teleport players to when leaving the Nether and entering the world.
     */
    private static final @NonNull Location netherToWorldLocation = new Location(
            Bukkit.getWorld("world"), 0.5, 66, 45, 0, 0
    );

    /**
     * The {@link Location} to teleport players to when leaving the world and entering the End.
     */
    private static final @NonNull Location worldToEndLocation = new Location(
            Bukkit.getWorld("world_the_end"), 1, 66, 21, 136, 12.5F
    );

    /**
     * The {@link Location} to spawn the Ender Dragon.
     */
    public static final @NonNull Location endDragonLocation = new Location(
            Bukkit.getWorld("world_the_end"), 0, 64, -18, 1, 6
    );

    /**
     * The MOTD to display upon joining.
     */
    private static final @NonNull Component[] motd = new Component[]{
            MiniMessage.get().parse("<aqua>Welcome to Minecraft: Abridged.</aqua>"),
            MiniMessage.get().parse("<aqua>Wanna beat Minecraft but only got 5 minutes?</aqua>"),
            MiniMessage.get().parse("<aqua>You're in the right place. Have fun!</aqua>"),
            Component.text(" "),
            MiniMessage.get().parse("<gray>Talk in my Discord: <click:open_url:https://chat.ksc.sh><hover:show_text:'<gray>Click to open!</gray>'><aqua>https://chat.ksc.sh</aqua></hover></click></gray>"),
            MiniMessage.get().parse("<gray>Check out the source code: <click:open_url:https://github.com/kadenscott/abridged><hover:show_text:'<gray>Click to open!</gray>'><aqua>https://github.com/kadenscott/abridged</aqua></hover></click></gray>"),
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

    /**
     * Controls where a player goes when using nether portals.
     *
     * @param event {@link PlayerPortalEvent}.
     */
    @EventHandler
    public void onPlayerPortalUse(final @NonNull PlayerChangedWorldEvent event) {
        final @NonNull World world = event.getFrom();
        final @NonNull Player player = event.getPlayer();

        final World.Environment environment = world.getEnvironment();

        if (environment == World.Environment.NETHER) {
            player.teleport(netherToWorldLocation);
        } else {
            final @NonNull World currentWorld = player.getWorld();

            final World.Environment currentEnvironment = currentWorld.getEnvironment();

            if (currentEnvironment == World.Environment.NORMAL) {
                player.teleport(worldToNetherLocation);
            } else if (currentEnvironment == World.Environment.THE_END) {
                player.teleport(worldToEndLocation);

                final @NonNull Collection<EnderDragon> dragons = endDragonLocation.getNearbyEntitiesByType(EnderDragon.class, 5, 5, 5);

                if (dragons.isEmpty()) {
                    endDragonLocation.getWorld().spawn(endDragonLocation, EnderDragon.class);
                }
            }
        }
    }

}
